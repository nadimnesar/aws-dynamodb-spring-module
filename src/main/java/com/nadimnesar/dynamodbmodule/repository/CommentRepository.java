package com.nadimnesar.dynamodbmodule.repository;

import com.nadimnesar.dynamodbmodule.entity.Comment;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CommentRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<Comment> commentTable;
    private DynamoDbIndex<Comment> commentIdIndex;
    private DynamoDbIndex<Comment> postIdIndex;

    public CommentRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @PostConstruct
    private void init() {
        this.commentTable = enhancedClient.table("Comment", TableSchema.fromBean(Comment.class));
        this.commentIdIndex = commentTable.index("CommentIdGSI");
        this.postIdIndex = commentTable.index("PostIdGSI");
    }

    public void save(Comment comment) {
        if (comment.getCommentId() == null || comment.getCommentId().isEmpty()) {
            comment.setCommentId(UUID.randomUUID().toString());
        }
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(comment.getCreatedAt());
        commentTable.putItem(comment);
    }

    public List<Comment> findAllComments() {
        return commentTable.scan()
                .items()
                .stream()
                .filter(comment -> !comment.isDeleted())
                .toList();
    }

    public Optional<Comment> findByCommentId(String commentId) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(commentId)
                .build());

        return commentIdIndex.query(r -> r.queryConditional(queryConditional))
                .stream().findFirst().flatMap(commentPage -> commentPage.items().stream().findFirst());
    }

    public List<Comment> findCommentsByPostId(String postId) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(postId)
                .build());

        return postIdIndex.query(r -> r.queryConditional(queryConditional))
                .stream()
                .flatMap(commentPage -> commentPage.items().stream())
                .filter(comment -> !comment.isDeleted())
                .toList();
    }

    public boolean updateComment(String commentId, Comment comment) {
        Optional<Comment> commentOptional = findByCommentId(commentId);
        if (commentOptional.isPresent()) {
            Comment existingComment = commentOptional.get();

            if (comment.getText() != null) {
                existingComment.setText(comment.getText());
            }
            if (comment.getAuthor() != null) {
                existingComment.setAuthor(comment.getAuthor());
            }

            existingComment.setUpdatedAt(LocalDateTime.now());
            commentTable.updateItem(existingComment);

            return true;
        } else {
            return false;
        }
    }
}
