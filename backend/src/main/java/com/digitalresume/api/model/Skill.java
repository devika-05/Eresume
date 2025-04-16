package com.digitalresume.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "skills")
public class Skill {
    @Id
    private String id;
    private String userId;
    private String name;
    private String category;
    private int proficiency;  // 1-10 scale
    private String iconUrl;   // optional icon for the skill
    
    public Skill(String userId, String name, String category, int proficiency, String iconUrl) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.proficiency = proficiency;
        this.iconUrl = iconUrl;
    }
}