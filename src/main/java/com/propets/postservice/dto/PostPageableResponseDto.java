package com.propets.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostPageableResponseDto {
    private int itemsOnPage;
    private int currentPage;
    private int itemsTotal;
    private List<PostResponseDto>posts;
}
