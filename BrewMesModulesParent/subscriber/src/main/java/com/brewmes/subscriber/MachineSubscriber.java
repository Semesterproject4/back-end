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
import java.util.concurrent.ExecutionException;


@Service
public class MachineSubscriber implements ISubscribeService {
    private final Map<String, Thread> activeSubscriptions = new HashMap<>();
    private final Map<String, Subscription> activeThreads = new HashMap<>();

    @Autowired
    private ConnectionRepository connectionRepo;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public boolean subscribeToMachineValues(String connectionID) {
//        Optional<Connection> connection = connectionRepo.findById(connectionID);

        Connection connection = new Connection();
        connection.setIp(connectionID);

        try {
//            if (connection.isPresent()) {
                if (!activeSubscriptions.containsKey(connectionID)) {
                    Subscription subscription = null;
                    subscription = new Subscription(connection, batchRepository);
                    Thread thread = new Thread(subscription);
                    thread.start();
                    activeSubscriptions.put(connectionID, thread);
                    activeThreads.put(connectionID, subscription);
//                } else {
//                    Thread thread = activeSubscriptions.get(connectionID);
//                    if (thread.isInterrupted()) {
//                        Subscription subscription = new Subscription(connection.get(), batchRepository);
//                        thread = new Thread(subscription);
//                        thread.start();
//                        activeSubscriptions.put(connectionID, thread);
//                        activeThreads.put(connectionID, subscription);
//                    }
                }
                return true;
//            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public MachineData getLatestMachineData(String connectionID) {
        Subscription subscription = activeThreads.get(connectionID);
        return subscription.getLatestMachineData();

//        MachineData data;
//        if (dataList != null && !dataList.isEmpty()) {
//            data = dataList.get(dataList.size() - 1);
//        } else {
//            data = null;
//        }
//        return data;
    }
}
