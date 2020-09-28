package com.forum.forum.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@SequenceGenerator(name ="IMG_SEQ", sequenceName = "IMG_SEQ", initialValue = 1, allocationSize = 1)
public class Image {

    @Id @GeneratedValue(generator = "IMG_SEQ")
    @Column(name = "IMAGE_ID")
    private Long id;

    private String src;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    protected Image() {
    }

    public Image(String src) {
        this.src = src;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
