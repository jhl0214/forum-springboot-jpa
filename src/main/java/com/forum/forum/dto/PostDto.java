package com.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {

    private String title;
    private String writer;
    private String content;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

}
