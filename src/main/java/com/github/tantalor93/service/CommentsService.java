package com.github.tantalor93.service;

import com.github.tantalor93.model.Comment;
import com.github.tantalor93.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class CommentsService {

    private static final String TOP_UPVOTED_COMMENTS_SET = "top-upvoted-comments";
    private static final String COMMENTS_ORDER = "order-comments";

    private final CommentsRepository commentsRepository;
    private final HashOperations<String, Object, Object> hashOps;
    private final ZSetOperations<String, String> zSetOps;
    private final ListOperations<String, String> listOps;

    @Autowired
    public CommentsService(
            RedisTemplate<String, Comment> redisTemplate,
            CommentsRepository commentsRepository,
            RedisTemplate<String, String> redisTemplateForSet
    ) {
        this.commentsRepository = commentsRepository;
        hashOps = redisTemplate.opsForHash();
        zSetOps = redisTemplateForSet.opsForZSet();
        listOps = redisTemplateForSet.opsForList();
    }

    public void deleteComment(final String id) {
        zSetOps.remove(TOP_UPVOTED_COMMENTS_SET, id);
        listOps.remove(COMMENTS_ORDER, 0, id);
        commentsRepository.deleteById(id);
    }

    public Comment createComment(final String author, final String comment) {
        final Comment commentToCreate = Comment.buildWithGeneratedId(0, comment, author);
        zSetOps.add(TOP_UPVOTED_COMMENTS_SET, commentToCreate.getId(), 0.0);
        listOps.leftPush(COMMENTS_ORDER, commentToCreate.getId());
        return commentsRepository.save(commentToCreate);
    }

    public void upvote(final String id) {
        zSetOps.incrementScore(TOP_UPVOTED_COMMENTS_SET, id, 1.0);
        hashOps.increment("comments:" + id, "upvotes", 1);
    }

    public List<Comment> getTopNUpvotedComments(final int n) {
        final Set<String> ids = zSetOps.reverseRange(TOP_UPVOTED_COMMENTS_SET, 0, n - 1);
        return ids.stream().map(id -> commentsRepository.findById(id).get()).collect(Collectors.toList());
    }

    public Iterable<Comment> getAllComments() {
        List<String> ids = listOps.range(COMMENTS_ORDER, 0, -1);
        ids.stream().map(id -> commentsRepository.findById(id).get()).collect(Collectors.toList());
        return ids.stream().map(id -> commentsRepository.findById(id).get()).collect(Collectors.toList());
    }

}
