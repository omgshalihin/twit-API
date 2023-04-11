package com.example.twitapi.security.controller;

import com.example.twitapi.security.dto.AuthRequestJWT;
import com.example.twitapi.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
@CrossOrigin
public class JWTController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public String authenticateAndGetToken(@RequestBody AuthRequestJWT authRequestJWT) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestJWT.username(), authRequestJWT.password()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequestJWT.username());
        } else {
            throw new UsernameNotFoundException("invalid user request!");
        }
    }


}
