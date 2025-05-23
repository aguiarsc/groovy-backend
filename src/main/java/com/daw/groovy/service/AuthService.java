package com.daw.groovy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.groovy.dto.AuthRequest;
import com.daw.groovy.dto.AuthResponse;
import com.daw.groovy.dto.UserDto;
import com.daw.groovy.entity.User;
import com.daw.groovy.enums.Role;
import com.daw.groovy.mapper.UserMapper;
import com.daw.groovy.repository.UserRepository;
import com.daw.groovy.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        
        // The ID field is now marked as READ_ONLY with Jackson annotation
        // so it will be ignored during deserialization and we don't need to explicitly set it to null
        
        User user = userMapper.toEntity(userDto);
        
        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .user(userMapper.toDto(savedUser))
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            // First, check if the user exists
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
            
            // Then try to authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            String jwtToken = jwtService.generateToken(user);
            
            return AuthResponse.builder()
                    .token(jwtToken)
                    .user(userMapper.toDto(user))
                    .build();
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            
            // Re-throw the exception to maintain the original behavior
            throw e;
        }
    }
}
