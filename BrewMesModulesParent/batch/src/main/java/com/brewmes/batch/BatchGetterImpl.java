package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.services.IBatchGetterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchGetterImpl implements IBatchGetterService {
    @Override
    public List<Batch> getBatches(int page, int size) {
        return null;
    }

    @Override
    public boolean containsId(String id) {
        return false;
    }
}
