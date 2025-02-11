package com.nadimnesar.dynamodbmodule.controller;

import com.nadimnesar.dynamodbmodule.entity.Comment;
import com.nadimnesar.dynamodbmodule.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        try {
            commentService.saveComment(comment);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getCommentById(@RequestParam String id) {
        return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllComment() {
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }
}
