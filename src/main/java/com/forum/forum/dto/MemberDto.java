package com.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberDto {

    @NotEmpty(message = "Please enter your name.")
    private String name;
    @NotEmpty(message = "Please enter your username.")
    private String username;
    @NotEmpty(message = "Please enter your password.")
    @Size(min = 8, message = "Password has to be at least 8 characters.")
    private String password;
    @Email(message = "Please enter a valid email.")
    @NotEmpty(message = "Please enter your email")
    private String email;

    private String auth;

}
