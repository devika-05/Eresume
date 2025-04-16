package com.digitalresume.api.repository;

import com.digitalresume.api.model.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends MongoRepository<Skill, String> {
    List<Skill> findByCategory(String category);
    List<Skill> findByProficiencyGreaterThanEqual(int proficiency);
    
    // User-specific queries
    List<Skill> findByUserId(String userId);
    List<Skill> findByUserIdAndCategory(String userId, String category);
    List<Skill> findByUserIdAndProficiencyGreaterThanEqual(String userId, int proficiency);
    List<Skill> findByUserIdOrderByProficiencyDesc(String userId);
}