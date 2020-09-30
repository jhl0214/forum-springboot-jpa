package com.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostDTO {

    @NotEmpty(message = "Please enter a title.")
    @Size(max=40, message = "Cannot be more than 40 characters.")
    private String title;
    private String writer;
    @NotEmpty(message = "This field cannot be blank.")
    @Size(max=300, message = "Cannot be more than 300 characters.")
    private String content;

    private MultipartFile img1;
    private MultipartFile img2;
    private MultipartFile img3;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

}
