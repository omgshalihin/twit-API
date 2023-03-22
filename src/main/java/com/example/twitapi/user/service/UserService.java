package com.example.twitapi.user.service;

import com.example.twitapi.user.model.User;
import com.example.twitapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User followUser(String userName, String userNameToFollow) {
        User user = userRepository.findUserByUserName(userName);
        User userToFollow = userRepository.findUserByUserName(userNameToFollow);
        user.getUserFollowing().add(userToFollow.getUserId());
        userToFollow.getUserFollower().add(user.getUserId());
        userRepository.save(user);
        userRepository.save(userToFollow);
        return user;
    }

    public User unfollowUser(String userName, String userNameToUnfollow) {
        User user = userRepository.findUserByUserName(userName);
        User userToUnfollow = userRepository.findUserByUserName(userNameToUnfollow);
        user.getUserFollowing().remove(userToUnfollow.getUserId());
        userToUnfollow.getUserFollower().remove(user.getUserId());
        userRepository.save(user);
        userRepository.save(userToUnfollow);
        return user;
    }
}
