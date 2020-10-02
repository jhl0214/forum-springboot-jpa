package com.forum.forum.controller;

import com.forum.forum.domain.Comment;
import com.forum.forum.domain.Member;
import com.forum.forum.domain.Post;
import com.forum.forum.dto.CommentDTO;
import com.forum.forum.dto.PostDTO;
import com.forum.forum.service.CommentService;
import com.forum.forum.service.MemberService;
import com.forum.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/myPosts")
    public String myPosts(Model model, Principal principal,
                           @PageableDefault(size=10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = principal.getName();
        Page<Post> posts = postService.getMyPostsForPage(username, pageable);

        model.addAttribute("posts", posts);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("user", username);
        return "myPosts";
    }

    @GetMapping("/viewPost")
    public String viewPost(@RequestParam("id") Long postId, Model model, Principal principal) {
        Post post = postService.findPostById(postId);
        List<Comment> comments = commentService.findCommentsByPostId(postId);
        System.out.println(comments);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentDTO", new CommentDTO());
        model.addAttribute("user", principal.getName());

        return "viewPost";
    }

    @GetMapping("/writePost")
    public String writePostForm(Model model) {
        model.addAttribute("postDTO", new PostDTO());
        return "writePost";
    }

    @PostMapping("/writePost")
    public String addPost(@Valid PostDTO postDTO, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "writePost";
        }

        String username = principal.getName();

        postDTO.setWriter(username);
        postDTO.setCreatedDateTime(LocalDateTime.now());
        postDTO.setModifiedDateTime(LocalDateTime.now());

        Long postId = postService.addPost(postDTO, username);

        return "redirect:/";
    }

    @GetMapping("/modifyPost")
    public String modifyPostForm(@RequestParam("id") Long postId, Model model) {
        Post post = postService.findPostById(postId);

        PostDTO postDTO = new PostDTO();
        postDTO.setWriter(post.getWriter());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedDateTime(post.getCreatedDateTime());
        postDTO.setTitle(post.getTitle());

        model.addAttribute("postDTO", postDTO);
        model.addAttribute("numOfImagesForPost", post.getImages().size());

        return "modifyPost";
    }

    @PostMapping("/modifyPost")
    public String modifyPost(@RequestParam("id") Long postId, @Valid PostDTO postDTO,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            Post post = postService.findPostById(postId);
            model.addAttribute(model.addAttribute("numOfImagesForPost", post.getImages().size()));
            return "modifyPost";
        }

        postService.updatePost(postId, postDTO);

        return "redirect:/viewPost?id=" + postId;
    }

    @PostMapping("/deletePost")
    public String deletePost(@RequestParam("postId") Long postId, Principal principal) {
        Member member = memberService.findByUserName(principal.getName());
        Post post = postService.findPostById(postId);
        if (post.getMember().getId() != member.getId()) {
            return "home";
        }

        postService.deletePost(postId);
        return "redirect:/";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam("postId") Long postId, @Valid CommentDTO commentDTO,
                             BindingResult result, Model model, Principal principal) {

        if (result.hasErrors()) {
            Post post = postService.findPostById(postId);
            List<Comment> comments = commentService.findCommentsByPostId(postId);

            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            return "viewPost";
        }

        commentDTO.setWriter(principal.getName());
        commentService.addComment(postId, commentDTO);

        return "redirect:/viewPost?id=" + postId;
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId, Principal principal) {
        Comment comment = commentService.findCommentById(commentId);

        if (!comment.getWriter().equals(principal.getName())) {
            return "home";
        }

        commentService.deleteComment(commentId);
        return "redirect:/viewPost?id=" + comment.getPost().getId();
    }

    @GetMapping("/search")
    public String searchPost(@RequestParam("search") String search, @RequestParam("searchBy") String searchBy,
                             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model, Principal principal) {

        if (search == null || search.isBlank()) {
            return "redirect:/";
        }

        if (searchBy.equals("title")) {
            Page<Post> posts = postService.findPostsByTitle(search, pageable);
            model.addAttribute("posts", posts);
            model.addAttribute("totalPages", posts.getTotalPages());
        } else if (searchBy.equals("writer")) {
            Page<Post> posts = postService.findPostsByWriter(search, pageable);
            model.addAttribute("posts", posts);
            model.addAttribute("totalPages", posts.getTotalPages());

        }

        model.addAttribute("searchKeyWord", search);
        model.addAttribute("user", principal.getName());

        return "searchPosts";
    }

}
