package com.example.sejinboard.domain.like.presentation;

import com.example.sejinboard.domain.like.application.dto.response.LikeResponse;
import com.example.sejinboard.domain.like.application.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/api/articles/{articleId}/like")
    public ResponseEntity<LikeResponse> toggleArticleLike(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        LikeResponse response = likeService.toggleArticleLike(articleId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/comments/{commentId}/like")
    public ResponseEntity<LikeResponse> toggleCommentLike(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        LikeResponse response = likeService.toggleCommentLike(commentId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/articles/{articleId}/like/count")
    public ResponseEntity<Long> getArticleLikeCount(@PathVariable Long articleId) {
        Long count = likeService.getArticleLikeCount(articleId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/api/comments/{commentId}/like/count")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        Long count = likeService.getCommentLikeCount(commentId);
        return ResponseEntity.ok(count);
    }
}
