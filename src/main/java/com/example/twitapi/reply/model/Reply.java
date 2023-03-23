package com.example.twitapi.reply.model;

import com.example.twitapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "replies")
public class Reply {
    @Id
    private String replyId;
    private String replyContent;
    private User user;
    private User userReplyTo;

    public Reply(String replyContent) {
        this.replyContent = replyContent;
    }
}
