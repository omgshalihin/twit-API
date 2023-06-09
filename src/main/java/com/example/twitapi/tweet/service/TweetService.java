package com.example.twitapi.tweet.service;

import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.repository.TweetRepository;
import com.example.twitapi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    public Tweet throwErrorIfTweetNotFound(String tweetId) {
        if (tweetRepository.getTweetByTweetId(tweetId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found");
        }
        return tweetRepository.getTweetByTweetId(tweetId);
    }

    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    public List<Tweet> getUserTweets(User user) {
        return tweetRepository.findTweetsByUser(user);
    }

    public Tweet getSpecificTweet(User user, String tweetId) {
        throwErrorIfTweetNotFound(tweetId);
        return tweetRepository.getTweetByUserAndTweetId(user, tweetId);
    }

    public Tweet composeTweet(String tweetContent, User user) {
        Tweet tweet = new Tweet();
        tweet.setTweetContent(tweetContent);
        tweet.setUser(user);
        tweetRepository.save(tweet);
        return tweet;
    }

    public void deleteTweet(String tweetId) {
        throwErrorIfTweetNotFound(tweetId);
        tweetRepository.deleteById(tweetId);
    }

    public Tweet pinTweet(User user, String tweetId, boolean pinned) {

        throwErrorIfTweetNotFound(tweetId);

        Tweet tweet = throwErrorIfTweetNotFound(tweetId);
        Tweet pinExists = tweetRepository.getTweetByUserAndPinnedIsTrue(user);

        if (pinned && pinExists != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "a tweet has already been pinned");
        }

        tweet.setPinned(pinned);
        tweetRepository.save(tweet);

        return tweet;
    }

}
