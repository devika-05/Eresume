package com.digitalresume.api.controller;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.Skill;
import com.digitalresume.api.repository.SkillRepository;
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
@RequestMapping("/skills")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository skillRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getSkillsByUserId(@PathVariable String userId) {
        List<Skill> skills = skillRepository.findByUserIdOrderByProficiencyDesc(userId);
        return ResponseEntity.ok(ApiResponse.success("Skills retrieved successfully", skills));
    }
    
    @GetMapping("/public")
    public ResponseEntity<ApiResponse> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Skills retrieved successfully", skills));
    }


    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<ApiResponse> getSkillsByUserIdAndCategory(
            @PathVariable String userId, 
            @PathVariable String category) {
        List<Skill> skills = skillRepository.findByUserIdAndCategory(userId, category);
        return ResponseEntity.ok(ApiResponse.success("Skills retrieved successfully for category: " + category, skills));
    }

    @GetMapping("/user/{userId}/proficiency/{level}")
    public ResponseEntity<ApiResponse> getSkillsByUserIdAndProficiencyLevel(
            @PathVariable String userId, 
            @PathVariable int level) {
        List<Skill> skills = skillRepository.findByUserIdAndProficiencyGreaterThanEqual(userId, level);
        return ResponseEntity.ok(ApiResponse.success(
                "Skills retrieved successfully with proficiency level >= " + level, skills));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMySkills() {
        String userId = getCurrentUserId();
        List<Skill> skills = skillRepository.findByUserIdOrderByProficiencyDesc(userId);
        return ResponseEntity.ok(ApiResponse.success("Your skills retrieved successfully", skills));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSkillById(@PathVariable String id) {
        Optional<Skill> skill = skillRepository.findById(id);
        
        if (skill.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Skill not found with id: " + id));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Skill retrieved successfully", skill.get()));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createSkill(@Valid @RequestBody Skill skill) {
        String userId = getCurrentUserId();
        
        // Set the userId to ensure it matches the authenticated user
        skill.setUserId(userId);
        Skill savedSkill = skillRepository.save(skill);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Skill created successfully", savedSkill));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> updateSkill(
            @PathVariable String id, 
            @Valid @RequestBody Skill skill) {
        String userId = getCurrentUserId();
        
        Optional<Skill> existingSkill = skillRepository.findById(id);
        if (existingSkill.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Skill not found with id: " + id));
        }
        
        Skill skillToUpdate = existingSkill.get();
        
        // Check if the skill belongs to the authenticated user
        if (!skillToUpdate.getUserId().equals(userId)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to update this skill"));
        }
        
        // Update the skill details
        skill.setId(id);
        skill.setUserId(userId); // Ensure userId can't be changed
        Skill updatedSkill = skillRepository.save(skill);
        
        return ResponseEntity.ok(ApiResponse.success("Skill updated successfully", updatedSkill));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteSkill(@PathVariable String id) {
        String userId = getCurrentUserId();
        
        Optional<Skill> existingSkill = skillRepository.findById(id);
        if (existingSkill.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Skill not found with id: " + id));
        }
        
        Skill skillToDelete = existingSkill.get();
        
        // Check if the skill belongs to the authenticated user or if the user is an admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!skillToDelete.getUserId().equals(userId) && !isAdmin) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("You don't have permission to delete this skill"));
        }
        
        skillRepository.delete(skillToDelete);
        return ResponseEntity.ok(ApiResponse.success("Skill deleted successfully"));
    }
    
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}