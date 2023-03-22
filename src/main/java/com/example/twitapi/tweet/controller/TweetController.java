package com.example.twitapi.tweet.controller;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.service.TweetService;
import com.example.twitapi.user.model.User;
import com.example.twitapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {
    @Autowired
    private TweetService tweetService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    ResponseEntity<List<Tweet>> getAllTweets() {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.getAllTweets());
    }

    @GetMapping("/{userName}")
    ResponseEntity<List<Tweet>> getUserTweets(@PathVariable String userName) {
        User user = userRepository.findUserByUserName(userName);
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.getUserTweets(user));
    }

    @GetMapping("/{userName}/status/{tweetId}")
    ResponseEntity<Tweet> getSpecificTweet(
            @PathVariable String userName,
            @PathVariable String tweetId) {
        User user = userRepository.findUserByUserName(userName);
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.getSpecificTweet(user, tweetId));
    }

    @PostMapping("/compose")
    ResponseEntity<Tweet> composeTweet(
            @RequestBody Tweet tweet,
            @RequestParam(name = "username") String userName) {
        User user = userRepository.findUserByUserName(userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.composeTweet(tweet.getTweetContent(), user));
    }

//    http://localhost:8080/api/tweets/reply?username=cindy&to=jack&tweetId=641b0c6bc2b3da29e4d7fa7e
//    @PostMapping("/reply")
//    ResponseEntity<Tweet> replyTweet(
//            @RequestBody Reply reply,
//            @RequestParam(name = "username") String userName,
//            @RequestParam(name = "to") String userNameReplyTo,
//            @RequestParam(name = "tweetId") String tweetId) {
//        User user = userRepository.findUserByUserName(userName);
//        User userReplyTo = userRepository.findUserByUserName(userNameReplyTo);
//        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.replyTweet(reply.getReplyContent(), user, userReplyTo, tweetId));
//    }
}
