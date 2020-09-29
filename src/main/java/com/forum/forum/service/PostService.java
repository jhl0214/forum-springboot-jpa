package com.forum.forum.service;

import com.forum.forum.domain.Post;
import com.forum.forum.dto.PostDto;
import com.forum.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    /**
     * Add post
     */
    @Transactional
    public Long addPost(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    /**
     * Update post
     */
    @Transactional
    public void updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.find(postId);
        post.updatePost(postDto);
    }

    /**
     * Delete post
     */
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deletePost(postId);
    }

    /**
     * Get posts
     */
    public Post findPostById(Long postId) {
        return postRepository.find(postId);
    }

    public List<Post> findPostsByUserName(String username) {
        return postRepository.findByUserName(username);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }



}
