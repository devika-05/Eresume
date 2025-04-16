package com.digitalresume.api.repository;

import com.digitalresume.api.model.Experience;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExperienceRepository extends MongoRepository<Experience, String> {
    List<Experience> findAllByOrderByStartDateDesc();
    List<Experience> findByStartDateGreaterThanEqual(LocalDate date);
    List<Experience> findByCompanyContainingIgnoreCase(String company);
    List<Experience> findByCurrent(boolean current);
    
    // User-specific queries
    List<Experience> findByUserId(String userId);
    List<Experience> findByUserIdOrderByStartDateDesc(String userId);
    List<Experience> findByUserIdAndCompanyContainingIgnoreCase(String userId, String company);
    List<Experience> findByUserIdAndCurrent(String userId, boolean current);
}