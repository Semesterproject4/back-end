package com.brewmes.common_repository;

import com.brewmes.common.entities.ScheduledBatch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<ScheduledBatch, String> {
}
