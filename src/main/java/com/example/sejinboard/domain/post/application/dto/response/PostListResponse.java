package com.example.sejinboard.domain.post.application.dto.response;

import com.example.sejinboard.domain.post.domain.Post;

import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        String title,
        String authorName,
        Integer viewCount,
        LocalDateTime createdAt
) {
    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getName(),
                post.getViewCount(),
                post.getCreatedAt()
        );
    }
}
