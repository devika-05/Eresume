package com.digitalresume.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "hobbies")
public class Hobby {
    @Id
    private String id;
    private String userId;
    private String name;
    private String description;
    private String category;
    private String iconUrl;
    
    public Hobby(String userId, String name, String description, String category, String iconUrl) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.iconUrl = iconUrl;
    }
}