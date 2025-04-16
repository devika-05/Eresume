package com.digitalresume.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalresume.api.dto.ApiResponse;
import com.digitalresume.api.model.About;
import com.digitalresume.api.model.Certification;
import com.digitalresume.api.model.Contact;
import com.digitalresume.api.model.Experience;
import com.digitalresume.api.model.Hobby;
import com.digitalresume.api.model.Project;
import com.digitalresume.api.model.Skill;
import com.digitalresume.api.repository.AboutRepository;
import com.digitalresume.api.repository.CertificationRepository;
import com.digitalresume.api.repository.ContactRepository;
import com.digitalresume.api.repository.ExperienceRepository;
import com.digitalresume.api.repository.HobbyRepository;
import com.digitalresume.api.repository.ProjectRepository;
import com.digitalresume.api.repository.SkillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class PublicController {
	
	@Autowired
	public SkillRepository skillRepository;
	
	@Autowired
	public AboutRepository aboutRepository;
	
	@Autowired
	public ContactRepository contactRepository;
	
	@Autowired
	public ProjectRepository projectRepository;
	
	@Autowired
	public ExperienceRepository experienceRepository;
	
	@Autowired
	public HobbyRepository hobbyRepository;
	
	@Autowired
	public CertificationRepository certificationRepository;
	
	@GetMapping("/skills")
    public ResponseEntity<ApiResponse> getSkills() {
        List<Skill> skills = skillRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Skills retrieved successfully", skills));
    }
	
	@GetMapping("/about")
    public ResponseEntity<ApiResponse> getAbout() {
        List<About> about = aboutRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("About retrieved successfully", about));
    }
	
	@GetMapping("/contact")
    public ResponseEntity<ApiResponse> getContact() {
        List<Contact> contact = contactRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Contact retrieved successfully", contact));
    }
	
	@GetMapping("/projects")
    public ResponseEntity<ApiResponse> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved successfully", projects));
    }
	
	@GetMapping("/experience")
    public ResponseEntity<ApiResponse> getExperience() {
        List<Experience> experience = experienceRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Experience retrieved successfully", experience));
    }
	
	@GetMapping("/certification")
    public ResponseEntity<ApiResponse> getCertification() {
        List<Certification> certificate = certificationRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Certification retrieved successfully", certificate));
    }
	
	@GetMapping("/hobbies")
    public ResponseEntity<ApiResponse> getHobbies() {
        List<Hobby> hobbies = hobbyRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Hobbies retrieved successfully", hobbies));
    }

}
