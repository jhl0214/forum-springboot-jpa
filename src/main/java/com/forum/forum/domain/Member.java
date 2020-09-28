package com.forum.forum.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(name = "MEMBER_SEQ", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 1)
public class Member {

    @Id @GeneratedValue(generator = "MEMBER_SEQ")
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;
    private String username;
    private String password;

    private String email;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts = new ArrayList<Post>();

    protected Member() {

    }

    public Member(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setMember(this);
    }

}
