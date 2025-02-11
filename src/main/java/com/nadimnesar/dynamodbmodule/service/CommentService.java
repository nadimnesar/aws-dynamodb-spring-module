package com.nadimnesar.dynamodbmodule.service;

import com.nadimnesar.dynamodbmodule.entity.Comment;
import com.nadimnesar.dynamodbmodule.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentsRepository;

    public CommentService(CommentRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentsRepository.save(comment);
    }

    public Optional<Comment> getCommentByIdAndDate(String id, String date) {
        return commentsRepository.findByIdAndDate(id, date);
    }

    public List<Comment> getCommentById(String id) {
        return commentsRepository.findById(id);
    }

    public List<Comment> getCommentsByDateRange(String postId, String startDate, String endDate) {
        return commentsRepository.findByIdAndDateBetween(postId, startDate, endDate);
    }

    public void deleteComment(String id, String date) {
        commentsRepository.deleteByIdAndDate(id, date);
    }

    public List<Comment> getAllComments() {
        return commentsRepository.findAll();
    }
}
