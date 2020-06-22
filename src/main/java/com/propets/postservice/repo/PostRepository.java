package com.propets.postservice.repo;

import com.propets.postservice.model.PostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<PostEntity,Long> {
}
