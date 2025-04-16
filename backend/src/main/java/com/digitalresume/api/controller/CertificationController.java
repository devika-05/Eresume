package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.Certification;
import com.digitalresume.api.repository.CertificationRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/certifications")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class CertificationController {
    
    private final CertificationRepository certificationRepository;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getCertificationsByUserId(@PathVariable String userId) {
        List<Certification> certifications = certificationRepository.findByUserIdOrderByIssueDateDesc(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Certifications retrieved successfully", certifications));
    }
    
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<ApiResponse> getActiveCertificationsByUserId(@PathVariable String userId) {
        List<Certification> certifications = certificationRepository.findByUserIdAndExpiryDateGreaterThan(
                userId, LocalDate.now());
        
        return ResponseEntity.ok(ApiResponse.success("Active certifications retrieved successfully", certifications));
    }
    
    @GetMapping("/user/{userId}/issuer/{issuer}")
    public ResponseEntity<ApiResponse> getCertificationsByUserIdAndIssuer(
            @PathVariable String userId, 
            @PathVariable String issuer) {
        List<Certification> certifications = 
                certificationRepository.findByUserIdAndIssuerContainingIgnoreCase(userId, issuer);
        
        return ResponseEntity.ok(ApiResponse.success("Certifications by issuer retrieved successfully", certifications));
    }
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyCertifications() {
        String userId = getCurrentUserId();
        List<Certification> certifications = certificationRepository.findByUserIdOrderByIssueDateDesc(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Your certifications retrieved successfully", certifications));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCertificationById(@PathVariable String id) {
        Optional<Certification> certification = certificationRepository.findById(id);
        
        if (certification.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Certification not found"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Certification retrieved successfully", certification.get()));
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createCertification(@Valid @RequestBody Certification certification) {
        String userId = getCurrentUserId();
        
        // Set the userId to ensure it matches the authenticated user
        certification.setUserId(userId);
        Certification savedCertification = certificationRepository.save(certification);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Certification created successfully", savedCertification));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateCertification(
            @PathVariable String id, 
            @Valid @RequestBody Certification certification) {
        String userId = getCurrentUserId();
        
        Optional<Certification> existingCertification = certificationRepository.findById(id);
        if (existingCertification.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Certification not found"));
        }
        
        Certification certificationToUpdate = existingCertification.get();
        
        // Check if the certification belongs to the authenticated user
        if (!certificationToUpdate.getUserId().equals(userId)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to update this certification"));
        }
        
        // Update the certification details
        certification.setId(id);
        certification.setUserId(userId); // Ensure userId can't be changed
        Certification updatedCertification = certificationRepository.save(certification);
        
        return ResponseEntity.ok(ApiResponse.success("Certification updated successfully", updatedCertification));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteCertification(@PathVariable String id) {
        String userId = getCurrentUserId();
        
        Optional<Certification> existingCertification = certificationRepository.findById(id);
        if (existingCertification.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Certification not found"));
        }
        
        Certification certificationToDelete = existingCertification.get();
        
        // Check if the certification belongs to the authenticated user or if the user is an admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!certificationToDelete.getUserId().equals(userId) && !isAdmin) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to delete this certification"));
        }
        
        certificationRepository.delete(certificationToDelete);
        return ResponseEntity.ok(ApiResponse.success("Certification deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}