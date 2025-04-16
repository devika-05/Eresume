package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.Project;
import com.digitalresume.api.repository.ProjectRepository;
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
@RequestMapping("/projects")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectRepository projectRepository;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getProjectsByUserId(@PathVariable String userId) {
        List<Project> projects = projectRepository.findByUserIdOrderByCompletionDateDesc(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved successfully", projects));
    }
    
    @GetMapping("/user/{userId}/tag/{tag}")
    public ResponseEntity<ApiResponse> getProjectsByUserIdAndTag(
            @PathVariable String userId, 
            @PathVariable String tag) {
        List<Project> projects = projectRepository.findByUserIdAndTagsContainingIgnoreCase(userId, tag);
        
        return ResponseEntity.ok(ApiResponse.success("Projects with tag retrieved successfully", projects));
    }
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyProjects() {
        String userId = getCurrentUserId();
        List<Project> projects = projectRepository.findByUserIdOrderByCompletionDateDesc(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Your projects retrieved successfully", projects));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProjectById(@PathVariable String id) {
        Optional<Project> project = projectRepository.findById(id);
        
        if (project.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Project not found"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Project retrieved successfully", project.get()));
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createProject(@Valid @RequestBody Project project) {
        String userId = getCurrentUserId();
        
        // Set the userId to ensure it matches the authenticated user
        project.setUserId(userId);
        Project savedProject = projectRepository.save(project);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", savedProject));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateProject(
            @PathVariable String id, 
            @Valid @RequestBody Project project) {
        String userId = getCurrentUserId();
        
        Optional<Project> existingProject = projectRepository.findById(id);
        if (existingProject.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Project not found"));
        }
        
        Project projectToUpdate = existingProject.get();
        
        // Check if the project belongs to the authenticated user
        if (!projectToUpdate.getUserId().equals(userId)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to update this project"));
        }
        
        // Update the project details
        project.setId(id);
        project.setUserId(userId); // Ensure userId can't be changed
        Project updatedProject = projectRepository.save(project);
        
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", updatedProject));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable String id) {
        String userId = getCurrentUserId();
        
        Optional<Project> existingProject = projectRepository.findById(id);
        if (existingProject.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Project not found"));
        }
        
        Project projectToDelete = existingProject.get();
        
        // Check if the project belongs to the authenticated user or if the user is an admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!projectToDelete.getUserId().equals(userId) && !isAdmin) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to delete this project"));
        }
        
        projectRepository.delete(projectToDelete);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}