package com.example.twitapi.security.service;

import com.example.twitapi.security.model.SecurityUser;
import com.example.twitapi.security.repository.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityUserService {

    @Autowired
    private SecurityUserRepository securityUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<SecurityUser> getAllUsers() {
        return securityUserRepository.findAll();
    }

    public String createUser(SecurityUser securityUser) {
        securityUser.setPassword(passwordEncoder.encode(securityUser.getPassword()));
        securityUser.setRoles(securityUser.getRoles().toUpperCase());
        securityUserRepository.save(securityUser);
        return String.format("User [%s] has been added to the database", securityUser.getUsername());
    }
}