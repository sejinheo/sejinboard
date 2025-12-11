package com.example.sejinboard.domain.like.domain;

import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "article_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"article_id", "user_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public ArticleLike(Article article, User user) {
        this.article = article;
        this.user = user;
    }
}
