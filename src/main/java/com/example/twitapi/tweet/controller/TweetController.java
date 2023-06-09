package com.example.twitapi.tweet.controller;

import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.service.TweetService;
import com.example.twitapi.user.model.User;
import com.example.twitapi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tweets")
@CrossOrigin
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<List<Tweet>> getAllTweets() {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.getAllTweets());
    }

    @GetMapping("/{userName}")
    ResponseEntity<List<Tweet>> getUserTweets(@PathVariable String userName) {
        User user = userService.throwErrorIfUserNotFound(userName);
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.getUserTweets(user));
    }

    @GetMapping("/{userName}/status/{tweetId}")
    ResponseEntity<Tweet> getSpecificTweet(
            @PathVariable String userName,
            @PathVariable String tweetId) {
        User user = userService.throwErrorIfUserNotFound(userName);
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.getSpecificTweet(user, tweetId));
    }

    @PostMapping("/compose")
    ResponseEntity<Tweet> composeTweet(
            @RequestBody Tweet tweet,
            @RequestParam(name = "username") String userName) {
        User user = userService.throwErrorIfUserNotFound(userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.composeTweet(tweet.getTweetContent(), user));
    }

    @DeleteMapping("/{tweetId}")
    ResponseEntity<Void> deleteTweet(@PathVariable String tweetId) {
        tweetService.deleteTweet(tweetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{userName}/status/{tweetId}")
    ResponseEntity<Tweet> pinTweet(
            @PathVariable String userName,
            @PathVariable String tweetId,
            @RequestParam(name = "pinned" ) boolean pinned) {
        User user = userService.throwErrorIfUserNotFound(userName);
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.pinTweet(user, tweetId, pinned));
    }
}
