package com.forum.forum.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@SequenceGenerator(name = "COMMENT_SEQ", sequenceName = "COMMENT_SEQ", initialValue = 1, allocationSize = 1)
public class Comment {

    @Id @GeneratedValue(generator = "COMMENT_SEQ")
    @Column(name = "COMMENT_ID")
    private Long id;

    private String writer;
    private String content;
    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    protected Comment() {

    }

    public Comment(String writer, String content, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.writer = writer;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public void changeModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
