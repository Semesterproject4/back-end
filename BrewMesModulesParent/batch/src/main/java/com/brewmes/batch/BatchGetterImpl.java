package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import com.brewmes.common_repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service("batchGetter")
public class BatchGetterImpl implements IBatchGetterService {
    @Autowired
    BatchRepository batchRepository;

    @Override
    public Page<Batch> getBatches(int page, int size) {
        return batchRepository.findAll(PageRequest.of(page, size));
    }
}
