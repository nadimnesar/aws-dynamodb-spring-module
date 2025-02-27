package com.nadimnesar.dynamodbmodule.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private String commentId;
    private String postId;
    private String text;
    private String author;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "PostIdGSI")
    public String getPostId() {
        return postId;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "CommentIdGSI")
    public String getCommentId() {
        return commentId;
    }
}
