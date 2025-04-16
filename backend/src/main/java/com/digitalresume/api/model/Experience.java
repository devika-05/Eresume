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
@Document(collection = "experiences")
public class Experience {
    @Id
    private String id;
    private String userId;
    private String company;
    private String position;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;   // null if current
    private boolean current;
    private String description;
    private String companyLogoUrl;
    private List<String> responsibilities = new ArrayList<>();
    private List<String> achievements = new ArrayList<>();
    
    public Experience(String userId, String company, String position, String location, 
                     LocalDate startDate, LocalDate endDate, boolean current, 
                     String description, String companyLogoUrl) {
        this.userId = userId;
        this.company = company;
        this.position = position;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.current = current;
        this.description = description;
        this.companyLogoUrl = companyLogoUrl;
    }
}