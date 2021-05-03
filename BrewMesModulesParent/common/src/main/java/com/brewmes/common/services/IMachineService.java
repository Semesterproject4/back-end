package com.brewmes.common.services;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;

import java.util.List;

public interface IMachineService {

    void controlMachine(Command command, String machineId);

    void setVariables(double speed, Products beertype, int batchSize, String machineId);

    void subscibeToMachineValues(String machineId);

    List<Connection> getConnections();

    boolean addConnection(String ip, String name);

    boolean removeConnection(String machineId);

    void startAutoBrew(String machineId);

    void stopAutoBrew(String machineId);
}
