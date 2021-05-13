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
    private final Map<String, Thread> activeThreads = new HashMap<>();
    private final Map<String, Subscription> activeSubscriptions = new HashMap<>();

    @Autowired
    private ConnectionRepository connectionRepo;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public boolean subscribeToMachineValues(String connectionID) {
        Optional<Connection> connection = connectionRepo.findById(connectionID);

        if (connection.isPresent()) {
            if (!activeThreads.containsKey(connectionID)) {
                Subscription subscription = new Subscription(connection.get(), batchRepository);
                Thread thread = new Thread(subscription);
                thread.start();
                activeThreads.put(connectionID, thread);
                activeSubscriptions.put(connectionID, subscription);
            } else {
                if (activeThreads.get(connectionID).isInterrupted()) {
                    Subscription subscription = new Subscription(connection.get(), batchRepository);
                    Thread thread = new Thread(subscription);
                    thread.start();
                    activeThreads.put(connectionID, thread);
                    activeSubscriptions.put(connectionID, subscription);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public MachineData getLatestMachineData(String connectionID) {
        Subscription subscription = activeSubscriptions.get(connectionID);
        return subscription.getLatestMachineData();
    }
}
