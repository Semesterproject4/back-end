package com.brewmes.common.services;

import com.brewmes.common.entities.Batch;
import org.springframework.data.domain.Page;

import java.util.List;


public interface IBatchGetterService {
    /**
     * Gets the {@code Batch}es from the database with pagination
     * @param page The desired page number
     * @param size The desired size of the page
     * @return The list of {@code Batch}es
     */
    Page<Batch> getBatches(int page, int size);
}
