package com.example.sejinboard.domain.like.repository;

import com.example.sejinboard.domain.like.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    Long countByCommentId(Long commentId);
}