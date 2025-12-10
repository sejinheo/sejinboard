package com.example.sejinboard.domain.comment.domain;

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
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Builder
    public Comment(String content, Article article, User author) {
        this.content = content;
        this.article = article;
        this.author = author;
    }

    public void update(String content) {
        this.content = content;
    }
}
