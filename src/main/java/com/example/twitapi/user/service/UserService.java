package com.example.twitapi.user.service;

import com.example.twitapi.user.model.User;
import com.example.twitapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private void userNotFoundError(String userName) {
        if (userRepository.findUserByUserName(userName) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not a valid user");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User newUser) {
        if (userRepository.findUserByUserName(newUser.getUserName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        return userRepository.save(newUser);
    }

    public User followUser(String userName, String userNameToFollow) {
        User user = userRepository.findUserByUserName(userName);
        User userToFollow = userRepository.findUserByUserName(userNameToFollow);

        if (userToFollow.getUserFollower().contains(user.getUserName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a follower");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a follower");
        }

        user.getUserFollowing().remove(userToUnfollow.getUserName());
        userToUnfollow.getUserFollower().remove(user.getUserName());
        userRepository.save(user);
        userRepository.save(userToUnfollow);
        return user;
    }

    public List<String> getUserFollowing(String userName) {
        userNotFoundError(userName);
        return userRepository.findUserByUserName(userName).getUserFollowing();
    }

    public List<String> getUserFollowers(String userName) {
        userNotFoundError(userName);
        return userRepository.findUserByUserName(userName).getUserFollower();
    }

    public User getUser(String userName) {
        userNotFoundError(userName);
        return userRepository.findUserByUserName(userName);
    }

    public void deleteUser(String userName) {
        userNotFoundError(userName);

        User userToBeDeleted = userRepository.findUserByUserName(userName);
        List<String> listOfFollowingUsers = userToBeDeleted.getUserFollowing();

        listOfFollowingUsers.forEach(followingUserUserName -> {
            User followingUser = userRepository.findUserByUserName(followingUserUserName);
            followingUser.getUserFollower().remove(userName);
            userRepository.save(followingUser);
        });

        userRepository.deleteUserByUserName(userName);
    }

}
