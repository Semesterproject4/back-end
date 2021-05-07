package com.brewmes.common.services;

import com.brewmes.common.entities.Batch;
import java.util.List;


public interface IBatchGetterService {
    /**
     * Gets the {@code Batch}es from the database with pagination
     * @param page The desired page number
     * @param size The desired size of the page
     * @return The list of {@code Batch}es
     */
    List<Batch> getBatches(int page, int size);

    /**
     * Checks whether a {@code Batch} exists in the database
     * @param id of the {@code Batch} which is desired to be checked
     * @return {@code true} if the {@code Batch} exists in the database, otherwise {@code false}
     */
    boolean containsId(String id);
}
