package com.example.twitapi.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "security-users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser {

    @MongoId
    private String userId;
    private String username;
    private String password;
    private String email;
    private String roles;
}
