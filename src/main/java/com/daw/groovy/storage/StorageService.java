package com.daw.groovy.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    
    /**
     * Store a file and return the path where it was stored
     * 
     * @param file The file to store
     * @return The path where the file was stored
     */
    String store(MultipartFile file);
    
    /**
     * Store a file with a custom filename and return the path where it was stored
     * 
     * @param file The file to store
     * @param filename The custom filename to use
     * @return The path where the file was stored
     */
    String store(MultipartFile file, String filename);
    
    /**
     * Load a file as a resource
     * 
     * @param filename The name of the file to load
     * @return The file as a resource
     */
    byte[] loadAsResource(String filename);
    
    /**
     * Delete a file
     * 
     * @param filename The name of the file to delete
     */
    void delete(String filename);
    
    /**
     * Initialize the storage
     */
    void init();
}
