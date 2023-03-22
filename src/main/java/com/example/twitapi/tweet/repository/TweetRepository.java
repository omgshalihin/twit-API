package com.example.twitapi.tweet.repository;

import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<Tweet, String> {

    List<Tweet> findTweetsByUser(User user);
    Tweet getTweetByUserAndTweetId(User user, String tweetId);

    Tweet getTweetByTweetId(String tweetId);

}
