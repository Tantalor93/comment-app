package com.github.tantalor93;

import com.github.tantalor93.model.Comment;
import com.github.tantalor93.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class Application implements CommandLineRunner {

    @Autowired
    CommentsService commentsService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        for (Comment comment : commentsService.findAllComments()) {
            commentsService.deleteComment(comment.getId());
        }
        final Comment myComment = commentsService.createComment("Obenky@gmail.com", "this my first comment");
        commentsService.createComment("Zuzi@seznam.cz", "mine too :)");

        commentsService.upvote(myComment.getId());
        commentsService.upvote(myComment.getId());


        commentsService.deleteComment(myComment.getId());

        commentsService.createComment("Petr@gmail.com", "Where is Obenky?");
        commentsService.createComment("Zuzi@seznam.cz", "Dunno");
        final Comment i_miss_him = commentsService.createComment("Petr@gmail.com", "I miss him");
        commentsService.upvote(i_miss_him.getId());
        commentsService.createComment("Zuzi@seznam.cz", "me too :(");
    }
}
