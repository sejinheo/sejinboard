package com.example.sejinboard.domain.like.application.dto.response;

public record LikeResponse(
        boolean liked,
        Long likeCount
) {
    public static LikeResponse of(boolean liked, Long likeCount) {
        return new LikeResponse(liked, likeCount);
    }
}