package com.brewmes.machine;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.services.IMachineService;
import com.brewmes.common.services.IScheduleService;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common.util.Command;
import com.brewmes.common.util.Products;
import com.brewmes.common.util.machinenodes.CommandNodes;
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

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MachineServiceImpl implements IMachineService {
    private static Logger logger = Logger.getLogger(MachineServiceImpl.class.getName());

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired(required = false)
    private ISubscribeService subscribeService;

    @Autowired(required = false)
    private IScheduleService scheduleService;

    private Map<String, Thread> autobrewers = new HashMap<>();
    private Map<String, OpcUaClient> opcUaClients = new HashMap<>();


    //If the program is restarted manually or due to a crash this method will assure that machines which were autobrewing before start doing it again
    @PostConstruct
    private void initializeAutoBrew() {
        List<Connection> list = getConnections();

        for (Connection connection : list) {
            if (connection.isAutobrewing()) {
                startAutoBrew(connection.getId());
            }
        }
    }

    private void connectToMachine(String connectionID) {
        if (!opcUaClients.containsKey(connectionID)) {
            //Get connection object from database based on id
            Connection connection = getConnection(connectionID);

            //Create and add connection to the map
            try {
                opcUaClients.put(connectionID, getOpcUaClient(connection.getIp()));
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException | UaException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
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

    private void sendChanges(String connectionID) {
        try {
            NodeId cmdChangeRequest = CommandNodes.EXECUTE_MACHINE_COMMAND.nodeId;
            opcUaClients.get(connectionID).writeValue(cmdChangeRequest, DataValue.valueOnly(new Variant(true))).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean controlMachine(Command command, String connectionID) {
        connectToMachine(connectionID);

        try {
            NodeId cntrlCmd = CommandNodes.SET_MACHINE_COMMAND.nodeId;
            opcUaClients.get(connectionID).writeValue(cntrlCmd, DataValue.valueOnly(new Variant(command.label))).get();
            sendChanges(connectionID);
            return true;
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean setMachineVariables(double speed, Products beerType, int batchSize, String connectionID) {
        connectToMachine(connectionID);
        try {
            NodeId setBeerType = CommandNodes.SET_PRODUCT_ID_FOR_NEXT_BATCH.nodeId;
            opcUaClients.get(connectionID).writeValue(setBeerType, DataValue.valueOnly(new Variant((float) beerType.productType))).get();
            NodeId setSpeed = CommandNodes.SET_MACHINE_SPEED.nodeId;
            opcUaClients.get(connectionID).writeValue(setSpeed, DataValue.valueOnly(new Variant((float) speed))).get();
            NodeId setBatchSize = CommandNodes.SET_PRODUCT_AMOUNT_IN_NEXT_BATCH.nodeId;
            opcUaClients.get(connectionID).writeValue(setBatchSize, DataValue.valueOnly(new Variant((float) batchSize))).get();
            return true;
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Connection> getConnections() {
        return connectionRepository.findAll();
    }

    @Override
    public Connection getConnection(String connectionID) {
        Optional<Connection> connectionOptional = connectionRepository.findById(connectionID);

        return connectionOptional.orElse(null);
    }

    @Override
    public List<Products> getProducts() {
        return new ArrayList<>(Arrays.asList(Products.values()));
    }

    @Override
    public boolean addConnection(Connection connection) {
        Connection connectionReturned = connectionRepository.insert(connection);

        return connectionReturned.getId() != null;
    }

    @Override
    public boolean removeConnection(String connectionID) {
        connectionRepository.deleteById(connectionID);

        return connectionRepository.findById(connectionID).isEmpty();
    }

    @Override
    public boolean startAutoBrew(String connectionID) {
        connectToMachine(connectionID);

        Runnable task = new AutobrewRunner(connectionID, subscribeService, scheduleService, this);
        Thread thread = new Thread(task);

        thread.setDaemon(true);
        thread.start();
        autobrewers.put(connectionID, thread);

        Connection connection = getConnection(connectionID);
        connection.setAutobrew(true);
        connection = connectionRepository.save(connection);


        return connection.isAutobrewing();
    }

    @Override
    public boolean stopAutoBrew(String connectionID) {
        connectToMachine(connectionID);

        autobrewers.get(connectionID).interrupt();
        autobrewers.remove(connectionID);

        Connection connection = getConnection(connectionID);
        connection.setAutobrew(false);
        connection = connectionRepository.save(connection);

        return !connection.isAutobrewing();
    }

    public void setOpcUaClients(Map<String, OpcUaClient> opcUaClients) {
        this.opcUaClients = opcUaClients;
    }
}
