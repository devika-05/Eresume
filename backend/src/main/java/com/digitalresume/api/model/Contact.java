package com.digitalresume.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact")
public class Contact {
    @Id
    private String id;
    private String userId;
    private String email;
    private String phone;
    private String linkedIn;
    private String github;
    private String twitter;
    private String website;
    
    public Contact(String userId, String email, String phone, String linkedIn, String github, 
                  String twitter, String website) {
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.linkedIn = linkedIn;
        this.github = github;
        this.twitter = twitter;
        this.website = website;
    }
}