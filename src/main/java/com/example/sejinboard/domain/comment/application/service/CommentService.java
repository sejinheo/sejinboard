package com.example.sejinboard.domain.comment.application.service;

import com.example.sejinboard.domain.comment.application.dto.request.CreateCommentRequest;
import com.example.sejinboard.domain.comment.application.dto.request.UpdateCommentRequest;
import com.example.sejinboard.domain.comment.application.dto.response.CommentResponse;
import com.example.sejinboard.domain.comment.domain.Comment;
import com.example.sejinboard.domain.comment.repository.CommentRepository;
import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.article.repository.ArticleRepository;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private static final int PATH_PAD_LENGTH = 10;

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Long articleId, CreateCommentRequest request, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Comment parent = null;
        if (request.parentId() != null) {
            parent = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다"));

            if (!parent.getArticle().getId().equals(articleId)) {
                throw new RuntimeException("부모 댓글과 게시글이 일치하지 않습니다");
            }

            if (parent.isDeleted()) {
                throw new RuntimeException("삭제된 댓글에는 답글을 달 수 없습니다");
            }
        }

        Comment comment = Comment.builder()
                .content(request.content())
                .article(article)
                .author(user)
                .parent(parent)
                .depth(parent == null ? 0 : parent.getDepth() + 1)
                .deleted(false)
                .build();

        Comment savedComment = commentRepository.save(comment);
        String path = buildPath(parent, savedComment.getId());
        savedComment.updatePath(path);
        return CommentResponse.from(savedComment);
    }

    public List<CommentResponse> getCommentsByArticle(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다");
        }

        return commentRepository.findByArticleIdOrderByPathAsc(articleId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long articleId, Long commentId, UpdateCommentRequest request, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        if (comment.isDeleted()) {
            throw new RuntimeException("삭제된 댓글입니다");
        }

        if (!comment.getArticle().getId().equals(articleId)) {
            throw new RuntimeException("댓글과 게시글이 일치하지 않습니다");
        }

        if (!comment.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("댓글 수정 권한이 없습니다");
        }

        comment.update(request.content());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long articleId, Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        if (!comment.getArticle().getId().equals(articleId)) {
            throw new RuntimeException("댓글과 게시글이 일치하지 않습니다");
        }

        if (!comment.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다");
        }

        deleteRecursively(comment);
    }

    private void deleteRecursively(Comment comment) {
        boolean hasChildren = commentRepository.existsByParentId(comment.getId());

        if (hasChildren) {
            if (!comment.isDeleted()) {
                comment.markDeleted();
            }
            return;
        }

        Comment parent = comment.getParent();
        commentRepository.delete(comment);

        if (parent != null && parent.isDeleted() && !commentRepository.existsByParentId(parent.getId())) {
            deleteRecursively(parent);
        }
    }

    private String buildPath(Comment parent, Long commentId) {
        String current = String.format("%0" + PATH_PAD_LENGTH + "d", commentId);
        return parent == null ? current : parent.getPath() + "." + current;
    }
}
