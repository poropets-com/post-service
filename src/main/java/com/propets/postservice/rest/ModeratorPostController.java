package com.propets.postservice.rest;

import com.propets.postservice.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.propets.postservice.api.ApiConstants.*;

@RestController
public class ModeratorPostController {
    
    @Autowired
    IPostService postService;
    
    @PutMapping(value = PREFIX+HIDE+"/{postId}")
    void hidePost(@PathVariable long postId){
        System.out.println("jjjjjjjjjjj");
    }
}
