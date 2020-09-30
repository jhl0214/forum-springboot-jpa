package com.forum.forum.controller;

import com.forum.forum.domain.Member;
import com.forum.forum.domain.Post;
import com.forum.forum.dto.MemberDTO;
import com.forum.forum.service.MemberService;
import com.forum.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/")
    public String home(Model model, Principal principal,
                       @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.getAllPostsForPage(pageable);
        int totalPages = posts.getTotalPages();
        String username = principal.getName();

        model.addAttribute("posts", posts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("user", username);

        return "home";
    }

    @GetMapping("/join")
    public String joinForm(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        model.addAttribute("memberDTO", new MemberDTO());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDTO memberDTO, BindingResult result) {
        // Check if user already exists.
        if (result.hasErrors()) {
            return "join";
        }

        memberDTO.setAuth("ROLE_USER");

        try {
            memberService.join(memberDTO);
        } catch (IllegalStateException e) {
            result.addError(new FieldError("memberDTO", "username", "User already exists."));
            return "join";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }

        return "login";
    }


    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

}
