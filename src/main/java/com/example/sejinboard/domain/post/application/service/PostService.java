package com.example.sejinboard.domain.post.application.service;

import com.example.sejinboard.domain.post.application.dto.request.CreatePostRequest;
import com.example.sejinboard.domain.post.application.dto.request.UpdatePostRequest;
import com.example.sejinboard.domain.post.application.dto.response.PostListResponse;
import com.example.sejinboard.domain.post.application.dto.response.PostResponse;
import com.example.sejinboard.domain.post.domain.Post;
import com.example.sejinboard.domain.post.repository.PostRepository;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse createPost(CreatePostRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .author(user)
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    @Transactional
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        post.increaseViewCount();
        return PostResponse.from(post);
    }

    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(PostListResponse::from);
    }

    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request, String userEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!post.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다");
        }

        post.update(request.title(), request.content());
        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId, String userEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!post.getAuthor().getEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다");
        }

        postRepository.delete(post);
    }
}