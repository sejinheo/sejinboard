package com.example.sejinboard.domain.like.application.service;

import com.example.sejinboard.domain.comment.domain.Comment;
import com.example.sejinboard.domain.comment.repository.CommentRepository;
import com.example.sejinboard.domain.like.application.dto.response.LikeResponse;
import com.example.sejinboard.domain.like.domain.CommentLike;
import com.example.sejinboard.domain.like.domain.ArticleLike;
import com.example.sejinboard.domain.like.repository.CommentLikeRepository;
import com.example.sejinboard.domain.like.repository.ArticleLikeRepository;
import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.article.repository.ArticleRepository;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse toggleArticleLike(Long articleId, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        return articleLikeRepository.findByArticleIdAndUserId(articleId, user.getId())
                .map(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    Long likeCount = articleLikeRepository.countByArticleId(articleId);
                    return LikeResponse.of(false, likeCount);
                })
                .orElseGet(() -> {
                    ArticleLike newLike = ArticleLike.builder()
                            .article(article)
                            .user(user)
                            .build();
                    articleLikeRepository.save(newLike);
                    Long likeCount = articleLikeRepository.countByArticleId(articleId);
                    return LikeResponse.of(true, likeCount);
                });
    }

    @Transactional
    public LikeResponse toggleCommentLike(Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        return commentLikeRepository.findByCommentIdAndUserId(commentId, user.getId())
                .map(commentLike -> {
                    commentLikeRepository.delete(commentLike);
                    Long likeCount = commentLikeRepository.countByCommentId(commentId);
                    return LikeResponse.of(false, likeCount);
                })
                .orElseGet(() -> {
                    CommentLike newLike = CommentLike.builder()
                            .comment(comment)
                            .user(user)
                            .build();
                    commentLikeRepository.save(newLike);
                    Long likeCount = commentLikeRepository.countByCommentId(commentId);
                    return LikeResponse.of(true, likeCount);
                });
    }

    public Long getArticleLikeCount(Long articleId) {
        return articleLikeRepository.countByArticleId(articleId);
    }

    public Long getCommentLikeCount(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    public boolean isArticleLikedByUser(Long articleId, Long userId) {
        return articleLikeRepository.existsByArticleIdAndUserId(articleId, userId);
    }

    public boolean isCommentLikedByUser(Long commentId, Long userId) {
        return commentLikeRepository.existsByCommentIdAndUserId(commentId, userId);
    }
}
