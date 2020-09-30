package com.forum.forum.controller;

import com.forum.forum.domain.Member;
import com.forum.forum.domain.Post;
import com.forum.forum.dto.PostDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

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
    public String viewPost(@RequestParam("id") Long postId, Model model) {
        Post post = postService.findPostById(postId);
        model.addAttribute("post", post);
        return "viewPost";
    }

    @GetMapping("/writePost")
    public String writePostForm(Model model) {
        model.addAttribute("postDTO", new PostDTO());
        return "writePost";
    }

    @PostMapping("/writePost")
    public String addPost(@Valid PostDTO postDTO, BindingResult result, Principal principal) {
        System.out.println(result.toString());
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

    @PostMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") Long postId, Principal principal) {
        Member member = memberService.findByUserName(principal.getName());
        Post post = postService.findPostById(postId);
        if (post.getMember().getId() != member.getId()) {
            return "home";
        }

        postService.deletePost(postId);
        return "redirect:/myPosts";
    }

}
