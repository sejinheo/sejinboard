package com.example.sejinboard.domain.article.repository;

import com.example.sejinboard.domain.article.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByIdDesc(Pageable pageable);
    List<Article> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

    @Query("""
            select a from Article a
            where (lower(a.title) like lower(concat('%', :keyword, '%'))
               or lower(a.content) like lower(concat('%', :keyword, '%')))
              and (:lastId is null or a.id < :lastId)
            order by a.id desc
            """)
    List<Article> searchByKeyword(
            @Param("keyword") String keyword,
            @Param("lastId") Long lastId,
            Pageable pageable
    );
}
