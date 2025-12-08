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

    @PostMapping("/api/posts/{postId}/like")
    public ResponseEntity<LikeResponse> togglePostLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        LikeResponse response = likeService.togglePostLike(postId, userDetails.getUsername());
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

    @GetMapping("/api/posts/{postId}/like/count")
    public ResponseEntity<Long> getPostLikeCount(@PathVariable Long postId) {
        Long count = likeService.getPostLikeCount(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/api/comments/{commentId}/like/count")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        Long count = likeService.getCommentLikeCount(commentId);
        return ResponseEntity.ok(count);
    }
}