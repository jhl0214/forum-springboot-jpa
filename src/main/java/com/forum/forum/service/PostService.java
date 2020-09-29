package com.forum.forum.service;

import com.forum.forum.domain.Post;
import com.forum.forum.dto.PostDto;
import com.forum.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Post post = postRepository.findById(postId).get();
        post.updatePost(postDto);
    }

    /**
     * Delete post
     */
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
    }

    /**
     * Get posts
     */
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).get();
    }

    public List<Post> findPostsByUserName(String username) {
        return postRepository.findByUserName(username);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Page<Post> getPostsForPage(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);

        return postRepository.findAll(pageable);
    }

    public Page<Post> getMyPostsForPage(String username, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);

        return postRepository.findByMemberUsername(username, pageable);
    }

}
