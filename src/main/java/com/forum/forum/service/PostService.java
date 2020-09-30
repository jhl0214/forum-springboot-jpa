package com.forum.forum.service;

import com.forum.forum.domain.Image;
import com.forum.forum.domain.Member;
import com.forum.forum.domain.Post;
import com.forum.forum.dto.PostDTO;
import com.forum.forum.repository.MemberRepository;
import com.forum.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * Add post
     */
    @Transactional
    public Long addPost(PostDTO postDTO, String username) {
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .writer(postDTO.getWriter())
                .content(postDTO.getContent())
                .createdDateTime(postDTO.getCreatedDateTime())
                .modifiedDateTime(postDTO.getModifiedDateTime())
                .build();

        Member member = memberRepository.findByUserName(username);
        member.addPost(post);

        Long postId = postRepository.save(post).getId();

        // If images are uploaded, save the images.
        saveImages(postDTO, postId);

        return postId;
    }

    /**
     * Update post
     */
    @Transactional
    public void updatePost(Long postId, PostDTO postDto) {
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

    public Page<Post> getAllPostsForPage(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        return postRepository.findAll(pageable);
    }

    public Page<Post> getMyPostsForPage(String username, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        return postRepository.findByMemberUsername(username, pageable);
    }

    @Transactional
    public List<Image> saveImages(PostDTO postDTO, Long postId) {
        List<Image> images = new ArrayList<>();
        Post post = postRepository.findById(postId).get();

        // New image name in format username-postId-#
        String baseImgName = post.getWriter() + "-" + post.getId() + "-";

        // Root path
        String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
        // Directory to save images to
        String basePath = rootPath + "\\Java coding\\Spring\\forum-springboot-jpa\\src\\main\\resources\\static\\img";

        MultipartFile img1 = postDTO.getImg1();
        MultipartFile img2 = postDTO.getImg2();
        MultipartFile img3 = postDTO.getImg3();

        try {
            // If files are uploaded from the user, save these images.
            if (!img1.isEmpty()) {
                String imgName = baseImgName + img1.getOriginalFilename();
                images.add(new Image(basePath + "\\" + imgName, imgName, img1.getOriginalFilename()));
                File dest = new File(basePath + "\\" + imgName);
                img1.transferTo(dest);
                log.debug("IMAGE 1 SAVED.");
            }
            if (!img2.isEmpty()) {
                String imgName = baseImgName + img2.getOriginalFilename();
                images.add(new Image(basePath + "\\" + imgName, imgName, img2.getOriginalFilename()));
                File dest = new File(basePath + "\\" + imgName);
                img2.transferTo(dest);
                log.debug("IMAGE 2 SAVED.");
            }
            if (!img3.isEmpty()) {
                String imgName = baseImgName + img3.getOriginalFilename();
                images.add(new Image(basePath + "\\" + imgName, imgName, img3.getOriginalFilename()));
                File dest = new File(basePath + "\\" + imgName);
                img3.transferTo(dest);
                log.debug("IMAGE 3 SAVED.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add images to the post
        for (Image img : images) {
            post.addImage(img);
        }

        return images;
    }
}
