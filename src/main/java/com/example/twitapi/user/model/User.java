package com.example.twitapi.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String userName;
    private String userEmail;
    private List<String> userFollowing = new ArrayList<>();
    private List<String> userFollower = new ArrayList<>();

    public User(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
