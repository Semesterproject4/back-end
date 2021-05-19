package com.brewmes.common.services;

import com.brewmes.common.entities.Batch;
import com.google.gson.JsonObject;
import org.springframework.data.domain.Page;

public interface IBatchGetterService {
    /**
     * Gets the {@code Batch}es from the database with pagination
     *
     * @param page The desired page number
     * @param size The desired size of the page
     * @return The list of {@code Batch}es
     */
    Page<Batch> getBatches(int page, int size);


    /**
     * Gets the {@code Batch} variables: {@code id}, {@code beerType}, {@code amount} and {@code speed} from the most recent {@code batch} that the given machine has produced.
     * @param id is the {@code connectionID} of the batch
     * @return a {@code JsonObject} containing the variables if successful; otherwise null
     */
    JsonObject getStaticBatchVariables(String id);
}
