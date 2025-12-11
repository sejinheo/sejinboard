package com.example.sejinboard.domain.tag.application.dto.request;

import java.util.List;

public record TagUpdateRequest(
        List<String> tags
) {
}
