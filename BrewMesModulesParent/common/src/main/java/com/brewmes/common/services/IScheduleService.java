package com.brewmes.common.services;

import com.brewmes.common.entities.ScheduledBatch;

import java.util.List;

public interface IScheduleService {

    /**
     * Adds a batch to the global queue.
     *
     * @param speed    Speed of the batch.
     * @param beerType Beer type of the batch.
     * @param amount   Amount to produce.
     * @return the placement of the added batch in the queue.
     */
    int addToQueue(int speed, int beerType, int amount);

    /**
     * Removes a batch from the queue.
     *
     * @param scheduleID a {@code ScheduledBatch}'s ID.
     */
    void removeFromQueue(int scheduleID);

    /**
     * Looks through the queue and returns it as a {@code List}.
     *
     * @return Every {@code ScheduledBatch} in the queue.
     */
    List<ScheduledBatch> getQueue();

    /**
     * Gets the entity in the queue.
     *
     * @return the next batch to produce.
     */
    ScheduledBatch getFirstInQueue();

    /**
     * Checks if there are any batches in the queue.
     *
     * @return {@code True} if there is nothing in the queue; {@code False} if there are batches in the queue.
     */
    boolean isQueueEmpty();
}
