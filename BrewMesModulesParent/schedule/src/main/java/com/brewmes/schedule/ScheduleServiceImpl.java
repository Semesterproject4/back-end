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
