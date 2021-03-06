package com.brewmes.common.services;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.MachineState;
import com.brewmes.common.util.Products;

import java.util.List;

public interface IMachineService {

    /**
     * Takes a command and sends it to the desired machine
     *
     * @param command   The {@code command} that should be sent to the machine
     * @param connectionID The ID of the machine that should receive the {@code command}
     * @return Returns {@code true} if the control {@code command} was successfully sent, returns {@code false} if the control {@code command} failed
     */
    boolean controlMachine(Command command, String connectionID);

    /**
     * Sets the desired values for a production
     *
     * @param speed     The desired speed that the production should run at
     * @param beerType  The desired beer type of the production
     * @param batchSize The desired amount of beer to be produced
     * @param connectionID The ID of the desired machine the values should be set on
     * @return Returns {@code true} if the variables were successfully set, returns {@code false} if the setting of the variables failed
     */
    boolean setMachineVariables(double speed, Products beerType, int batchSize, String connectionID);

    /**
     * Gets all the connections from the database
     *
     * @return The list of {@code Connection} objects gotten from the database
     */
    List<Connection> getConnections();

    /**
     * Gets the specified connection from the database
     *
     * @param connectionID ID of the desired {@code Connection}
     * @return The {@code Connection} object associated with the given ID; {@code null} if no {@code Connection} was found
     */
    Connection getConnection(String connectionID);


    /**
     * Gets a list of products, so the max speed can be easily read
     *
     * @return a {@code List} of {@code Products} if the request was successful; {@code null} if not
     */
    List<Products> getProducts();

    /**
     * Gets a list of machine states, so the string can be mapped to an integer
     *
     * @return a {@code List} of {@code MachineStates} if the request was successful; {@code null} if not
     */
    List<MachineState> getStates();

    /**
     * Adds a connection to a Machine
     *
     * @param connection The {@code Connection Object} representation of the machine
     * @return {@code true} if the connection was successfully made; {@code false} if the connection failed
     */
    boolean addConnection(Connection connection);

    /**
     * Removes a connection from the database and from the system in general
     *
     * @param connectionID The ID of the desired machine
     * @return {@code true} if the connection was successfully removed, returns {@code false} if the removal failed
     */
    boolean removeConnection(String connectionID);

    /**
     * Sets the machine in the autobrewing mode so it brews from the queue
     *
     * @param connectionID The ID of the machine
     * @return Returns {@code true} if the autobrewing was successfully started, returns {@code false} if the autobrew starting failed
     */
    boolean startAutoBrew(String connectionID);

    /**
     * Stops the machine from autobrewing
     *
     * @param connectionID The ID of the machine
     * @return Returns {@code true} if the autobrewing was successfully stopped, returns {@code false} if the autobrew stop failed
     */
    boolean stopAutoBrew(String connectionID);
}
