package com.forum.forum.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@SequenceGenerator(name = "MEMBER_SEQ", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 1)
public class Member implements UserDetails {

    @Id @GeneratedValue(generator = "MEMBER_SEQ")
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @Column(unique = true, length = 20)
    private String username;
    private String password;

    private String email;
    private String auth;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts = new ArrayList<Post>();

    protected Member() {

    }

    @Builder
    public Member(String name, String username, String password, String email, String auth) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.auth = auth;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setMember(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        // split by comma because ADMIN needs to have both admin and user role,
        // and it's going to be assigned as follow: ROLE_ADMIN,ROLE_USER
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
