package com.example.sejinboard.domain.article.application.dto.response;

public record ArticleOwnershipResponse(
        boolean isOwner
) {
    public static ArticleOwnershipResponse of(boolean isOwner) {
        return new ArticleOwnershipResponse(isOwner);
    }
}