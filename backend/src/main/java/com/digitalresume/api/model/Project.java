package com.digitalresume.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    private String userId;
    private String name;
    private String description;
    private String role;
    private LocalDate startDate;
    private LocalDate completionDate;
    private boolean ongoing;
    private String githubUrl;
    private String demoUrl;
    private String imageUrl;
    private List<String> technologies = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<String> keyFeatures = new ArrayList<>();
    
    public Project(String userId, String name, String description, String role, 
                  LocalDate startDate, LocalDate completionDate, boolean ongoing, 
                  String githubUrl, String demoUrl, String imageUrl) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.role = role;
        this.startDate = startDate;
        this.completionDate = completionDate;
        this.ongoing = ongoing;
        this.githubUrl = githubUrl;
        this.demoUrl = demoUrl;
        this.imageUrl = imageUrl;
    }
}