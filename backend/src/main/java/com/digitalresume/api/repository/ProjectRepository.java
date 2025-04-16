package com.digitalresume.api.repository;

import com.digitalresume.api.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByOrderByCompletionDateDesc();
    List<Project> findByTagsContainingIgnoreCase(String tag);
    
    // User-specific queries
    List<Project> findByUserId(String userId);
    List<Project> findByUserIdOrderByCompletionDateDesc(String userId);
    List<Project> findByUserIdAndTagsContainingIgnoreCase(String userId, String tag);
}