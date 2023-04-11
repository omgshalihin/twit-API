package com.example.twitapi.security.controller;

import com.example.twitapi.security.model.SecurityUser;
import com.example.twitapi.security.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class SecurityUserController {

    @Autowired
    private SecurityUserService securityUserService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome, this endpoint is not secure";
    }
    @PostMapping("/new")
    public String createUser(@RequestBody SecurityUser securityUser) {
        return securityUserService.createUser(securityUser);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<SecurityUser> getAllUsers() {
        return securityUserService.getAllUsers();
    }


}
