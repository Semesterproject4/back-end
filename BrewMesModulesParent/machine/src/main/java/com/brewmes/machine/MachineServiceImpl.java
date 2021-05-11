package com.brewmes.machine;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;
import com.brewmes.common_repository.ConnectionRepository;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class MachineServiceImpl implements IMachineService {
    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    ISubscribeService subscribeService;
    Map<String, Thread> autobrewers = new HashMap<>();
    private Connection currentConnection;
    private OpcUaClient client;
    private boolean autobrew = false;

    private OpcUaClient connectToMachine(String id) {
        //check id against currentConnection
        if (currentConnection.getId().equals(id)) {
            return client;
        } else {
            //Get connection object from database based on id
            this.currentConnection = getConnection(id);

            //create opcua connection to that machine
            try {
                client = getOpcUaClient(currentConnection.getIp());
                return client;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return null;
            } catch (ExecutionException | UaException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private OpcUaClient getOpcUaClient(String ipAddress) throws InterruptedException, ExecutionException, UaException {
        //get all endpoints from the machine
        List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints(ipAddress).get();

        String[] ipAddressArray = ipAddress.split(":");
        EndpointDescription configPoint = EndpointUtil.updateUrl(endpoints.get(0), ipAddressArray[1].substring(2), Integer.parseInt(ipAddressArray[2]));

        //loading endpoints into configuration
        OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
        cfg.setEndpoint(configPoint);

        //setting up machine client with config
        OpcUaClient opcUaClient = OpcUaClient.create(cfg.build());

        //connecting machine
        opcUaClient.connect().get();
        return opcUaClient;
    }

    private void sendChanges() {
        try {
            NodeId cmdChangeRequest = new NodeId(6, "::Program:Cube.Command.CmdChangeRequest");
            client.writeValue(cmdChangeRequest, DataValue.valueOnly(new Variant(true))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Takes a command and sends it to the desired machine
     *
     * @param command      The {@code command} that should be sent to the machine
     * @param connectionID The ID of the machine that should receive the {@code command}
     * @return Returns {@code true} if the control {@code command} was successfully sent, returns {@code false} if the control {@code command} failed
     */
    @Override
    public boolean controlMachine(Command command, String connectionID) {
        connectToMachine(connectionID);

        try {
            NodeId cntrlCmd = new NodeId(6, "::Program:Cube.Command.CntrlCmd");
            client.writeValue(cntrlCmd, DataValue.valueOnly(new Variant(command.label))).get();
            sendChanges();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets the desired values for a production
     *
     * @param speed        The desired speed that the production should run at
     * @param beerType     The desired beer type of the production
     * @param batchSize    The desired amount of beer to be produced
     * @param connectionID The ID of the desired machine the values should be set on
     * @return Returns {@code true} if the variables were successfully set, returns {@code false} if the setting of the variables failed
     */
    @Override
    public boolean setVariables(double speed, Products beerType, int batchSize, String connectionID) {
        connectToMachine(connectionID);
        try {
            NodeId setBeerType = new NodeId(6, "::Program:Cube.Command.Parameter[1].Value");
            client.writeValue(setBeerType, DataValue.valueOnly(new Variant((float) beerType.productType))).get();

            NodeId setSpeed = new NodeId(6, "::Program:Cube.Command.MachSpeed");
            client.writeValue(setSpeed, DataValue.valueOnly(new Variant((float) speed))).get();

            NodeId setBatchSize = new NodeId(6, "::Program:Cube.Command.Parameter[2].Value");
            client.writeValue(setBatchSize, DataValue.valueOnly(new Variant((float) batchSize))).get();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets all the connections from the database
     *
     * @return The list of {@code Connection} objects gotten from the database
     */
    @Override
    public List<Connection> getConnections() {
        return connectionRepository.findAll();
    }

    /**
     * Gets the specified connection from the database.
     *
     * @param connectionID ID of the desired {@code Connection}.
     * @return The {@code Connection} object associated with the given ID; {@code null} if no {@code Connection} was found.
     */
    @Override
    public Connection getConnection(String connectionID) {
        Optional<Connection> connectionOptional = connectionRepository.findById(connectionID);

        return connectionOptional.orElse(null);
    }

    /**
     * Adds a connection to a Machine
     *
     * @param connection The {@code Connection Object} representation of the machine
     * @return Returns {@code true} if the connection was successfully made, returns {@code false} if the connection failed
     */
    @Override
    public boolean addConnection(Connection connection) {
        Connection connectionReturned = connectionRepository.insert(connection);

        if (connectionReturned.getId() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a connection from the database and from the system in general
     *
     * @param connectionID The ID of the desired machine
     * @return {@code true} if the connection was successfully removed, returns {@code false} if the removal failed
     */
    @Override
    public boolean removeConnection(String connectionID) {
        connectionRepository.deleteById(connectionID);

        return connectionRepository.findById(connectionID).isEmpty();
    }

    /**
     * Sets the machine in the autobrewing mode so it brews from the queue
     *
     * @param connectionID The ID of the machine
     * @return Returns {@code true} if the autobrewing was successfully started, returns {@code false} if the autobrew starting failed
     */
    @Override
    public boolean startAutoBrew(String connectionID) {
        connectToMachine(connectionID);

        Runnable task = new AutobrewRunner(connectionID);
        Thread thread = new Thread(task);

        thread.setDaemon(true);
        thread.start();
        autobrewers.put(connectionID, thread);

        currentConnection.setAutobrew(true);
        currentConnection = connectionRepository.save(currentConnection);

        return currentConnection.isAutobrewing();
    }

    /**
     * Stops the machine from autobrewing
     *
     * @param connectionID The ID of the machine
     * @return Returns {@code true} if the autobrewing was successfully stopped, returns {@code false} if the autobrew stop failed
     */
    @Override
    public boolean stopAutoBrew(String connectionID) {
        connectToMachine(connectionID);

        autobrewers.get(connectionID).interrupt();
        autobrewers.remove(connectionID);

        currentConnection.setAutobrew(false);
        currentConnection = connectionRepository.save(currentConnection);

        return !currentConnection.isAutobrewing();
    }
}
