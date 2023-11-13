package com.blog.payload;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostDto {
    private long id;

    @NotEmpty
   @Size(min=2, message="Title Should be more than 2 character")

    @NotEmpty
    @Size(min=5, message="Description Should be minimum 5 character")
    private String title;
    private String description;
    private String content;

}
