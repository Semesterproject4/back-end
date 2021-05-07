package com.brewmes.common_repository;

import com.brewmes.common.entities.Batch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {
}
