package com.brewmes.machine;

import com.brewmes.common.entities.ScheduledBatch;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.MachineState;

public class AutobrewRunner implements Runnable {
    private ISubscribeService subscribeService;
    private IScheduleService scheduleService;
    private IMachineService machineService;
    private String connectionID;

    public AutobrewRunner(String connectionID, ISubscribeService subscribeService, IScheduleService scheduleService, IMachineService machineService) {
        this.connectionID = connectionID;
        this.subscribeService = subscribeService;
        this.scheduleService = scheduleService;
        this.machineService = machineService;
    }

    @Override
    public void run() {
        subscribeService.subscribeToMachineValues(connectionID);
        while (true) {
            try {
                Thread.sleep(1_000);
                MachineState state = subscribeService.getLatestMachineData(connectionID).getState();
                if (state == MachineState.IDLE) {
                    if (!scheduleService.queueIsEmpty()) {
                        ScheduledBatch scheduledBatch = scheduleService.takeFirstInQueue();
                        machineService.setMachineVariables(scheduledBatch.getSpeed(), scheduledBatch.getType(), scheduledBatch.getAmount(), connectionID);
                        machineService.controlMachine(Command.START, connectionID);
                    }
                } else if (state == MachineState.COMPLETE) {
                    machineService.controlMachine(Command.RESET, connectionID);
                } else if (state == MachineState.STOPPED) {
                    machineService.controlMachine(Command.RESET, connectionID);
                } else if (state == MachineState.ABORTED) {
                    machineService.stopAutoBrew(connectionID);
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
