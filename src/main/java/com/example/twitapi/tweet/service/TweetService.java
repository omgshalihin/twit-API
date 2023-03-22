package com.example.twitapi.tweet.service;

import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.repository.TweetRepository;
import com.example.twitapi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {
    @Autowired
    private TweetRepository tweetRepository;

    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    public List<Tweet> getUserTweets(User user) {
        return tweetRepository.findTweetsByUser(user);
    }

    public Tweet getSpecificTweet(User user, String tweetId) {
        return tweetRepository.getTweetByUserAndTweetId(user, tweetId);
    }

    public Tweet composeTweet(String tweetContent, User user) {
        Tweet tweet = new Tweet();
        tweet.setTweetContent(tweetContent);
        tweet.setUser(user);
        tweetRepository.save(tweet);
        return tweet;
    }

//    public Tweet replyTweet(String replyContent, User user, User userReplyTo, String tweetId) {
//
//        Tweet tweet = tweetRepository.getTweetByUserAndTweetId(userReplyTo, tweetId);
//
//        Reply reply = new Reply();
////        reply.setReplyId(UUID.randomUUID().toString());
//        reply.setReplyContent(replyContent);
//        reply.setUser(user);
//
//        tweet.getTweetReplies().add(reply);
//        tweetRepository.save(tweet);
//
//        return tweet;
//    }
}
