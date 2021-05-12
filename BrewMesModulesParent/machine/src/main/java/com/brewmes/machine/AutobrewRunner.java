package com.brewmes.machine;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common.util.Command;
import org.springframework.beans.factory.annotation.Autowired;

public class AutobrewRunner implements Runnable {
    @Autowired
    ISubscribeService subscribeService;

    @Autowired
    IScheduleService scheduleService;

    @Autowired
    IMachineService machineService;

    String connectionID;

    public AutobrewRunner(String connectionID) {
        this.connectionID = connectionID;
    }


    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true) {
            int state = subscribeService.getLatestMachineData(connectionID).getState();
            if (state == 4) {
                if (!scheduleService.queueIsEmpty()) {
                    ScheduledBatch scheduledBatch = scheduleService.getFirstInQueue();
                    machineService.setVariables(scheduledBatch.getSpeed(), scheduledBatch.getType(), scheduledBatch.getAmount(), connectionID);
                    machineService.controlMachine(Command.START, connectionID);
                }
            } else if (state == 17) {
                machineService.controlMachine(Command.RESET, connectionID);
            } else if (state == 9) {
                machineService.stopAutoBrew(connectionID);
                break;
            }
        }
    }
}
