package com.forum.forum.controller;

import com.forum.forum.domain.Member;
import com.forum.forum.domain.Post;
import com.forum.forum.service.MemberService;
import com.forum.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/myPosts")
    private String myPosts(Model model, Principal principal) {
        String username = principal.getName();
        List<Post> posts = postService.findPostsByUserName(username);
        model.addAttribute("posts", posts);
        model.addAttribute("user", username);
        return "myPosts";
    }

    @PostMapping("/deletePost/{postId}")
    private String deletePost(@PathVariable("postId") Long postId, Principal principal) {
        Member member = memberService.findByUserName(principal.getName());
        Post post = postService.findPostById(postId);
        if (post.getMember().getId() != member.getId()) {
            return "home";
        }

        postService.deletePost(postId);
        return "redirect:/myPosts";
    }

}
