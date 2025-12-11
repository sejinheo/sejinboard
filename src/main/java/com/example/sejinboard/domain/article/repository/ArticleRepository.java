package com.example.sejinboard.domain.article.repository;

import com.example.sejinboard.domain.article.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByIdDesc(Pageable pageable);
    List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);
}
