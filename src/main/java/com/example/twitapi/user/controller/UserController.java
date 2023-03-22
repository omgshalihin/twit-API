package com.example.twitapi.user.controller;

import com.example.twitapi.user.model.User;
import com.example.twitapi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping
    ResponseEntity<User> saveUser(@RequestBody User newUser) {
        User savedUser = userService.saveUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

//    http://localhost:8080/api/users/user30/follow-user?username=jack
    @PostMapping("/{userName}/follow-user")
    public ResponseEntity<User> followUser(
            @PathVariable String userName,
            @RequestParam(name = "username") String userNameToFollow) {
        User user = userService.followUser(userName, userNameToFollow);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/{userName}/unfollow-user")
    public ResponseEntity<User> unfollowUser(
            @PathVariable String userName,
            @RequestParam(name = "username") String userNameToUnfollow) {
        User user = userService.unfollowUser(userName, userNameToUnfollow);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
