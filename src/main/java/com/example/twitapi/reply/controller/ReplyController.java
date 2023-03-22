package com.example.twitapi.reply.controller;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.reply.service.ReplyService;
import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.user.model.User;
import com.example.twitapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userName}")
    ResponseEntity<List<Reply>> getUserReplies(@PathVariable String userName) {
        User user = userRepository.findUserByUserName(userName);
        return ResponseEntity.status(HttpStatus.OK).body(replyService.getUserReplies(user));
    }

//    http://localhost:8080/api/replies/reply?username=cindy&to=jack&tweetId=641b0c6bc2b3da29e4d7fa7e
    @PostMapping("/tweet")
    ResponseEntity<Tweet> replyTweet(
            @RequestBody Reply reply,
            @RequestParam(name = "username") String userName,
            @RequestParam(name = "to") String userNameReplyTo,
            @RequestParam(name = "tweetId") String tweetId) {
        User user = userRepository.findUserByUserName(userName);
        User userReplyTo = userRepository.findUserByUserName(userNameReplyTo);
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.replyTweet(reply.getReplyContent(), user, userReplyTo, tweetId));
    }

}
