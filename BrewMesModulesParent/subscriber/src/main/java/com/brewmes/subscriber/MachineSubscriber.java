package com.brewmes.subscriber;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.ISubscribeService;
import com.brewmes.common_repository.BatchRepository;
import com.brewmes.common_repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class MachineSubscriber implements ISubscribeService {
    protected final Map<String, Thread> activeThreads = new HashMap<>();
    protected final Map<String, Runnable> activeSubscriptions = new HashMap<String, Runnable>();

    @Autowired
    private ConnectionRepository connectionRepo;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public boolean subscribeToMachineValues(String connectionID) {
        Optional<Connection> connection = connectionRepo.findById(connectionID);

        if (connection.isPresent()) {
            if (!activeThreads.containsKey(connectionID)) {
                createSubscription(connectionID, connection.get());
            } else {
                if (activeThreads.get(connectionID).isInterrupted()) {
                    createSubscription(connectionID, connection.get());
                }
            }
            return true;
        }
        return false;
    }

    protected void createSubscription(String connectionID, Connection connection) {
        Subscription subscription = new Subscription(connection, batchRepository);
        Thread thread = new Thread(subscription);
        thread.start();
        activeThreads.put(connectionID, thread);
        activeSubscriptions.put(connectionID, subscription);
    }

    @Override
    public MachineData getLatestMachineData(String connectionID) {
        Subscription subscription = (Subscription) activeSubscriptions.get(connectionID);
        if (subscription != null) {
            return subscription.getLatestMachineData();
        } else {
            return null;
        }
    }
}