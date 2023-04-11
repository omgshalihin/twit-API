package com.example.twitapi.security.dto;

public record AuthRequestJWT(
        String username,
        String password
) {
}
