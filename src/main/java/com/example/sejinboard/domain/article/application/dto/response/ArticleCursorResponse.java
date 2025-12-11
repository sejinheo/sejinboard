package com.example.sejinboard.domain.article.application.dto.response;

import java.util.List;

public record ArticleCursorResponse(
        List<ArticleListResponse> articles,
        Long nextCursor,
        boolean hasNext
) {
    public static ArticleCursorResponse of(List<ArticleListResponse> articles, Long nextCursor, boolean hasNext) {
        return new ArticleCursorResponse(articles, nextCursor, hasNext);
    }
}
