package com.example.sejinboard.domain.tag.domain;

import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "article_tags", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"article_id", "tag_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTag extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }
}
