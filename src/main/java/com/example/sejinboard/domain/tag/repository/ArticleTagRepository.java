package com.example.sejinboard.domain.tag.repository;

import com.example.sejinboard.domain.tag.domain.ArticleTag;
import com.example.sejinboard.domain.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {

    @Query("select at from ArticleTag at join fetch at.tag where at.article.id = :articleId")
    List<ArticleTag> findByArticleIdWithTag(@Param("articleId") Long articleId);

    @Query("select at.tag from ArticleTag at where at.article.id = :articleId")
    List<Tag> findTagsByArticleId(@Param("articleId") Long articleId);

    void deleteByArticleId(Long articleId);
}
