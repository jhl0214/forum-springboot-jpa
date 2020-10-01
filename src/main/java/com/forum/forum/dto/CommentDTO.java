package com.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private String writer;
    @NotEmpty(message = "This field cannot be blank.")
    @Size(max = 200, message = "Cannot be more than 200 characters.")
    private String content;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

}
