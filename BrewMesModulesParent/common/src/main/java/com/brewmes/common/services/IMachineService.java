package com.brewmes.common.services;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;

import java.util.List;

public interface IMachineService {

    /**
     * Takes a command and sends it to the desired machine
     *
     * @param command   The command that should be sent to the machine
     * @param machineID The ID of the machine that should receive the command
     * @return Returns {@code true} if the control command was successfully sent, returns {@code false} if the control command failed
     */
    boolean controlMachine(Command command, String machineID);

    /**
     * Sets the desired values for a production
     *
     * @param speed     The desired speed that the production should run at
     * @param beerType  The desired beer type of the production
     * @param batchSize The desired amount of beer to be produced
     * @param machineID The ID of the desired machine the values should be set on
     * @return Returns {@code true} if the variables were successfully set, returns {@code false} if the setting of the variables failed
     */
    boolean setVariables(double speed, Products beerType, int batchSize, String machineID);

    /**
     * Gets all the connections from the database
     *
     * @return The list of {@code Connection} objects gotten from the database
     */
    List<Connection> getConnections();

    /**
     * Gets the specified connection from the database.
     *
     * @param machineID ID of the desired {@code Connection}.
     * @return The {@code Connection} object associated with the given ID; {@code null} if no {@code Connection} was found.
     */
    Connection getConnection(String machineID);

    /**
     * Adds a connection to a Machine
     *
     * @param connection The {@code Connection Object} representation of the machine
     * @return Returns {@code true} if the connection was successfully made, returns {@code false} if the connection failed
     */
    boolean addConnection(Connection connection);

    /**
     * Removes a connection from the database and from the system in general
     *
     * @param machineID The ID of the desired machine
     * @return Returns true if the connection was successfully removed, returns false if the removal failed
     */
    boolean removeConnection(String machineID);

    /**
     * Sets the machine in the autobrewing mode so it brews from the queue
     *
     * @param machineID The ID of the machine
     * @return Returns {@code true} if the autobrewing was successfully started, returns {@code false} if the autobrew starting failed
     */
    boolean startAutoBrew(String machineID);

    /**
     * Stops the machine from autobrewing
     *
     * @param machineID The ID of the machine
     * @return Returns {@code true} if the autobrewing was successfully stopped, returns {@code false} if the autobrew stop failed
     */
    boolean stopAutoBrew(String machineID);
}
