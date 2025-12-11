package com.example.sejinboard.domain.tag.presentation;

import com.example.sejinboard.domain.tag.application.dto.request.TagUpdateRequest;
import com.example.sejinboard.domain.tag.application.dto.response.TagResponse;
import com.example.sejinboard.domain.tag.application.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PutMapping("/api/articles/{articleId}/tags")
    public ResponseEntity<List<TagResponse>> updateArticleTags(
            @PathVariable Long articleId,
            @RequestBody TagUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<TagResponse> responses = tagService.updateArticleTags(articleId, request, userDetails.getUsername());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/articles/{articleId}/tags")
    public ResponseEntity<List<TagResponse>> getArticleTags(@PathVariable Long articleId) {
        List<TagResponse> responses = tagService.getArticleTags(articleId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/tags")
    public ResponseEntity<List<TagResponse>> searchTags(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<TagResponse> responses = tagService.searchTags(query, size);
        return ResponseEntity.ok(responses);
    }
}
