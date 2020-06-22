package com.propets.postservice.service;

import com.propets.postservice.dto.PostPageableResponseDto;
import com.propets.postservice.dto.PostRequestDto;
import com.propets.postservice.dto.PostResponseDto;

public interface IPostService {
    PostResponseDto addPost(String email, PostRequestDto postRequestDto);
    PostResponseDto updatePost( long postId,String email, PostRequestDto postRequestDto);
    PostResponseDto deletePost(long postId,String email);
    PostResponseDto getPost(long id);
    PostPageableResponseDto getPosts(int itemsOnPage,int currentPage);
    void complainPost(long id);
    
}    

