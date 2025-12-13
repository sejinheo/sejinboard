package com.example.sejinboard.domain.article.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateArticleRequest(
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @NotBlank(message = "내용은 필수입니다")
        String content,

        @Size(max = 500, message = "썸네일 URL은 500자를 초과할 수 없습니다")
        String thumbnailUrl
) {
}
