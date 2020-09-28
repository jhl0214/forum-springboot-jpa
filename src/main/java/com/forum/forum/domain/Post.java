package com.forum.forum.domain;

import com.forum.forum.dto.PostDto;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", initialValue = 1, allocationSize = 1)
public class Post {

    @Id @GeneratedValue(generator = "POST_SEQ")
    @Column(name = "POST_ID")
    private Long id;

    private String title;
    private String writer;
    private String content;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<Image>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    protected Post() {

    }

    public Post(String title, String writer, String content, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public void setMember(Member member) {
        this.member =member;
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void changeModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public void updatePost(PostDto postDto) {
        this.title = postDto.getTitle();
        this.writer = postDto.getWriter();
        this.content = postDto.getContent();
        this.createdDateTime = postDto.getCreatedDateTime();
        this.modifiedDateTime = postDto.getModifiedDateTime();
    }
}
