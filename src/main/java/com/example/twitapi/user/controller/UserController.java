package com.example.twitapi.user.controller;

import com.example.twitapi.user.model.User;
import com.example.twitapi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping("/{userName}")
    ResponseEntity<User> getUser(@PathVariable String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userName));
    }

    @GetMapping("/{userName}/following")
    ResponseEntity<List<String>> getUserFollowing(@PathVariable String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserFollowing(userName));
    }

    @GetMapping(value = "/{userName}/followers")
    ResponseEntity<List<String>> getUserFollowers(@PathVariable String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserFollowers(userName));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<User> saveUser(@RequestBody User newUser) {
        if (userService.getUser(newUser.getUserName()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User savedUser = userService.saveUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/{userName}/follow-user")
    public ResponseEntity<User> followUser(
            @PathVariable String userName,
            @RequestParam(name = "username") String userNameToFollow) {
        if (userService.getUser(userNameToFollow).getUserFollower().contains(userName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = userService.followUser(userName, userNameToFollow);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/{userName}/unfollow-user")
    public ResponseEntity<User> unfollowUser(
            @PathVariable String userName,
            @RequestParam(name = "username") String userNameToUnfollow) {
        if (!userService.getUser(userNameToUnfollow).getUserFollower().contains(userName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = userService.unfollowUser(userName, userNameToUnfollow);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{userName}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userName) {
        userService.deleteUser(userName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
