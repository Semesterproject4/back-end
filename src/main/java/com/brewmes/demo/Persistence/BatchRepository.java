package com.brewmes.demo.Persistence;

import com.brewmes.demo.model.Batch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BatchRepository extends CrudRepository<Batch, UUID>, PagingAndSortingRepository<Batch, UUID> {
}
