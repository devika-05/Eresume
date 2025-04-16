package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.Experience;
import com.digitalresume.api.repository.ExperienceRepository;
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
@RequestMapping("/experiences")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceRepository experienceRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getExperiencesByUserId(@PathVariable String userId) {
        List<Experience> experiences = experienceRepository.findByUserIdOrderByStartDateDesc(userId);
        return ResponseEntity.ok(ApiResponse.success("Experiences retrieved successfully", experiences));
    }

    @GetMapping("/user/{userId}/current")
    public ResponseEntity<ApiResponse> getCurrentExperiencesByUserId(@PathVariable String userId) {
        List<Experience> experiences = experienceRepository.findByUserIdAndCurrent(userId, true);
        return ResponseEntity.ok(ApiResponse.success("Current experiences retrieved successfully", experiences));
    }

    @GetMapping("/user/{userId}/company/{companyName}")
    public ResponseEntity<ApiResponse> getExperiencesByUserIdAndCompany(
            @PathVariable String userId, 
            @PathVariable String companyName) {
        List<Experience> experiences = experienceRepository.findByUserIdAndCompanyContainingIgnoreCase(userId, companyName);
        return ResponseEntity.ok(ApiResponse.success(
                "Experiences retrieved successfully for company: " + companyName, experiences));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyExperiences() {
        String userId = getCurrentUserId();
        List<Experience> experiences = experienceRepository.findByUserIdOrderByStartDateDesc(userId);
        return ResponseEntity.ok(ApiResponse.success("Your experiences retrieved successfully", experiences));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getExperienceById(@PathVariable String id) {
        Optional<Experience> experience = experienceRepository.findById(id);
        
        if (experience.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Experience not found with id: " + id));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Experience retrieved successfully", experience.get()));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createExperience(@Valid @RequestBody Experience experience) {
        String userId = getCurrentUserId();
        
        // Set the userId to ensure it matches the authenticated user
        experience.setUserId(userId);
        Experience savedExperience = experienceRepository.save(experience);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Experience created successfully", savedExperience));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateExperience(
            @PathVariable String id, 
            @Valid @RequestBody Experience experience) {
        String userId = getCurrentUserId();
        
        Optional<Experience> existingExperience = experienceRepository.findById(id);
        if (existingExperience.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Experience not found with id: " + id));
        }
        
        Experience experienceToUpdate = existingExperience.get();
        
        // Check if the experience belongs to the authenticated user
        if (!experienceToUpdate.getUserId().equals(userId)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to update this experience"));
        }
        
        // Update the experience details
        experience.setId(id);
        experience.setUserId(userId); // Ensure userId can't be changed
        Experience updatedExperience = experienceRepository.save(experience);
        
        return ResponseEntity.ok(ApiResponse.success("Experience updated successfully", updatedExperience));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteExperience(@PathVariable String id) {
        String userId = getCurrentUserId();
        
        Optional<Experience> existingExperience = experienceRepository.findById(id);
        if (existingExperience.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Experience not found with id: " + id));
        }
        
        Experience experienceToDelete = existingExperience.get();
        
        // Check if the experience belongs to the authenticated user or if the user is an admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!experienceToDelete.getUserId().equals(userId) && !isAdmin) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to delete this experience"));
        }
        
        experienceRepository.delete(experienceToDelete);
        return ResponseEntity.ok(ApiResponse.success("Experience deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}