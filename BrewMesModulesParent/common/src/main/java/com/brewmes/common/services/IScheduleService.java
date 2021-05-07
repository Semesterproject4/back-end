package com.brewmes.common.services;

import com.brewmes.common.entities.ScheduledBatch;

import java.util.List;

public interface IScheduleService {

    /**
     * Adds a batch to the global queue.
     *
     * @param scheduledBatch The batch to add to the queue.
     * @return the placement of the added {@code ScheduledBatch} in the queue; Returns -1 if the batch could not be placed in the queue.
     */
    int addToQueue(ScheduledBatch scheduledBatch);

    /**
     * Removes a batch from the queue.
     *
     * @param scheduleID a {@code ScheduledBatch}'s ID.
     * @return {@code True} if the {@code ScheduledBatch} was successfully removed; {@code False} if an error occurred.
     */
    boolean removeFromQueue(String scheduleID);

    /**
     * Looks through the queue and returns it as a {@code List}.
     *
     * @return Every {@code ScheduledBatch} in the queue.
     */
    List<ScheduledBatch> getQueue();

    /**
     * Gets the first entity in the queue.
     *
     * @return the next batch to produce; {@code null} if no {@code ScheduledBatch} is found in the queue.
     */
    ScheduledBatch getFirstInQueue();

    /**
     * Checks if there are any batches in the queue.
     *
     * @return {@code True} if there is nothing in the queue; {@code False} if there are batches in the queue.
     */
    boolean queueIsEmpty();

    /**
     * Moves the {@code ScheduledBatch} up in the queue by one spot. If the batch was number 4 in the queue, it will now be number 3.
     *
     * @param scheduleID a {@code ScheduledBatch}'s ID.
     * @return {@code True} if the {@code ScheduledBatch} was moved; {@code False} if an error occurred.
     */
    boolean moveUpInQueue(String scheduleID);

    /**
     * Moves the {@code ScheduledBatch} down in the queue by one spot. If the batch was number 2 in the queue, it will now be number 3.
     *
     * @param scheduleID a {@code ScheduledBatch}'s ID.
     * @return {@code True} if the {@code ScheduledBatch} was moved; {@code False} if an error occurred.
     */
    boolean moveDownInQueue(String scheduleID);

}
