package com.example.twitapi.tweet.model;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tweets")
public class Tweet {
    @Id
    private String tweetId;
    private String tweetContent;
    private List<Reply> tweetReplies = new ArrayList<>();
    private User user;

    public Tweet(String tweetContent) {
        this.tweetContent = tweetContent;
    }
}
