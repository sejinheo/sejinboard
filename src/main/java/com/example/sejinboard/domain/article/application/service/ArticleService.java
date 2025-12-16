package com.example.sejinboard.domain.article.application.service;

import com.example.sejinboard.domain.article.application.dto.request.CreateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.request.UpdateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.response.ArticleCursorResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleListResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleLikeRankResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleOwnershipResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleResponse;
import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.article.repository.ArticleRepository;
import com.example.sejinboard.domain.tag.repository.ArticleTagRepository;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleTagRepository articleTagRepository;

    @Transactional
    public ArticleResponse createArticle(CreateArticleRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Article article = Article.builder()
                .title(request.title())
                .content(request.content())
                .thumbnailUrl(request.thumbnailUrl())
                .author(user)
                .build();

        Article savedArticle = articleRepository.save(article);
        return ArticleResponse.from(savedArticle);
    }

    @Transactional
    public ArticleResponse getArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        article.increaseViewCount();
        return ArticleResponse.from(article);
    }

    public ArticleCursorResponse getArticles(Long lastId, int size) {
        int pageSize = Math.max(size, 1);
        PageRequest pageRequest = PageRequest.of(0, pageSize + 1, Sort.by(Sort.Direction.DESC, "id"));

        List<Article> articles = (lastId == null)
                ? articleRepository.findAllByOrderByIdDesc(pageRequest)
                : articleRepository.findByIdLessThanOrderByIdDesc(lastId, pageRequest);

        boolean hasNext = articles.size() > pageSize;
        if (hasNext) {
            articles = articles.subList(0, pageSize);
        }

        Long nextCursor = hasNext && !articles.isEmpty()
                ? articles.get(articles.size() - 1).getId()
                : null;

        List<ArticleListResponse> responses = articles.stream()
                .map(ArticleListResponse::from)
                .toList();

        return ArticleCursorResponse.of(responses, nextCursor, hasNext);
    }

    public ArticleCursorResponse getMyArticles(String userEmail, Long lastId, int size) {
        int pageSize = Math.max(size, 1);
        PageRequest pageRequest = PageRequest.of(0, pageSize + 1, Sort.by(Sort.Direction.DESC, "id"));

        List<Article> articles = articleRepository.findMyArticles(userEmail, lastId, pageRequest);

        boolean hasNext = articles.size() > pageSize;
        if (hasNext) {
            articles = articles.subList(0, pageSize);
        }

        Long nextCursor = hasNext && !articles.isEmpty()
                ? articles.get(articles.size() - 1).getId()
                : null;

        List<ArticleListResponse> responses = articles.stream()
                .map(ArticleListResponse::from)
                .toList();

        return ArticleCursorResponse.of(responses, nextCursor, hasNext);
    }

    public ArticleCursorResponse getLikedArticles(String userEmail, Long lastId, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        int pageSize = Math.max(size, 1);
        PageRequest pageRequest = PageRequest.of(0, pageSize + 1, Sort.by(Sort.Direction.DESC, "id"));

        List<Article> articles = articleRepository.findLikedArticles(user.getId(), lastId, pageRequest);

        boolean hasNext = articles.size() > pageSize;
        if (hasNext) {
            articles = articles.subList(0, pageSize);
        }

        Long nextCursor = hasNext && !articles.isEmpty()
                ? articles.get(articles.size() - 1).getId()
                : null;

        List<ArticleListResponse> responses = articles.stream()
                .map(ArticleListResponse::from)
                .toList();

        return ArticleCursorResponse.of(responses, nextCursor, hasNext);
    }

    public ArticleCursorResponse searchArticles(String keyword, Long lastId, int size) {
        if (keyword == null || keyword.isBlank()) {
            throw new RuntimeException("검색어를 입력해주세요");
        }

        int pageSize = Math.max(size, 1);
        PageRequest pageRequest = PageRequest.of(0, pageSize + 1, Sort.by(Sort.Direction.DESC, "id"));

        List<Article> articles = articleRepository.searchByKeyword(keyword, lastId, pageRequest);

        boolean hasNext = articles.size() > pageSize;
        if (hasNext) {
            articles = articles.subList(0, pageSize);
        }

        Long nextCursor = hasNext && !articles.isEmpty()
                ? articles.get(articles.size() - 1).getId()
                : null;

        List<ArticleListResponse> responses = articles.stream()
                .map(ArticleListResponse::from)
                .toList();

        return ArticleCursorResponse.of(responses, nextCursor, hasNext);
    }

    public List<ArticleLikeRankResponse> getArticlesByLikeCount(int size) {
        int pageSize = Math.max(size, 1);
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        return articleRepository.findTopByLikeCount(pageRequest);
    }

    public List<ArticleListResponse> getArticlesByViewCount(int page, int size) {
        int pageSize = Math.max(size, 1);
        int pageIndex = Math.max(page, 0);
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "viewCount").and(Sort.by(Sort.Direction.DESC, "id")));
        return articleRepository.findByOrderByViewCountDescIdDesc(pageRequest).stream()
                .map(ArticleListResponse::from)
                .toList();
    }

    @Transactional
    public ArticleResponse updateArticle(Long articleId, UpdateArticleRequest request, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!article.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다");
        }

        article.update(request.title(), request.content(), request.thumbnailUrl());
        return ArticleResponse.from(article);
    }

    @Transactional
    public void deleteArticle(Long articleId, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!article.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다");
        }

        articleTagRepository.deleteByArticleId(articleId);
        articleRepository.delete(article);
    }

    public ArticleOwnershipResponse checkArticleOwnership(Long articleId, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        boolean isOwner = article.getAuthor().getEmail().equals(userEmail);
        return ArticleOwnershipResponse.of(isOwner);
    }
}
