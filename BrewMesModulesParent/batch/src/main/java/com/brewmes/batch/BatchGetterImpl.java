package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import com.brewmes.common_repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service("batchGetter")
public class BatchGetterImpl implements IBatchGetterService {
    @Autowired
    private BatchRepository batchRepository;

    @Override
    public Page<Batch> getBatches(int page, int size) {
        return batchRepository.findAll(PageRequest.of(page, size));
    }

    public Batch getStaticBatchVariables(String id) {
        List<Batch> batchList = batchRepository.findByConnectionID(id);

        if (batchList != null) {
            Collections.reverse(batchList);
            Batch returnBatch = new Batch();
            returnBatch.setID(batchList.get(0).getID());
            returnBatch.setAmountToProduce(batchList.get(0).getAmountToProduce());
            returnBatch.setDesiredSpeed(batchList.get(0).getDesiredSpeed());
            returnBatch.setProductType(batchList.get(0).getProductType());
            return returnBatch;

        }
        return null;
    }
}
