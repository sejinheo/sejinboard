package com.example.sejinboard.domain.comment.application.dto.response;

import com.example.sejinboard.domain.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String authorName,
        Long articleId,
        Long parentId,
        String path,
        int depth,
        boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.isDeleted() ? null : comment.getContent(),
                comment.getAuthor().getName(),
                comment.getArticle().getId(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getPath(),
                comment.getDepth(),
                comment.isDeleted(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
