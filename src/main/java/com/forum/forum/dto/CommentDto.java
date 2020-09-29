package com.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    @NotEmpty
    private String writer;
    @NotEmpty(message = "This field cannot be blank.")
    private String content;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

}
