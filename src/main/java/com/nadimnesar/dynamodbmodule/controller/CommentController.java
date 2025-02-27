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

    @GetMapping("/getByCommentId")
    public ResponseEntity<?> getCommentById(@RequestParam String commentId) {
        return new ResponseEntity<>(commentService.getByCommentId(commentId), HttpStatus.OK);
    }

    @GetMapping("/getAllComments")
    public ResponseEntity<?> getAllComments() {
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }

    @GetMapping("/getCommentsByPostId")
    public ResponseEntity<?> getCommentsByPostId(@RequestParam String postId) {
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteByCommentId")
    public ResponseEntity<?> deleteByCommentId(@RequestParam String commentId) {
        return new ResponseEntity<>(commentService.deleteByCommentId(commentId), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateComment(@RequestParam String commentId, @RequestBody Comment comment) {
        return new ResponseEntity<>(commentService.updateComment(commentId, comment), HttpStatus.OK);
    }
}
