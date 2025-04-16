package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.Contact;
import com.digitalresume.api.repository.ContactRepository;
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
@RequestMapping("/contact")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ContactController {
    
    private final ContactRepository contactRepository;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getContactByUserId(@PathVariable String userId) {
        Optional<Contact> contact = contactRepository.findByUserId(userId);
        
        if (contact.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Contact information not found for user"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Contact information retrieved successfully", contact.get()));
    }
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyContact() {
        String userId = getCurrentUserId();
        Optional<Contact> contact = contactRepository.findByUserId(userId);
        
        if (contact.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Contact information not found for your profile"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Contact information retrieved successfully", contact.get()));
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createContact(@Valid @RequestBody Contact contact) {
        String userId = getCurrentUserId();
        
        // Check if contact information already exists for this user
        Optional<Contact> existingContact = contactRepository.findByUserId(userId);
        if (existingContact.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Contact information already exists for this user. Use PUT to update."));
        }
        
        // Set the userId to ensure it matches the authenticated user
        contact.setUserId(userId);
        Contact savedContact = contactRepository.save(contact);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Contact information created successfully", savedContact));
    }
    
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateContact(@Valid @RequestBody Contact contact) {
        String userId = getCurrentUserId();
        
        // Find the contact information for this user
        Optional<Contact> existingContact = contactRepository.findByUserId(userId);
        if (existingContact.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Contact information not found for this user. Use POST to create."));
        }
        
        // Update the contact information, ensuring userId is preserved
        Contact contactToUpdate = existingContact.get();
        contact.setId(contactToUpdate.getId());
        contact.setUserId(userId); // Ensure userId can't be changed
        Contact updatedContact = contactRepository.save(contact);
        
        return ResponseEntity.ok(ApiResponse.success("Contact information updated successfully", updatedContact));
    }
    
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteContact(@RequestParam String userId) {
        Optional<Contact> existingContact = contactRepository.findByUserId(userId);
        if (existingContact.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Contact information not found for specified user"));
        }
        
        contactRepository.delete(existingContact.get());
        return ResponseEntity.ok(ApiResponse.success("Contact information deleted successfully"));
    }
    
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteMyContact() {
        String userId = getCurrentUserId();
        Optional<Contact> existingContact = contactRepository.findByUserId(userId);
        if (existingContact.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Contact information not found for your profile"));
        }
        
        contactRepository.delete(existingContact.get());
        return ResponseEntity.ok(ApiResponse.success("Contact information deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}