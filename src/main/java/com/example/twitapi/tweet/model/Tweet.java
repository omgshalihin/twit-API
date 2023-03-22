package com.example.twitapi.tweet.model;

import com.example.twitapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tweets")
public class Tweet {
    @Id
    private String tweetId;
    private String tweetContent;
    private User user;
}
