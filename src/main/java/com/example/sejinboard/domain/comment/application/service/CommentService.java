package com.example.sejinboard.domain.comment.application.service;

import com.example.sejinboard.domain.comment.application.dto.request.CreateCommentRequest;
import com.example.sejinboard.domain.comment.application.dto.request.UpdateCommentRequest;
import com.example.sejinboard.domain.comment.application.dto.response.CommentResponse;
import com.example.sejinboard.domain.comment.domain.Comment;
import com.example.sejinboard.domain.comment.repository.CommentRepository;
import com.example.sejinboard.domain.post.domain.Post;
import com.example.sejinboard.domain.post.repository.PostRepository;
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

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Long postId, CreateCommentRequest request, String userEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Comment comment = Comment.builder()
                .content(request.content())
                .post(post)
                .author(user)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다");
        }

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        if (!comment.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("댓글 수정 권한이 없습니다");
        }

        comment.update(request.content());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다"));

        if (!comment.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다");
        }

        commentRepository.delete(comment);
    }
}