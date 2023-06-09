package com.example.twitapi.reply.controller;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.reply.service.ReplyService;
import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.service.TweetService;
import com.example.twitapi.user.model.User;
import com.example.twitapi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/replies")
@CrossOrigin
public class ReplyController {

    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;
    @Autowired
    private TweetService tweetService;

    @GetMapping("/{userName}")
    ResponseEntity<List<Reply>> getUserReplies(@PathVariable String userName, @RequestParam("incoming") Boolean incoming) {
        User user = userService.throwErrorIfUserNotFound(userName);
        if (incoming) {
            return ResponseEntity.status(HttpStatus.OK).body(replyService.getUserTweetReplies(user));
        }
        return ResponseEntity.status(HttpStatus.OK).body(replyService.getUserReplies(user));
    }


    @PostMapping("/tweet")
    ResponseEntity<Tweet> replyTweet(
            @RequestBody Reply reply,
            @RequestParam(name = "username") String userName,
            @RequestParam(name = "to") String userNameReplyTo,
            @RequestParam(name = "tweetId") String tweetId) {
        User user = userService.throwErrorIfUserNotFound(userName);
        User userReplyTo = userService.throwErrorIfUserNotFound(userNameReplyTo);
        Tweet tweet = tweetService.throwErrorIfTweetNotFound(tweetId);
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.replyTweet(
                reply.getReplyContent(),
                user,
                userReplyTo,
                tweet.getTweetId()));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteTweetReply(
            @RequestParam(name = "tweetId") String tweetId,
            @RequestParam(name = "replyId") String replyId) {
        Tweet tweet = tweetService.throwErrorIfTweetNotFound(tweetId);
        replyService.deleteTweetReply(tweet.getTweetId(), replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
