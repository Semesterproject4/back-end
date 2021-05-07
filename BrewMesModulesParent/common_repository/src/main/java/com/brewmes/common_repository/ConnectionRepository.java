package com.brewmes.common_repository;

import com.brewmes.common.entities.Connection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends MongoRepository<Connection, String>{
}
