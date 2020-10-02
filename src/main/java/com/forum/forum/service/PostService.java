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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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
        if (!postDTO.getImg1().isEmpty() || !postDTO.getImg2().isEmpty() || !postDTO.getImg3().isEmpty()) {
            saveImages(postId, postDTO);
        }

        return postId;
    }

    /**
     * Update post
     */
    @Transactional
    public void updatePost(Long postId, PostDTO postDTO) {
        Post post = postRepository.findById(postId).get();

        postDTO.setWriter(post.getWriter());
        postDTO.setCreatedDateTime(post.getCreatedDateTime());
        postDTO.setModifiedDateTime(LocalDateTime.now());

        post.updatePost(postDTO);

        if ((postDTO.getImg1() != null && !postDTO.getImg1().isEmpty()) ||
                (postDTO.getImg2() != null && !postDTO.getImg2().isEmpty()) ||
                (postDTO.getImg3() != null && !postDTO.getImg3().isEmpty())) {
            saveImages(postId, postDTO);
        }
    }

    /**
     * Delete post
     */
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
        for (Image img : post.getImages()) {
            deleteImageInDirectory(img);
        }
    }

    /**
     * Get posts
     */
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).get();
    }

    public Page<Post> findPostsByWriter(String username, Pageable pageable) {
        return postRepository.findByWriterContainingIgnoreCase(username, pageable);
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

        return postRepository.findByWriterContainingIgnoreCase(username, pageable);
    }

    @Transactional
    public List<Image> saveImages(Long postId, PostDTO postDTO) {
        List<Image> images = new ArrayList<>();
        Post post = postRepository.findById(postId).get();

        // New image name in format username-postId-#
        String baseImgName = post.getWriter() + "-" + post.getId() + "-";

        // Directory to save images to
        File f = new File(System.getProperty("user.home") + "\\forumImages");
        if (!f.exists()) {
            f.mkdir();
        }

        String basePath = f.getAbsolutePath();

        MultipartFile img1 = postDTO.getImg1();
        MultipartFile img2 = postDTO.getImg2();
        MultipartFile img3 = postDTO.getImg3();

        try {
            // If files are uploaded from the user, save these images.
            if (img1 != null && !img1.isEmpty()) {
                String imgName = baseImgName + img1.getOriginalFilename();
                images.add(new Image(basePath + "\\" + imgName, imgName, img1.getOriginalFilename()));
                File dest = new File(basePath + "\\" + imgName);
                img1.transferTo(dest);
                log.info("IMAGE 1 SAVED.");
            }
            if (img2 != null && !img2.isEmpty()) {
                String imgName = baseImgName + img2.getOriginalFilename();
                images.add(new Image(basePath + "\\" + imgName, imgName, img2.getOriginalFilename()));
                File dest = new File(basePath + "\\" + imgName);
                img2.transferTo(dest);
                log.info("IMAGE 2 SAVED.");
            }
            if (img3 != null && !img3.isEmpty()) {
                String imgName = baseImgName + img3.getOriginalFilename();
                images.add(new Image(basePath + "\\" + imgName, imgName, img3.getOriginalFilename()));
                File dest = new File(basePath + "\\" + imgName);
                img3.transferTo(dest);
                log.info("IMAGE 3 SAVED.");
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

    private void deleteImageInDirectory(Image image) {
        File img = new File(image.getSrc());

        if (img.exists()) {
            boolean deleted = img.delete();
            if (deleted) {
                log.info(image.getImgName() + " DELETED SUCCESSFULLY.");
            } else {
                log.info("ERROR WHILE DELETING " + image.getImgName());
            }
        }
    }

    public Page<Post> findPostsByTitle(String title, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        return postRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Post> findPostsByTitleAndWriter(String title, String writer, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        return postRepository.findByTitleContainingIgnoreCaseAndWriter(title, writer, pageable);
    }
}
