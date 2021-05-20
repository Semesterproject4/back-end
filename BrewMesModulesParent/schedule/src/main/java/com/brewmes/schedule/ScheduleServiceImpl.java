package com.brewmes.schedule;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common_repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public boolean addToQueue(ScheduledBatch scheduledBatch) {
        ScheduledBatch insertedBatch = scheduleRepository.insert(scheduledBatch);

        return insertedBatch.getId() != null;
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
    public ScheduledBatch takeFirstInQueue() {
        if (!queueIsEmpty()) {
            List<ScheduledBatch> newList = getQueue();
            ScheduledBatch firstBatch = newList.get(0);

            boolean removal = newList.remove(firstBatch);
            boolean prio = prioritizeQueue(newList);

            if (removal && prio) {
                return firstBatch;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean queueIsEmpty() {

        return scheduleRepository.findAll().isEmpty();
    }

    @Override
    public boolean prioritizeQueue(List<ScheduledBatch> prioritizedList) {
        scheduleRepository.deleteAll();

        if (queueIsEmpty()) {
            List<ScheduledBatch> newPrioList = scheduleRepository.saveAll(prioritizedList);
            return prioritizedList.equals(newPrioList);
        } else {
            return false;
        }
    }
}
