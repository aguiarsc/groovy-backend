package com.daw.groovy.storage;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.daw.groovy.exception.StorageException;
import com.daw.groovy.exception.StorageFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    private Path rootLocation;

    @PostConstruct
    @Override
    public void init() {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
            log.info("Storage initialized at: {}", rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "unknown_file";
        }
        
        String filename = StringUtils.cleanPath(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        
        try {
            if (filename.contains("..")) {
                // Security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(uniqueFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            
            return uniqueFilename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }
    
    @Override
    public String store(MultipartFile file, String customFilename) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }
        
        String filename = StringUtils.cleanPath(customFilename);
        
        try {
            if (filename.contains("..")) {
                // Security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            
            return filename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public byte[] loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            if (!Files.exists(file)) {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            FileSystemUtils.deleteRecursively(file);
        } catch (IOException e) {
            throw new StorageException("Could not delete file: " + filename, e);
        }
    }
}
