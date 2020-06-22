package com.propets.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PostResponseDto {
    private long id;
    private String ownerId;
    private LocalDateTime postDate;
    private String text;
    private List<String>images;
}
