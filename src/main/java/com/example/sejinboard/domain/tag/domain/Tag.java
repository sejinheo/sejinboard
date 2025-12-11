package com.example.sejinboard.domain.tag.domain;

import com.example.sejinboard.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
