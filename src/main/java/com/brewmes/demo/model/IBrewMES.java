package com.brewmes.demo.model;

import java.util.Map;
import java.util.UUID;

public interface IBrewMES {

    void controlMachine(String command, UUID id);

    void setMachineVariables(double speed, String beerType, int batchSize, UUID id);

    Batch getBatch(UUID id);

    void generateReport(Batch batch);

    void disconnectMachine(UUID id);

    boolean connectMachine(String ipAddress);

    Map<UUID, Machine> getMachines();

    Map<String, Object> getBatchesPage(int page, int size);

}
