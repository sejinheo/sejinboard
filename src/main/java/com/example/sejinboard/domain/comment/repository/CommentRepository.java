package com.example.sejinboard.domain.comment.repository;

import com.example.sejinboard.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdOrderByPathAsc(Long articleId);
    boolean existsByParentId(Long parentId);
}
