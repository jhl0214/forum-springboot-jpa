package com.forum.forum.config;

import com.forum.forum.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .ignoringAntMatchers("/deletePost/**", "/writePost/**", "/modifyPost/**", "/addComment", "/deleteComment")
            .and()
                .authorizeRequests()
                .antMatchers("/login*", "/join").permitAll() // Everyone can access
                .antMatchers("/list", "/writePost/**", "/modifyPost/**", "/deletePost/**", "/viewPost/**", "/addComment", "/deleteComment", "/myPosts", "/").hasAnyRole("ADMIN", "USER") // Only user and admin
                .antMatchers("/admin").hasRole("ADMIN") // Only admin
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login") // Login view
                .defaultSuccessUrl("/") // Redirect after login
                .failureUrl("/login?error=true") // Redirect if login fail
            .and()
                .logout()
                .logoutSuccessUrl("/login") // Redirect page after logout
                .invalidateHttpSession(true) // Delete session
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

}
