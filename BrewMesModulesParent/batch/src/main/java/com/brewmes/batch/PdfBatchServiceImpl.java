package com.brewmes.batch;


import com.brewmes.common.entities.Batch;
import com.brewmes.common_repository.BatchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("pdf")
public class PdfBatchServiceImpl extends PrepareData {

    @Autowired
    public BatchRepository batchRepo;

    @Override
    public String prepareBatchReportService(String id) {
        Optional<Batch> optional = batchRepo.findById(id);
        DataOverTime data;
        String path;
        if (optional.isPresent()) {
            data = prepareData(optional.get());
            PdfReportGenerator.generatePdf(data);   //maybe this method needs to return a String path or at least take one as parameter.
            path = "realPath";
        } else {
            path = "";
        }
        return path;
    }
}
