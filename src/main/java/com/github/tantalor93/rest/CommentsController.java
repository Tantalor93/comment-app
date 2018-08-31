package com.github.tantalor93.rest;

import com.github.tantalor93.dto.CommentToCreate;
import com.github.tantalor93.model.Comment;
import com.github.tantalor93.service.CommentsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping(value = "/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> createComment(@RequestBody final CommentToCreate commentToCreate) {
        final Comment comment = commentsService.createComment(
                commentToCreate.getAuthor(),
                commentToCreate.getAuthor()
        );
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/comments")
    public ResponseEntity<Iterable<Comment>> findAllComments() {
        return ResponseEntity.ok(commentsService.findAllComments());
    }

    @GetMapping("/comments/top")
    public ResponseEntity<List<Comment>> findTopComments(@RequestParam("n") Optional<Integer> n) {
        return ResponseEntity.ok(commentsService.findTopNUpvotedComments(n.orElse(10)));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> findComment(@PathVariable("id") String id) {
        final Optional<Comment> comment = commentsService.findComment(id);
        if (!comment.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment.get());
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity deleteComment(@PathVariable("id") String id) {
        commentsService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/comments/{id}/upvote")
    public ResponseEntity upvoteComment(@PathVariable("id") String id) {
        commentsService.upvote(id);
        return ResponseEntity.ok().build();
    }

}
