package com.brewmes.common.services;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;

import java.util.List;

public interface IMachineService {

    /**
     * Takes a command and send it to the desired machine
     * @param command The command that should be send to the machine
     * @param machineId The ID of the machine that should receive the command
     */
    void controlMachine(Command command, String machineId);

    /**
     * Sets the desired values for a production
     * @param speed The desired speed that the production should run at
     * @param beerType The desired beer type of the production
     * @param batchSize The desired amount of beer to be produced
     * @param machineId The ID of the desired machine the values should be set on
     */
    void setVariables(double speed, Products beerType, int batchSize, String machineId);

    /**
     * Sets up the desired subscriptions to a machines values.
     * @param machineId The ID of the machine desired for the subscription
     */
    void subscribeToMachineValues(String machineId);

    /**
     * Gets all the connections from the database
     * @return The list of Connection objects gotten from the database
     */
    List<Connection> getConnections();

    /**
     * Adds a connection to a Machine
     * @param ip The IP address of the machine
     * @param name The desired name of the machine, used for easy identification of the machine
     * @return Returns true if the connection was successfully made, returns false if the connection failed.
     */
    boolean addConnection(String ip, String name);

    /**
     * Removes a connection from the database and from the system in general
     * @param machineId The id of the desired machine
     * @return Returns true if the connection was successfully removed, returns false if the removal failed.
     */
    boolean removeConnection(String machineId);

    /**
     * Sets the machine in the autobrewing mode so it brews from the queue.
     * @param machineId The id of the machine
     */
    void startAutoBrew(String machineId);

    /**
     * Stops the machine from autobrewing
     * @param machineId The id of the machine
     */
    void stopAutoBrew(String machineId);
}
