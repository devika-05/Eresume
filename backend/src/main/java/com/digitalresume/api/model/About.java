package com.digitalresume.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "about")
public class About {
    @Id
    private String id;
    private String userId;
    private String fullName;
    private String title;
    private String summary;
    private String profileImageUrl;
    private String locationCity;
    private String locationCountry;
    
    public About(String userId, String fullName, String title, String summary, String profileImageUrl, 
                 String locationCity, String locationCountry) {
        this.userId = userId;
        this.fullName = fullName;
        this.title = title;
        this.summary = summary;
        this.profileImageUrl = profileImageUrl;
        this.locationCity = locationCity;
        this.locationCountry = locationCountry;
    }
}