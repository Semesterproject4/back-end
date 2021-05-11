package com.brewmes.common_repository;

import com.brewmes.common.entities.Batch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {
    public List<Batch> findByConnectionID(String connectionID);
}
