package com.example.twitapi.reply.service;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.reply.repository.ReplyRepository;
import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.repository.TweetRepository;
import com.example.twitapi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private TweetRepository tweetRepository;

    public List<Reply> getUserReplies(User user) {
        System.out.println(user);
        var a = replyRepository.findRepliesByUser(user);
        System.out.println(a);
        return a;
    }


    public Tweet replyTweet(String replyContent, User user, User userReplyTo, String tweetId) {

        Tweet tweet = tweetRepository.getTweetByUserAndTweetId(userReplyTo, tweetId);

        Reply reply = new Reply();
        reply.setReplyContent(replyContent);
        reply.setUser(user);
        reply.setUserReplyTo(userReplyTo);

        tweet.getTweetReplies().add(reply);
        tweetRepository.save(tweet);
        replyRepository.save(reply);

        return tweet;
    }
}