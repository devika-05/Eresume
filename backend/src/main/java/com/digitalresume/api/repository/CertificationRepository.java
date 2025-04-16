package com.digitalresume.api.repository;

import com.digitalresume.api.model.Certification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CertificationRepository extends MongoRepository<Certification, String> {
    List<Certification> findByOrderByIssueDateDesc();
    List<Certification> findByExpiryDateGreaterThan(LocalDate date);
    List<Certification> findByIssuerContainingIgnoreCase(String issuer);
    
    // User-specific queries
    List<Certification> findByUserId(String userId);
    List<Certification> findByUserIdOrderByIssueDateDesc(String userId);
    List<Certification> findByUserIdAndExpiryDateGreaterThan(String userId, LocalDate date);
    List<Certification> findByUserIdAndIssuerContainingIgnoreCase(String userId, String issuer);
}