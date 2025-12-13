package com.example.sejinboard.domain.article.repository;

import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.article.application.dto.response.ArticleLikeRankResponse;
import com.example.sejinboard.domain.like.domain.ArticleLike;
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
            where a.author.email = :email
              and (:lastId is null or a.id < :lastId)
            order by a.id desc
            """)
    List<Article> findMyArticles(
            @Param("email") String email,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("""
            select a from Article a
            join ArticleLike al on al.article = a
            where al.user.id = :userId
              and (:lastId is null or a.id < :lastId)
            group by a.id, a.title, a.content, a.thumbnailUrl, a.author.name, a.viewCount, a.createdAt, a.updatedAt
            order by a.id desc
            """)
    List<Article> findLikedArticles(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

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

    @Query("""
            select new com.example.sejinboard.domain.article.application.dto.response.ArticleLikeRankResponse(
                a.id,
                a.title,
                a.content,
                a.thumbnailUrl,
                a.author.name,
                a.viewCount,
                count(al.id),
                a.createdAt,
                a.updatedAt
            )
            from Article a
            left join ArticleLike al on al.article = a
            group by a.id, a.title, a.content, a.thumbnailUrl, a.author.name, a.viewCount, a.createdAt, a.updatedAt
            order by count(al.id) desc, a.id desc
            """)
    List<ArticleLikeRankResponse> findTopByLikeCount(Pageable pageable);
}
