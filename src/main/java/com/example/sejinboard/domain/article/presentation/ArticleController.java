package com.example.sejinboard.domain.article.presentation;

import com.example.sejinboard.domain.article.application.dto.request.CreateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.request.UpdateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.response.ArticleCursorResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleLikeRankResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleResponse;
import com.example.sejinboard.domain.article.application.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(
            @Valid @RequestBody CreateArticleRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ArticleResponse response = articleService.createArticle(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long articleId) {
        ArticleResponse response = articleService.getArticle(articleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "내 게시글 목록 조회", description = "현재 로그인한 사용자의 게시글을 최신순으로 조회합니다. cursor 기반(lastId) 페이지네이션을 사용합니다.")
    public ResponseEntity<ArticleCursorResponse> getMyArticles(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ArticleCursorResponse response = articleService.getMyArticles(userDetails.getUsername(), lastId, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ArticleCursorResponse> getAllArticles(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size
    ) {
        ArticleCursorResponse response = articleService.getArticles(lastId, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-liked")
    @Operation(summary = "좋아요 순 게시글 조회", description = "좋아요 수 내림차순으로 상위 게시글을 반환합니다.")
    public ResponseEntity<List<ArticleLikeRankResponse>> getArticlesByLikeCount(
            @RequestParam(defaultValue = "20") int size
    ) {
        List<ArticleLikeRankResponse> response = articleService.getArticlesByLikeCount(size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/liked")
    @Operation(summary = "내가 좋아요한 게시글 조회", description = "로그인 사용자가 좋아요한 게시글을 최신순으로 조회합니다. cursor 기반(lastId) 페이지네이션 사용.")
    public ResponseEntity<ArticleCursorResponse> getLikedArticles(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ArticleCursorResponse response = articleService.getLikedArticles(userDetails.getUsername(), lastId, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ArticleCursorResponse> searchArticles(
            @RequestParam String query,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size
    ) {
        ArticleCursorResponse response = articleService.searchArticles(query, lastId, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long articleId,
            @Valid @RequestBody UpdateArticleRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ArticleResponse response = articleService.updateArticle(articleId, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        articleService.deleteArticle(articleId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
