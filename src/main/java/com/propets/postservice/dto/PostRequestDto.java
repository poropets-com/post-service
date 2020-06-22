package com.propets.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class PostRequestDto {
    private String text;
    private List<String>images;
}
