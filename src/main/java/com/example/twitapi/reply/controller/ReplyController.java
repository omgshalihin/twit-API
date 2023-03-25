package com.example.twitapi.reply.controller;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.reply.service.ReplyService;
import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.service.TweetService;
import com.example.twitapi.user.model.User;
import com.example.twitapi.user.repository.UserRepository;
import com.example.twitapi.user.service.UserService;
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

    @Autowired
    private UserService userService;
    @Autowired
    private TweetService tweetService;

    @GetMapping("/{userName}")
    ResponseEntity<List<Reply>> getUserReplies(@PathVariable String userName) {
        User user = userService.throwErrorIfUserNotFound(userName);
        return ResponseEntity.status(HttpStatus.OK).body(replyService.getUserReplies(user));
    }

    @PostMapping("/tweet")
    ResponseEntity<Tweet> replyTweet(
            @RequestBody Reply reply,
            @RequestParam(name = "username") String userName,
            @RequestParam(name = "to") String userNameReplyTo,
            @RequestParam(name = "tweetId") String tweetId) {
        if (!userRepository.findUserByUserName(userNameReplyTo).getUserFollower().contains(userName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = userService.throwErrorIfUserNotFound(userName);
        User userReplyTo = userService.throwErrorIfUserNotFound(userNameReplyTo);
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.replyTweet(reply.getReplyContent(), user, userReplyTo, tweetId));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteTweetReply(
            @RequestParam(name = "tweetId") String tweetId,
            @RequestParam(name = "replyId") String replyId) {
        replyService.deleteTweetReply(tweetId, replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
