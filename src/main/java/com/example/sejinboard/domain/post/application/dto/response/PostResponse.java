package com.example.sejinboard.domain.post.application.dto.response;

import com.example.sejinboard.domain.post.domain.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        String authorName,
        Integer viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getName(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
