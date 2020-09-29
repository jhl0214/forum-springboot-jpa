package com.forum.forum.controller;

import com.forum.forum.domain.Post;
import com.forum.forum.dto.MemberDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/")
    public String home(Model model, Principal principal,
                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.getPostsForPage(pageable);
        int totalPages = posts.getTotalPages();
        String username = principal.getName();

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("user", username);

        return "home";
    }

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "join";
        }

        memberDto.setAuth("ROLE_USER");
        memberService.join(memberDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

}
