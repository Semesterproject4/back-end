package com.brewmes.subscriber;

import com.brewmes.common.entities.Connection;
import com.brewmes.common.entities.MachineData;
import com.brewmes.subscriber.util.AdminNodes;
import com.brewmes.subscriber.util.CommandNodes;
import com.brewmes.subscriber.util.MachineNodes;
import com.brewmes.subscriber.util.StatusNodes;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Subscription implements Runnable {
    private static final Logger LOGGER = Logger.getLogger("com.brewmes");
    private final List<ManagedDataItem> dataItems = new ArrayList<>();
    private final Connection connection;
    private boolean alive;
    private OpcUaClient client;
    private ManagedSubscription managedSubscription;
    private MachineData latestMachineData;
    private UaSubscriptionManager.SubscriptionListener subListener;

    public Subscription(Connection connection) throws ExecutionException, InterruptedException {
//        try {
        //ip of client
//            this.client = OpcUaClient.create(co nnection.getIp());
        this.connection = connection;
        alive = true;
//        } catch (UaException e) {
//            e.printStackTrace();
//        }
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

    public void subscribe() {
        try {
            this.managedSubscription = getManagedSubscription();

            UaSubscriptionManager subscriptionManager = client.getSubscriptionManager();
            subscriptionManager.addSubscriptionListener(setupSubListener());

            managedSubscription.addDataChangeListener(this::onDataChanged);

            createMonitoredItems(managedSubscription);

            for (ManagedDataItem data : this.dataItems) {
                if (data.getStatusCode().isGood()) {
                    LOGGER.info("item created for nodeId={}" + data.getNodeId());
                }
            }

            while (alive) {
                Thread.sleep(2000);
            }


        } catch (UaException | InterruptedException e) {
            alive = false;
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private ManagedSubscription getManagedSubscription() throws UaException, ExecutionException, InterruptedException {
        //get all endpoints from the machine
        List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints(connection.getIp()).get();

        String[] ipAddressArray = connection.getIp().split(":");
        EndpointDescription configPoint = EndpointUtil.updateUrl(endpoints.get(0), ipAddressArray[1].substring(2), Integer.parseInt(ipAddressArray[2]));

        //loading endpoints into configuration
        OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
        cfg.setEndpoint(configPoint);

        //setting up machine client with config
        OpcUaClient client = OpcUaClient.create(cfg.build());

        // what to read
        client.connect().get();

        this.client = client;

        return ManagedSubscription.create(client, 500);
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void run() {
        subscribe();
    }

    public MachineData getLatestMachineData() {
        return latestMachineData;
    }

    /**
     * Adds all relevant endpoints as {@code ManagedDataItem} to a local list of all endpoints the {@code ManagedSubscription} should subscribe to.
     * @param subscription The {@code ManagedSubscription} to add the
     * @throws UaException
     */
    private void createMonitoredItems(ManagedSubscription subscription) throws UaException {
        //command nodes
        dataItems.add(subscription.createDataItem(CommandNodes.SET_MACHINE_SPEED.nodeId));
        dataItems.add(subscription.createDataItem(CommandNodes.SET_NEXT_BATCH_ID.nodeId));
        dataItems.add(subscription.createDataItem(CommandNodes.SET_MACHINE_COMMAND.nodeId));
        dataItems.add(subscription.createDataItem(CommandNodes.SET_PRODUCT_ID_FOR_NEXT_BATCH.nodeId));
        dataItems.add(subscription.createDataItem(CommandNodes.SET_PRODUCT_AMOUNT_IN_NEXT_BATCH.nodeId));

        //status nodes
        dataItems.add(subscription.createDataItem(StatusNodes.CURRENT_BATCH_ID.nodeId));
        dataItems.add(subscription.createDataItem(StatusNodes.HUMIDITY.nodeId));
        dataItems.add(subscription.createDataItem(StatusNodes.MACHINE_SPEED.nodeId));
        dataItems.add(subscription.createDataItem(StatusNodes.MACHINE_STATE.nodeId));
        dataItems.add(subscription.createDataItem(StatusNodes.NORMALIZED_MACHINE_SPEED.nodeId));
        dataItems.add(subscription.createDataItem(StatusNodes.PRODUCTS_IN_CURRENT_BATCH.nodeId));
        dataItems.add(subscription.createDataItem(StatusNodes.TEMPERATURE.nodeId));
        dataItems.add(subscription.createDataItem(StatusNodes.VIBRATION.nodeId));

        //admin nodes
        dataItems.add(subscription.createDataItem(AdminNodes.DEFECTIVE_PRODUCTS.nodeId));
        dataItems.add(subscription.createDataItem(AdminNodes.PRODUCED_PRODUCTS.nodeId));

        //machine nodes(ingredients & maintenance)
        dataItems.add(subscription.createDataItem(MachineNodes.MAINTENANCE.nodeId));
        dataItems.add(subscription.createDataItem(MachineNodes.YEAST.nodeId));
        dataItems.add(subscription.createDataItem(MachineNodes.BARLEY.nodeId));
        dataItems.add(subscription.createDataItem(MachineNodes.WHEAT.nodeId));
        dataItems.add(subscription.createDataItem(MachineNodes.HOPS.nodeId));
        dataItems.add(subscription.createDataItem(MachineNodes.MALT.nodeId));

    }

    /**
     * the callback method to be registered as listener.
     * It sets the changed values on the respective fields on the associated {@Code MachineData} object.
     * @param managedDataItems A {@Code List} of the changed items.
     * @param dataValues A {@Code List} of the changed values.
     */
    private void onDataChanged(List<ManagedDataItem> managedDataItems, List<DataValue> dataValues) {
        for (int i = 0; i < managedDataItems.size(); i++) {
            LOGGER.info("new value = " + dataValues.get(i).getValue() + " " + managedDataItems.get(i).getNodeId());
        }
    }

    private UaSubscriptionManager.SubscriptionListener setupSubListener() {
        return new UaSubscriptionManager.SubscriptionListener() {
            @Override
            public void onSubscriptionTransferFailed(UaSubscription subscription, StatusCode statusCode) {
                UaSubscriptionManager.SubscriptionListener.super.onSubscriptionTransferFailed(subscription, statusCode);
                LOGGER.info(subscription.toString() + " " + statusCode.getValue());
                setUpSubscription();
            }
        };
    }

    private void setUpSubscription() {
        try {
            this.managedSubscription = getManagedSubscription();
            createMonitoredItems(this.managedSubscription);
            this.managedSubscription.addDataChangeListener(this::onDataChanged);
        } catch (UaException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}