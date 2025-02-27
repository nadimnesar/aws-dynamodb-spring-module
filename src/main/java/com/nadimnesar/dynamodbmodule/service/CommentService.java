package com.nadimnesar.dynamodbmodule.service;

import com.nadimnesar.dynamodbmodule.entity.Comment;
import com.nadimnesar.dynamodbmodule.repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public ResponseEntity<?> getByCommentId(String commentId) {
        Optional<Comment> comment = commentRepository.findByCommentId(commentId);
        if (comment.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(comment.get());
        }
    }

    public ResponseEntity<?> getAllComments() {
        List<Comment> comments = commentRepository.findAllComments();
        if (comments.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(comments);
        }
    }

    public ResponseEntity<?> getCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        if (comments.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(comments);
        }
    }

    public ResponseEntity<?> deleteByCommentId(String commentId) {
        Optional<Comment> comment = commentRepository.findByCommentId(commentId);
        if (comment.isPresent()) {
            comment.get().setDeleted(true);
            commentRepository.save(comment.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> updateComment(String commentId, Comment comment) {
        if (commentRepository.updateComment(commentId, comment)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
