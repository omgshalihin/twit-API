package com.example.twitapi.reply.service;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.reply.repository.ReplyRepository;
import com.example.twitapi.tweet.model.Tweet;
import com.example.twitapi.tweet.repository.TweetRepository;
import com.example.twitapi.user.model.User;
import com.example.twitapi.user.model.UserDTO;
import com.example.twitapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Reply> getUserReplies(User user) {
        return replyRepository.findRepliesByUser(user);
    }

    public Tweet replyTweet(String replyContent, User user, User userReplyTo, String tweetId) {

        if (!userRepository.findUserByUserName(userReplyTo.getUserName()).getUserFollower().contains(user.getUserName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a follower");
        }

        Reply reply = new Reply();
        reply.setReplyContent(replyContent);
        reply.setUser(new UserDTO(user.getUserId(), user.getUserName(), user.getUserEmail()));
        reply.setUserReplyTo(new UserDTO(userReplyTo.getUserId(), userReplyTo.getUserName(), userReplyTo.getUserEmail()));
        replyRepository.save(reply);

        Tweet tweet = tweetRepository.getTweetByUserAndTweetId(userReplyTo, tweetId);
        tweet.getTweetReplies().add(reply);
        tweetRepository.save(tweet);

        return tweet;
    }

    public void deleteTweetReply(String tweetId, String replyId) {

        if (replyRepository.findReplyByReplyId(replyId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Reply ID");
        }

        Reply tweetReplyToDelete = replyRepository.findReplyByReplyId(replyId);
        Tweet tweet = tweetRepository.getTweetByTweetId(tweetId);

        tweet.getTweetReplies().remove(tweetReplyToDelete);
        tweetRepository.save(tweet);
        replyRepository.deleteById(replyId);
    }
}
