package com.propets.postservice.rest;

import com.propets.postservice.dto.PostPageableResponseDto;
import com.propets.postservice.dto.PostRequestDto;
import com.propets.postservice.dto.PostResponseDto;
import com.propets.postservice.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;

import static com.propets.postservice.api.ApiConstants.*;

@RestController
public class UserPostController {
    @Autowired
    IPostService postService;
    
    @PostMapping(value = PREFIX+OWNER+"/{ownerId}")
    PostResponseDto createPost(@PathVariable String ownerId,
                               @RequestBody PostRequestDto postRequestDto){
        return postService.addPost(ownerId,postRequestDto);
    }
    @PutMapping(value = PREFIX+"/{ownerId}"+"/{postId}")
    PostResponseDto updatePost(@PathVariable String ownerId,
                               @PathVariable long postId,
                               @RequestBody PostRequestDto postRequestDto){
        return postService.updatePost(postId,ownerId,postRequestDto);
    }
    @DeleteMapping(value = PREFIX+"/{ownerId}"+"/{postId}")
    PostResponseDto deletePost(@PathVariable String ownerId,
                               @PathVariable long postId){
        return postService.deletePost(postId,ownerId);
    }
    @GetMapping(value = PREFIX+"/{postId}")
    PostResponseDto getPost(@PathVariable long postId){
        return postService.getPost(postId);
    }
    @GetMapping(value = PREFIX+VIEW)
    PostPageableResponseDto viewPosts(@RequestParam int itemsOnPage,
                                     @RequestParam int currentPage){
        return postService.getPosts(itemsOnPage,currentPage);
    }
    @PutMapping(value = PREFIX+COMPLAIN+"/{postId}")
    void complain(@PathVariable long postId){
        postService.complainPost(postId);
        return;
    }
}
