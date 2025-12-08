package com.example.sejinboard.domain.like.application.service;

import com.example.sejinboard.domain.comment.domain.Comment;
import com.example.sejinboard.domain.comment.repository.CommentRepository;
import com.example.sejinboard.domain.like.application.dto.response.LikeResponse;
import com.example.sejinboard.domain.like.domain.CommentLike;
import com.example.sejinboard.domain.like.domain.PostLike;
import com.example.sejinboard.domain.like.repository.CommentLikeRepository;
import com.example.sejinboard.domain.like.repository.PostLikeRepository;
import com.example.sejinboard.domain.post.domain.Post;
import com.example.sejinboard.domain.post.repository.PostRepository;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse togglePostLike(Long postId, String userEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        return postLikeRepository.findByPostIdAndUserId(postId, user.getId())
                .map(postLike -> {
                    postLikeRepository.delete(postLike);
                    Long likeCount = postLikeRepository.countByPostId(postId);
                    return LikeResponse.of(false, likeCount);
                })
                .orElseGet(() -> {
                    PostLike newLike = PostLike.builder()
                            .post(post)
                            .user(user)
                            .build();
                    postLikeRepository.save(newLike);
                    Long likeCount = postLikeRepository.countByPostId(postId);
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

    public Long getPostLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    public Long getCommentLikeCount(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    public boolean isPostLikedByUser(Long postId, Long userId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public boolean isCommentLikedByUser(Long commentId, Long userId) {
        return commentLikeRepository.existsByCommentIdAndUserId(commentId, userId);
    }
}