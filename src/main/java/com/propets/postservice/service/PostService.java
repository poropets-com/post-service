package com.propets.postservice.service;

import com.propets.postservice.dto.PostPageableResponseDto;
import com.propets.postservice.dto.PostRequestDto;
import com.propets.postservice.dto.PostResponseDto;
import com.propets.postservice.model.PostEntity;
import com.propets.postservice.model.SequenceId;
import com.propets.postservice.repo.PostRepository;
import com.propets.postservice.repo.SequenceIdRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService implements IPostService {
    
    private final PostRepository postRepository;
    private final SequenceIdRepository sequenceIdRepository;

    @Autowired
    public PostService(PostRepository postRepository, SequenceIdRepository sequenceIdRepository) {
        this.postRepository = postRepository;
        this.sequenceIdRepository = sequenceIdRepository;
    }

    @Override
    public PostResponseDto addPost(String email, PostRequestDto postRequestDto) {
        long id=getNextId();
        LocalDateTime date=LocalDateTime.now();
        List<String>images=postRequestDto.getImages()==null?new ArrayList<>():postRequestDto.getImages();
        PostEntity postEntity=new PostEntity(id,email,date,postRequestDto.getText(),images,false);
        postRepository.save(postEntity);
        log.info("IN addPost- user {} successfully add new post id {}",email,id);
        return toPostResponseDto(postEntity);
    }

    private PostResponseDto toPostResponseDto(PostEntity postEntity) {
        return new PostResponseDto(postEntity.getId(),
                postEntity.getOwnerId(),
                postEntity.getPostDate(),
                postEntity.getText(),
                postEntity.getImages());
    }

    private long getNextId() {
        long lastId=1;
        SequenceId sequenceId=sequenceIdRepository.findById(1).orElse(null);
        if (sequenceId==null){
            sequenceId=new SequenceId(1,new ArrayList<>(Arrays.asList(lastId)));
            sequenceIdRepository.save(sequenceId);
            return lastId;
        }
        if (sequenceId.getNextId().size()>1){
            lastId=sequenceId.getNextId().get(1);
            sequenceId.getNextId().remove(1);
            sequenceIdRepository.save(sequenceId);
            return lastId;
        }
        lastId=sequenceId.getNextId().get(0)+1;
        sequenceId.getNextId().remove(0);
        sequenceId.getNextId().add(lastId);
        sequenceIdRepository.save(sequenceId);
        return lastId;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public PostResponseDto updatePost(long postId, String email, PostRequestDto postRequestDto) {
        PostEntity postEntity=postRepository.findById(postId).orElse(null);
        if (postEntity==null){
            log.error("IN updatePost- user {} add wrong postId {} ",email,postId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"post with id "+postId+" not found" );
        }
        if (!postEntity.getOwnerId().equals(email)){
            log.error("IN updatePost- user {} add wrong postId {} .post have another author",email,postId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"post with id "+postId+" have another author" );
        }
        postRepository.save(postEntity);
        log.info("IN updatePost- post id {} successfully updated by author {}",postId,email);
        return toPostResponseDto(postEntity);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public PostResponseDto deletePost(long postId, String email) {
        PostEntity postEntity=postRepository.findById(postId).orElse(null);
        if (postEntity==null){
            log.error("IN deletePost- user {} add wrong postId {} ",email,postId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"post with id "+postId+" not found" );
        }
        if (!postEntity.getOwnerId().equals(email)){
            log.error("IN updatePost- user {} add wrong postId {} .post have another author",email,postId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"post with id "+postId+" have another author" );
        }
        postRepository.deleteById(postId);
        log.info("IN deletePost- post id {} successfully removed by author {}",postId,email);
        return toPostResponseDto(postEntity);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public PostResponseDto getPost(long id) {
        PostEntity postEntity=postRepository.findById(id).orElse(null);
        if (postEntity==null){
            log.error("IN getPost- added wrong postId {} ",id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"post with id "+id+" not found" );
        }
        log.info("IN getPost- post id {} successfully loaded",id);
        return toPostResponseDto(postEntity);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public PostPageableResponseDto getPosts(int itemsOnPage, int currentPage) {
        long collectionSize = postRepository.count();
        Pageable pageable=PageRequest.of(currentPage,itemsOnPage,Sort.by(Sort.Direction.DESC,"postDate"));
        Page<PostEntity> posts=postRepository.findAll(pageable);
        if (posts.isEmpty()){
            return new PostPageableResponseDto(posts.getSize(),currentPage,(int)posts.getTotalElements(),new ArrayList<>());
        }
        List<PostResponseDto> potsDto=posts.get().map(p->toPostResponseDto(p)).collect(Collectors.toList());
        return new PostPageableResponseDto(posts.getSize(),currentPage,(int)posts.getTotalElements(),potsDto);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void complainPost(long id) {
        PostEntity postEntity=postRepository.findById(id).orElse(null);
        if (postEntity==null){
            log.error("IN complainPost- added wrong postId {} ",id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"post with id "+id+" not found" );
        }
        postEntity.setComplaint(true);
        postRepository.save(postEntity);
        log.info("IN complainPost- post id {} successfully added flag to moderator");
    }
}
