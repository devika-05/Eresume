package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.About;
import com.digitalresume.api.model.User;
import com.digitalresume.api.repository.AboutRepository;
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
@RequestMapping("/about")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AboutController {
    
    private final AboutRepository aboutRepository;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getAboutByUserId(@PathVariable String userId) {
        Optional<About> about = aboutRepository.findByUserId(userId);
        
        if (about.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("About information not found for user"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("About information retrieved successfully", about.get()));
    }
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyAbout() {
        String userId = getCurrentUserId();
        Optional<About> about = aboutRepository.findByUserId(userId);
        
        if (about.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("About information not found for your profile"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("About information retrieved successfully", about.get()));
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createAbout(@Valid @RequestBody About about) {
        String userId = getCurrentUserId();
        
        // Check if about information already exists for this user
        Optional<About> existingAbout = aboutRepository.findByUserId(userId);
        if (existingAbout.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("About information already exists for this user. Use PUT to update."));
        }
        
        // Set the userId to ensure it matches the authenticated user
        about.setUserId(userId);
        About savedAbout = aboutRepository.save(about);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("About information created successfully", savedAbout));
    }
    
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateAbout(@Valid @RequestBody About about) {
        String userId = getCurrentUserId();
        
        // Find the about information for this user
        Optional<About> existingAbout = aboutRepository.findByUserId(userId);
        if (existingAbout.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("About information not found for this user. Use POST to create."));
        }
        
        // Update the about information, ensuring userId is preserved
        About aboutToUpdate = existingAbout.get();
        about.setId(aboutToUpdate.getId());
        about.setUserId(userId); // Ensure userId can't be changed
        About updatedAbout = aboutRepository.save(about);
        
        return ResponseEntity.ok(ApiResponse.success("About information updated successfully", updatedAbout));
    }
    
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAbout(@RequestParam String userId) {
        Optional<About> existingAbout = aboutRepository.findByUserId(userId);
        if (existingAbout.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("About information not found for specified user"));
        }
        
        aboutRepository.delete(existingAbout.get());
        return ResponseEntity.ok(ApiResponse.success("About information deleted successfully"));
    }
    
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteMyAbout() {
        String userId = getCurrentUserId();
        Optional<About> existingAbout = aboutRepository.findByUserId(userId);
        if (existingAbout.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("About information not found for your profile"));
        }
        
        aboutRepository.delete(existingAbout.get());
        return ResponseEntity.ok(ApiResponse.success("About information deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}