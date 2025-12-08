package com.example.sejinboard.domain.like.domain;

import com.example.sejinboard.domain.post.domain.Post;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "post_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}