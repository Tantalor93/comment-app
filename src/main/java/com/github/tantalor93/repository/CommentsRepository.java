package com.github.tantalor93.repository;

import com.github.tantalor93.model.Comment;
import org.springframework.data.repository.CrudRepository;


public interface CommentsRepository extends CrudRepository<Comment, String> {
}
