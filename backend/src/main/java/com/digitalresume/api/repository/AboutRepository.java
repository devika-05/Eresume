package com.digitalresume.api.repository;

import com.digitalresume.api.model.About;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AboutRepository extends MongoRepository<About, String> {
    Optional<About> findByUserId(String userId);
}