package com.brewmes.common.services;

public interface IBatchReportService {

    /**
     * Gets a specific batch and formats it for the desired implementation
     * @param id is a unique string id of a batch
     * @return the prepared batch report
     */
    String prepareBatchReportService(String id);
}
