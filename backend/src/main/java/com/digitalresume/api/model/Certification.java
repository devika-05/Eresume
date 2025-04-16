package com.digitalresume.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "certifications")
public class Certification {
    @Id
    private String id;
    private String userId;
    private String name;
    private String issuer;
    private String credentialId;
    private String credentialUrl;
    private LocalDate issueDate;
    private LocalDate expiryDate;  // null if doesn't expire
    private boolean noExpiry;
    private String description;
    private String badgeUrl;      // URL to certification badge image
    
    public Certification(String userId, String name, String issuer, String credentialId, 
                        String credentialUrl, LocalDate issueDate, LocalDate expiryDate, 
                        boolean noExpiry, String description, String badgeUrl) {
        this.userId = userId;
        this.name = name;
        this.issuer = issuer;
        this.credentialId = credentialId;
        this.credentialUrl = credentialUrl;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.noExpiry = noExpiry;
        this.description = description;
        this.badgeUrl = badgeUrl;
    }
}