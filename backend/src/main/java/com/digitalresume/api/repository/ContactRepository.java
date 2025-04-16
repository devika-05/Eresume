package com.digitalresume.api.repository;

import com.digitalresume.api.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {
    Optional<Contact> findByUserId(String userId);
}