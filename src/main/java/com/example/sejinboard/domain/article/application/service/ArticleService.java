package com.example.sejinboard.domain.article.application.service;

import com.example.sejinboard.domain.article.application.dto.request.CreateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.request.UpdateArticleRequest;
import com.example.sejinboard.domain.article.application.dto.response.ArticleListResponse;
import com.example.sejinboard.domain.article.application.dto.response.ArticleResponse;
import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.article.repository.ArticleRepository;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public ArticleResponse createArticle(CreateArticleRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Article article = Article.builder()
                .title(request.title())
                .content(request.content())
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

    public Page<ArticleListResponse> getAllArticles(Pageable pageable) {
        return articleRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(ArticleListResponse::from);
    }

    @Transactional
    public ArticleResponse updateArticle(Long articleId, UpdateArticleRequest request, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!article.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다");
        }

        article.update(request.title(), request.content());
        return ArticleResponse.from(article);
    }

    @Transactional
    public void deleteArticle(Long articleId, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!article.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다");
        }

        articleRepository.delete(article);
    }
}
