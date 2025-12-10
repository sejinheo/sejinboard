package com.example.sejinboard.domain.article.application.dto.response;

import com.example.sejinboard.domain.article.domain.Article;

import java.time.LocalDateTime;

public record ArticleListResponse(
        Long id,
        String title,
        String authorName,
        Integer viewCount,
        LocalDateTime createdAt
) {
    public static ArticleListResponse from(Article article) {
        return new ArticleListResponse(
                article.getId(),
                article.getTitle(),
                article.getAuthor().getName(),
                article.getViewCount(),
                article.getCreatedAt()
        );
    }
}
