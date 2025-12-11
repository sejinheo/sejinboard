package com.example.sejinboard.domain.tag.application.service;

import com.example.sejinboard.domain.article.domain.Article;
import com.example.sejinboard.domain.article.repository.ArticleRepository;
import com.example.sejinboard.domain.tag.application.dto.request.TagUpdateRequest;
import com.example.sejinboard.domain.tag.application.dto.response.TagResponse;
import com.example.sejinboard.domain.tag.domain.ArticleTag;
import com.example.sejinboard.domain.tag.domain.Tag;
import com.example.sejinboard.domain.tag.repository.ArticleTagRepository;
import com.example.sejinboard.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    @Transactional
    public List<TagResponse> updateArticleTags(Long articleId, TagUpdateRequest request, String userEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!article.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다");
        }

        Set<String> normalizedNames = Optional.ofNullable(request.tags())
                .orElse(List.of())
                .stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Tag> existingTags = normalizedNames.isEmpty()
                ? Collections.emptyList()
                : tagRepository.findByNameIn(normalizedNames);
        Map<String, Tag> existingTagMap = existingTags.stream()
                .collect(Collectors.toMap(Tag::getName, tag -> tag));

        List<Tag> tagsToUse = new ArrayList<>(existingTags);
        for (String name : normalizedNames) {
            if (!existingTagMap.containsKey(name)) {
                Tag newTag = tagRepository.save(Tag.builder().name(name).build());
                tagsToUse.add(newTag);
            }
        }

        List<ArticleTag> currentArticleTags = articleTagRepository.findByArticleIdWithTag(articleId);
        Set<Long> incomingTagIds = tagsToUse.stream().map(Tag::getId).collect(Collectors.toSet());

        // Remove tags not in request
        List<ArticleTag> toDelete = currentArticleTags.stream()
                .filter(at -> !incomingTagIds.contains(at.getTag().getId()))
                .toList();
        if (!toDelete.isEmpty()) {
            articleTagRepository.deleteAllInBatch(toDelete);
        }

        Set<Long> existingTagIds = currentArticleTags.stream()
                .map(at -> at.getTag().getId())
                .collect(Collectors.toSet());

        // Add new links
        List<ArticleTag> toAdd = tagsToUse.stream()
                .filter(tag -> !existingTagIds.contains(tag.getId()))
                .map(tag -> ArticleTag.builder().article(article).tag(tag).build())
                .toList();
        if (!toAdd.isEmpty()) {
            articleTagRepository.saveAll(toAdd);
        }

        return tagsToUse.stream()
                .sorted(Comparator.comparing(Tag::getName))
                .map(TagResponse::from)
                .toList();
    }

    public List<TagResponse> getArticleTags(Long articleId) {
        List<Tag> tags = articleTagRepository.findTagsByArticleId(articleId);
        return tags.stream()
                .sorted(Comparator.comparing(Tag::getName))
                .map(TagResponse::from)
                .toList();
    }

    public List<TagResponse> searchTags(String keyword, int size) {
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        int pageSize = Math.max(size, 1);
        Pageable pageable = PageRequest.of(0, pageSize);
        return tagRepository.findByNameContainingIgnoreCaseOrderByNameAsc(keyword.trim(), pageable)
                .stream()
                .map(TagResponse::from)
                .toList();
    }
}
