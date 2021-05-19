package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import com.brewmes.common_repository.BatchRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service("batchGetter")
public class BatchGetterImpl implements IBatchGetterService {
    @Autowired
    BatchRepository batchRepository;

    @Override
    public Page<Batch> getBatches(int page, int size) {
        return batchRepository.findAll(PageRequest.of(page, size));
    }

    public JsonObject getStaticBatchVariables(String id) {
        List<Batch> batchList = new ArrayList();
        batchList = batchRepository.findAll();
        Collections.reverse(batchList);
        for (Batch batch : batchList) {
            if (batch.getConnectionID().equals(id)) {
                JsonObject batchObject = new JsonObject();
                batchObject.add("id", new JsonPrimitive(batch.getID()));
                batchObject.add("type", new JsonPrimitive(batch.getProductType().productName));
                batchObject.add("amount", new JsonPrimitive(batch.getAmountToProduce()));
                batchObject.add("speed", new JsonPrimitive(batch.getDesiredSpeed()));

                return batchObject;
            }
        }
        return null;
    }
}
