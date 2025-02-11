package com.nadimnesar.dynamodbmodule.repository;

import com.nadimnesar.dynamodbmodule.entity.Comment;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CommentRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<Comment> commentTable;

    public CommentRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @PostConstruct
    private void init() {
        this.commentTable = enhancedClient.table("Comment", TableSchema.fromBean(Comment.class));
    }

    public Comment save(Comment comment) {
        if (comment.getId() == null || comment.getId().isEmpty()) {
            comment.setId(UUID.randomUUID().toString());
        }

        if (comment.getDate() == null) {
            comment.setDate(new Date().toString());
        }

        commentTable.putItem(comment);
        return comment;
    }

    public Optional<Comment> findByIdAndDate(String id, String date) {
        return Optional.ofNullable(commentTable.getItem(
                Key.builder()
                        .partitionValue(id)
                        .sortValue(date)
                        .build()
        ));
    }

    public List<Comment> findById(String id) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(id).build());

        return commentTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Comment> findByIdAndDateBetween(String id, String startDate, String endDate) {
        QueryConditional queryConditional = QueryConditional
                .sortBetween(
                        Key.builder().partitionValue(id).sortValue(startDate).build(),
                        Key.builder().partitionValue(id).sortValue(endDate).build()
                );

        return commentTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public void deleteByIdAndDate(String id, String date) {
        commentTable.deleteItem(
                Key.builder()
                        .partitionValue(id)
                        .sortValue(date)
                        .build()
        );
    }

    public List<Comment> findAll() {
        return commentTable.scan()
                .items()
                .stream().toList();
    }
}
