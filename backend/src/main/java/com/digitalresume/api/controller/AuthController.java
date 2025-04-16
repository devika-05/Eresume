package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.dto.AuthRequest;
import com.digitalresume.api.dto.AuthResponse;
import com.digitalresume.api.dto.RegisterRequest;
import com.digitalresume.api.model.Role;
import com.digitalresume.api.model.User;
import com.digitalresume.api.repository.RoleRepository;
import com.digitalresume.api.repository.UserRepository;
import com.digitalresume.api.security.JwtUtils;
import com.digitalresume.api.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        AuthResponse response = AuthResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email is already in use!"));
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Handle roles
        Set<Role> roles = new HashSet<>();
        
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            // Default role is USER - use findByName to get it from the database
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role USER not found. Please restart the application to initialize default roles."));
            roles.add(userRole);
        } else {
            registerRequest.getRoles().forEach(roleName -> {
                // Remove ROLE_ prefix if present, as we store roles without prefix in the database
                String normalizedRoleName = roleName.startsWith("ROLE_") ? roleName.substring(5) : roleName;
                
                Role role = roleRepository.findByName(normalizedRoleName)
                        .orElseThrow(() -> new RuntimeException("Error: Role " + normalizedRoleName + " not found."));
                roles.add(role);
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success("User registered successfully!", savedUser));
    }
}