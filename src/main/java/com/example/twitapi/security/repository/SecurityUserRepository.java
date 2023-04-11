package com.example.twitapi.security.repository;

import com.example.twitapi.security.model.SecurityUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityUserRepository extends MongoRepository<SecurityUser, String> {

    Optional<SecurityUser> findUserModelByUsername(String username);
}
