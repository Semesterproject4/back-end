package com.brewmes.demo.model;

import com.brewmes.demo.Persistence.BatchRepository;
import com.brewmes.demo.Persistence.MachineRepository;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
//@Transactional
public class BrewMES implements IBrewMES {

    //Repository injected by Spring
    @Autowired
    private MachineRepository machineRepo;
    @Autowired
    private BatchRepository batchRepo;
    private Map<UUID, Machine> machines;
    private Thread batchSaveThread;

    public Batch getBatch(UUID id) {
        Batch batch = batchRepo.findById(id).orElse(null);

        if (batch != null) {
            batch.setAverages();
            batch.setMaxes();
            batch.setMinimums();
        }

        return batch;
    }

    public void generateReport(Batch batch) {
        Report.generatePDF(batch);
    }

    /**
     * Connects a machine to our OPC-UA client and saves it in the machines map as well as in the database.
     *
     * @param ipAddress a String representation of the ip of the machine you wish to connect.
     */
    public boolean connectMachine(String ipAddress) {
        try {
            //get all endpoints from the machine
            OpcUaClient connection = getOpcUaClient(ipAddress);
            if (machines == null) {
                machines = new HashMap<>();
            }
            Machine newMachine = new Machine(ipAddress, connection);
            machineRepo.save(newMachine);
            machines.put(newMachine.getId(), newMachine);
            makeBatchSaveThread();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (UaException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean connectMachineWithID(String ipAddress, UUID id) {
        try {
            OpcUaClient connection = getOpcUaClient(ipAddress);
            if (machines == null) {
                machines = new HashMap<>();
            }
            Machine newMachine = new Machine(ipAddress, connection, id);
            machineRepo.save(newMachine);
            machines.put(newMachine.getId(), newMachine);
            makeBatchSaveThread();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (UaException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, Object> getBatchesPage(int page, int size) {
        Pageable paging = PageRequest.of(page, size);

        Page<Batch> pageBatch = batchRepo.findAll(paging);

        List<Batch> batches = pageBatch.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("batches", batches);
        response.put("currentPage", pageBatch.getNumber());
        response.put("totalItems", pageBatch.getTotalElements());
        response.put("totalPages", pageBatch.getTotalPages());

        return response;
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
        OpcUaClient connection = OpcUaClient.create(cfg.build());

        //connecting machine
        connection.connect().get();
        return connection;
    }

    /**
     * Removes and deletes from both the machine map and the database.
     *
     * @param id the UUID of the machine you wish to delete.
     */
    public void disconnectMachine(UUID id) {
        machines.remove(id);
        machineRepo.deleteById(id);
    }

    /**
     * Makes a single thread that loops through all the machines and saves their batch to the database if the state of the machine is 17
     */
    private void makeBatchSaveThread() {
        if (batchSaveThread == null) {

            batchSaveThread = new Thread(() -> {
                while (true) {
                    for (Machine machine : machines.values()) {
                        if (machine.getCurrentState() == 17 && machine.getCurrentBatch() != null && !machine.getCurrentBatch().isSaved()) {
                            machine.setCurrentBatch(batchRepo.save(machine.getCurrentBatch()));
                            machine.getCurrentBatch().setSaved(true);
                        }
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        batchSaveThread = null;
                        e.printStackTrace();
                    }
                }
            });

            batchSaveThread.setDaemon(true);
            batchSaveThread.start();
        }
    }

    public void setMachineVariables(double speed, String beerType, int batchSize, UUID id) {
        machines.get(id).setVariables(speed, BeerType.valueOf(beerType.toUpperCase()), batchSize);
    }

    //Parsing the command to the current selected machine.
    public void controlMachine(String command, UUID id) {
        machines.get(id).controlMachine(Command.valueOf(command.toUpperCase()));
    }

    /**
     * Finds all machines in the database and updates the Map machines.
     *
     * @return the updated map of machines.
     */
    @Override
    public Map<UUID, Machine> getMachines() {
        if (machines == null) {
            machines = new HashMap<>();
        }
        machineRepo.findAll().forEach(machine -> {
            if (!machines.containsKey(machine.getId())) {
                connectMachineWithID(machine.getIp(), machine.getId());
            }
        });

        return machines;
    }
}
