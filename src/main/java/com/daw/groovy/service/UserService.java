package com.daw.groovy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.groovy.dto.UserDto;
import com.daw.groovy.entity.User;
import com.daw.groovy.enums.Role;
import com.daw.groovy.exception.ResourceNotFoundException;
import com.daw.groovy.mapper.UserMapper;
import com.daw.groovy.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }
    
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }
    
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }
    
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        
        User user = userMapper.toEntity(userDto);
        
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Check if email is being changed and if it's already in use
        if (userDto.getEmail() != null && !user.getEmail().equals(userDto.getEmail()) && 
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        
        // Store the current password and role in case they're not provided in the update
        String currentPassword = user.getPassword();
        Role currentRole = user.getRole();
        
        // Update the user entity from the DTO
        userMapper.updateEntityFromDto(userDto, user);
        
        // If password is null or empty in the DTO, keep the current password
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            user.setPassword(currentPassword);
        } else {
            // Otherwise, encode and set the new password
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        
        // Ensure the role is preserved if not explicitly set in the update
        if (user.getRole() == null) {
            user.setRole(currentRole);
        }
        
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDto getCurrentAuthenticatedUser() {
        // Get the authentication object from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }
        
        // Get the username (email) from the authentication object
        String email = authentication.getName();
        
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Convert to DTO and return
        return userMapper.toDto(user);
    }
}
