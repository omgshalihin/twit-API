package com.example.twitapi.reply.repository;

import com.example.twitapi.reply.model.Reply;
import com.example.twitapi.user.model.UserDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends MongoRepository<Reply, String> {
    List<Reply> findRepliesByUser(UserDTO userDTO);
    List<Reply> findRepliesByUserReplyTo(UserDTO userDTO);

    Reply findReplyByReplyId(String replyId);
}
