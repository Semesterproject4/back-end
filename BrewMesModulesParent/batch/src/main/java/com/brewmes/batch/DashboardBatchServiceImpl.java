package com.brewmes.batch;


import com.brewmes.common.entities.Batch;
import com.brewmes.common_repository.BatchRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("dashboard")
public class DashboardBatchServiceImpl extends PrepareData {

    @Autowired
    private BatchRepository batchRepository;


    @Override
    public String prepareBatchReportService(String id) {
        Optional<Batch> optional = batchRepository.findById(id);
        Batch batch = optional.isPresent() ? optional.get() : null;
        if (batch != null) {
            DataOverTime overTime = super.prepareData(batch);
            return serialize(overTime);
        } else {
            return null;
        }
    }

    /**
     * A method to serialize {@Code DataOverTime} objects.
     *
     * @param overTime The {@Code DataOverTime} object that needs to be serialized to json
     * @return A {@Code String} representation of the given object in JSON format.
     */
    private String serialize(DataOverTime overTime) {
        Gson gson = new Gson()
                .newBuilder()
                .serializeSpecialFloatingPointValues()
                .create();
        return gson.toJson(overTime, DataOverTime.class);
    }
}
