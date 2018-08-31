package com.github.tantalor93.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("comments")
public class Comment {

    @Id
    private String id;
    private int upvotes;
    private String comment;
    private String author;
    private long timestamp;

    public Comment(String id, int upvotes, String comment, String author, long timestamp) {
        this.id = id;
        this.upvotes = upvotes;
        this.comment = comment;
        this.author = author;
        this.timestamp = timestamp;
    }


    protected Comment() {
    }

    public String getId() {
        return id;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static Comment buildWithGeneratedId(int upvotes, String comment, String author) {
        final long now = System.currentTimeMillis();
        return new Comment(createId(author, now), upvotes, comment, author, now);
    }

    private static String createId(String author, long now) {
        return now + "t" + author.hashCode();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", upvotes=" + upvotes +
                ", comment='" + comment + '\'' +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
