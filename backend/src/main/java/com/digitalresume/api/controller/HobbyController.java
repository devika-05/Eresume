package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.Hobby;
import com.digitalresume.api.repository.HobbyRepository;
import com.digitalresume.api.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/hobbies")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class HobbyController {
    
    private final HobbyRepository hobbyRepository;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getHobbiesByUserId(@PathVariable String userId) {
        List<Hobby> hobbies = hobbyRepository.findByUserId(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Hobbies retrieved successfully", hobbies));
    }
    
    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<ApiResponse> getHobbiesByUserIdAndCategory(
            @PathVariable String userId, 
            @PathVariable String category) {
        List<Hobby> hobbies = hobbyRepository.findByUserIdAndCategoryContainingIgnoreCase(userId, category);
        
        return ResponseEntity.ok(ApiResponse.success("Hobbies by category retrieved successfully", hobbies));
    }
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyHobbies() {
        String userId = getCurrentUserId();
        List<Hobby> hobbies = hobbyRepository.findByUserId(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Your hobbies retrieved successfully", hobbies));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getHobbyById(@PathVariable String id) {
        Optional<Hobby> hobby = hobbyRepository.findById(id);
        
        if (hobby.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Hobby not found"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Hobby retrieved successfully", hobby.get()));
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createHobby(@Valid @RequestBody Hobby hobby) {
        String userId = getCurrentUserId();
        
        // Set the userId to ensure it matches the authenticated user
        hobby.setUserId(userId);
        Hobby savedHobby = hobbyRepository.save(hobby);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Hobby created successfully", savedHobby));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateHobby(
            @PathVariable String id, 
            @Valid @RequestBody Hobby hobby) {
        String userId = getCurrentUserId();
        
        Optional<Hobby> existingHobby = hobbyRepository.findById(id);
        if (existingHobby.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Hobby not found"));
        }
        
        Hobby hobbyToUpdate = existingHobby.get();
        
        // Check if the hobby belongs to the authenticated user
        if (!hobbyToUpdate.getUserId().equals(userId)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to update this hobby"));
        }
        
        // Update the hobby details
        hobby.setId(id);
        hobby.setUserId(userId); // Ensure userId can't be changed
        Hobby updatedHobby = hobbyRepository.save(hobby);
        
        return ResponseEntity.ok(ApiResponse.success("Hobby updated successfully", updatedHobby));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteHobby(@PathVariable String id) {
        String userId = getCurrentUserId();
        
        Optional<Hobby> existingHobby = hobbyRepository.findById(id);
        if (existingHobby.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Hobby not found"));
        }
        
        Hobby hobbyToDelete = existingHobby.get();
        
        // Check if the hobby belongs to the authenticated user or if the user is an admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!hobbyToDelete.getUserId().equals(userId) && !isAdmin) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to delete this hobby"));
        }
        
        hobbyRepository.delete(hobbyToDelete);
        return ResponseEntity.ok(ApiResponse.success("Hobby deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}