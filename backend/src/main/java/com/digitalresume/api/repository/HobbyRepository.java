package com.digitalresume.api.repository;

import com.digitalresume.api.model.Hobby;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HobbyRepository extends MongoRepository<Hobby, String> {
    List<Hobby> findByCategoryContainingIgnoreCase(String category);
    
    // User-specific queries
    List<Hobby> findByUserId(String userId);
    List<Hobby> findByUserIdAndCategoryContainingIgnoreCase(String userId, String category);
}