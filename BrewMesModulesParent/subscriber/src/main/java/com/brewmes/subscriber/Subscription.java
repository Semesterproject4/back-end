package com.brewmes.subscriber;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.Connection;
import com.brewmes.common.entities.MachineData;
import com.brewmes.subscriber.util.CommandNodes;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedEventItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Subscription implements Runnable {
    private final CompletableFuture<OpcUaClient> future = new CompletableFuture<>();
    private static final Logger LOGGER = Logger.getLogger("com.brewmes");
    private OpcUaClient client;
    private final Connection connection;
    private MachineData latestMachineData;

    public Subscription(Connection connection) throws ExecutionException, InterruptedException {
//        try {
        //ip of client
//            this.client = OpcUaClient.create(connection.getIp());
        this.connection = connection;
//        } catch (UaException e) {
//            e.printStackTrace();
//        }

    }

    public static void onSubscriptionValue(UaMonitoredItem item, DataValue value) {

    }


    private static void setValues(UaMonitoredItem item, DataValue value, Batch batchData) {

    }

    public static void onConstantSubscriptionValue(UaMonitoredItem item, DataValue value) {

    }

    private static String getFormat(int val, String name) {
        return null;
    }

    public static void main(String[] args) {
        Connection connectionTest = new Connection("opc.tcp://127.0.0.1:4840", "Tester");
        Subscription subscription = null;

        try {
            subscription = new Subscription(connectionTest);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(subscription);
        thread.start();


    }

    public void subscribe(CompletableFuture<OpcUaClient> future) {
        try {
            //get all endpoints from the machine
            List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:4840").get();

            String[] ipAddressArray = connection.getIp().split(":");
            EndpointDescription configPoint = EndpointUtil.updateUrl(endpoints.get(0), ipAddressArray[1].substring(2), Integer.parseInt(ipAddressArray[2]));

            //loading endpoints into configuration
            OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
            cfg.setEndpoint(configPoint);

            //setting up machine client with config
            OpcUaClient client = OpcUaClient.create(cfg.build());

            // what to read
            client.connect().get();


            ManagedSubscription subscription = ManagedSubscription.create(client);


            subscription.addDataChangeListener((managedDataItems, dataValues) -> {
                for (int i = 0; i < managedDataItems.size(); i++) {
                    LOGGER.info("new value = " + dataValues.get(i).getValue());
                }
            });

            ManagedDataItem dataItem = subscription.createDataItem(CommandNodes.SET_MACHINE_SPEED.nodeId);

            client.getSubscriptionManager().createSubscription(1000).get();

            client.getSubscriptionManager().addSubscriptionListener(new UaSubscriptionManager.SubscriptionListener() {

            });
            if (dataItem.getStatusCode().isGood()) {
                LOGGER.info("item created for nodeId={}" + dataItem.getNodeId());

                // let the example run for 5 seconds before completing
//                Thread.sleep(60_000);
            }


        } catch (UaException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void run() {
        subscribe(future);
    }

    public MachineData getLatestMachineData() {
        return latestMachineData;
    }
}