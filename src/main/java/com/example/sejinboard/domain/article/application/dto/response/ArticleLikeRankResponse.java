package com.example.sejinboard.domain.article.application.dto.response;

import java.time.LocalDateTime;

public record ArticleLikeRankResponse(
        Long id,
        String title,
        String content,
        String thumbnailUrl,
        String authorName,
        Integer viewCount,
        Long likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
