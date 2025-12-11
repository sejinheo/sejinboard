package com.example.sejinboard.domain.article.presentation;

import com.example.sejinboard.domain.article.application.dto.request.CreateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.request.UpdateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.response.ArticleCursorResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleResponse;
import com.example.sejinboard.domain.article.application.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
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

    @GetMapping
    public ResponseEntity<ArticleCursorResponse> getAllArticles(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size
    ) {
        ArticleCursorResponse response = articleService.getArticles(lastId, size);
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
