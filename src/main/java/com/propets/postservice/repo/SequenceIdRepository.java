package com.propets.postservice.repo;

import com.propets.postservice.model.SequenceId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SequenceIdRepository extends MongoRepository<SequenceId,Integer> {
}
