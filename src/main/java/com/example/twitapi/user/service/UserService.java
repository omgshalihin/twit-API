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
        if (userRepository.findUserByUserName(newUser.getUserName()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(newUser);
    }

    public User followUser(String userName, String userNameToFollow) {

        User user = userRepository.findUserByUserName(userName);
        User userToFollow = userRepository.findUserByUserName(userNameToFollow);

        if (userToFollow.getUserFollower().contains(user.getUserName())) {
            throw new IllegalArgumentException("User is already a follower");
        }

        user.getUserFollowing().add(userToFollow.getUserName());
        userToFollow.getUserFollower().add(user.getUserName());
        userRepository.save(user);
        userRepository.save(userToFollow);
        return user;
    }

    public User unfollowUser(String userName, String userNameToUnfollow) {

        User user = userRepository.findUserByUserName(userName);
        User userToUnfollow = userRepository.findUserByUserName(userNameToUnfollow);

        if (!userToUnfollow.getUserFollower().contains(user.getUserName())) {
            throw new IllegalArgumentException("User is not a follower");
        }

        user.getUserFollowing().remove(userToUnfollow.getUserName());
        userToUnfollow.getUserFollower().remove(user.getUserName());
        userRepository.save(user);
        userRepository.save(userToUnfollow);
        return user;
    }

    public List<String> getUserFollowing(String userName) {
        return userRepository.findUserByUserName(userName).getUserFollowing();
    }

    public List<String> getUserFollowers(String userName) {
        return userRepository.findUserByUserName(userName).getUserFollower();
    }

    public User getUser(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    public void deleteUser(String userName) {
        userRepository.deleteUserByUserName(userName);
    }
}
