package com.example.sejinboard.domain.comment.application.dto.response;

import com.example.sejinboard.domain.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String authorName,
        Long postId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getName(),
                comment.getPost().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
