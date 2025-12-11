package com.example.sejinboard.domain.tag.application.dto.response;

import com.example.sejinboard.domain.tag.domain.Tag;

public record TagResponse(
        Long id,
        String name
) {
    public static TagResponse from(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
