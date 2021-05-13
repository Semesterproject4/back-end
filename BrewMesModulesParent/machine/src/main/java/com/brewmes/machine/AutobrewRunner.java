package com.brewmes.machine;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.MachineState;
import org.springframework.beans.factory.annotation.Autowired;

public class AutobrewRunner implements Runnable {
    @Autowired(required = false)
    private ISubscribeService subscribeService;

    @Autowired(required = false)
    private IScheduleService scheduleService;

    @Autowired
    private IMachineService machineService;

    private String connectionID;

    public AutobrewRunner(String connectionID) {
        this.connectionID = connectionID;
    }

    @Override
    public void run() {
        while (true) {
            int state = subscribeService.getLatestMachineData(connectionID).getState();
            if (state == MachineState.IDLE.value) {
                if (!scheduleService.queueIsEmpty()) {
                    ScheduledBatch scheduledBatch = scheduleService.takeFirstInQueue();
                    machineService.setMachineVariables(scheduledBatch.getSpeed(), scheduledBatch.getType(), scheduledBatch.getAmount(), connectionID);
                    machineService.controlMachine(Command.START, connectionID);
                }
            } else if (state == MachineState.COMPLETE.value) {
                machineService.controlMachine(Command.RESET, connectionID);
            } else if (state == MachineState.STOPPED.value) {
                machineService.controlMachine(Command.RESET, connectionID);
            } else if (state == MachineState.ABORTED.value) {
                machineService.stopAutoBrew(connectionID);
                break;
            }
        }
    }
}