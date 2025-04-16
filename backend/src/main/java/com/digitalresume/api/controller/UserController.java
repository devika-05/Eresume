package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.*;
import com.digitalresume.api.repository.*;
import com.digitalresume.api.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AboutRepository aboutRepository;
    private final ContactRepository contactRepository;
    private final ExperienceRepository experienceRepository;
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final CertificationRepository certificationRepository;
    private final HobbyRepository hobbyRepository;
    
    @GetMapping("/{userId}/resume")
    public ResponseEntity<ApiResponse> getUserResume(@PathVariable String userId) {
        // Check if user exists
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        }
        
        // Create a consolidated object with all resume data
        Map<String, Object> resumeData = new HashMap<>();
        
        // Add user info
        User user = userOptional.get();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        resumeData.put("user", userInfo);
        
        // Add about information
        Optional<About> aboutOptional = aboutRepository.findByUserId(userId);
        resumeData.put("about", aboutOptional.orElse(null));
        
        // Add contact information
        Optional<Contact> contactOptional = contactRepository.findByUserId(userId);
        resumeData.put("contact", contactOptional.orElse(null));
        
        // Add experiences
        List<Experience> experiences = experienceRepository.findByUserIdOrderByStartDateDesc(userId);
        resumeData.put("experiences", experiences);
        
        // Add projects
        List<Project> projects = projectRepository.findByUserIdOrderByCompletionDateDesc(userId);
        resumeData.put("projects", projects);
        
        // Add skills
        List<Skill> skills = skillRepository.findByUserIdOrderByProficiencyDesc(userId);
        resumeData.put("skills", skills);
        
        // Add certifications
        List<Certification> certifications = certificationRepository.findByUserIdOrderByIssueDateDesc(userId);
        resumeData.put("certifications", certifications);
        
        // Add hobbies
        List<Hobby> hobbies = hobbyRepository.findByUserId(userId);
        resumeData.put("hobbies", hobbies);
        
        return ResponseEntity.ok(ApiResponse.success("User resume data retrieved successfully", resumeData));
    }
    
    @GetMapping("/me/resume")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyResume() {
        String userId = getCurrentUserId();
        return getUserResume(userId);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        
        if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user.get()));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        
        if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        }
        
        // Delete all associated resume data
        aboutRepository.findByUserId(id).ifPresent(aboutRepository::delete);
        contactRepository.findByUserId(id).ifPresent(contactRepository::delete);
        experienceRepository.findByUserId(id).forEach(experienceRepository::delete);
        projectRepository.findByUserId(id).forEach(projectRepository::delete);
        skillRepository.findByUserId(id).forEach(skillRepository::delete);
        certificationRepository.findByUserId(id).forEach(certificationRepository::delete);
        hobbyRepository.findByUserId(id).forEach(hobbyRepository::delete);
        
        // Finally delete the user
        userRepository.delete(user.get());
        
        return ResponseEntity.ok(ApiResponse.success("User and all associated data deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}