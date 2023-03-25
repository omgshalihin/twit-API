package com.example.twitapi.reply.model;

import com.example.twitapi.user.model.UserDTO;
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
    private UserDTO user;
    private UserDTO userReplyTo;

    public Reply(String replyContent) {
        this.replyContent = replyContent;
    }
}
