package com.example.sejinboard.domain.comment.domain;

import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @Column(nullable = false)
    private String path = "";

    @Builder.Default
    @Column(nullable = false)
    private int depth = 0;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    public void update(String content) {
        this.content = content;
    }

    public void updatePath(String path) {
        this.path = path;
    }

    public void markDeleted() {
        this.deleted = true;
    }
}
