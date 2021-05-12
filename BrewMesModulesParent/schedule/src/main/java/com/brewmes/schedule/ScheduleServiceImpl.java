package com.brewmes.schedule;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common_repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ScheduleServiceImpl implements IScheduleService {

    @Autowired
    public ScheduleRepository scheduleRepository;

    @Override
    public int addToQueue(ScheduledBatch scheduledBatch) {
        ScheduledBatch insertedBatch = scheduleRepository.insert(scheduledBatch);
        int placement = scheduleRepository.findAll().indexOf(insertedBatch); //NOSONAR

        return placement;
    }

    @Override
    public boolean removeFromQueue(String scheduleID) {
        scheduleRepository.deleteById(scheduleID);

        return !scheduleRepository.existsById(scheduleID);
    }

    @Override
    public List<ScheduledBatch> getQueue() {

        return scheduleRepository.findAll();
    }

    @Override
    public ScheduledBatch getFirstInQueue() {

        return scheduleRepository.findAll().get(0);
    }

    @Override
    public boolean queueIsEmpty() {

        return scheduleRepository.findAll().isEmpty();
    }

    @Override
    public boolean prioritizeQueue(List<ScheduledBatch> prioritizedList) {
        scheduleRepository.deleteAll();

        if (scheduleRepository.findAll().isEmpty()) {
            List<ScheduledBatch> newPrioList = scheduleRepository.saveAll(prioritizedList);
            return prioritizedList == newPrioList;
        } else {
            return false;
        }
    }
}
