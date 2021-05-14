package com.brewmes.subscriber;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.Connection;
import com.brewmes.common.entities.Ingredients;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.util.MachineState;
import com.brewmes.common.util.machinenodes.AdminNodes;
import com.brewmes.common.util.machinenodes.CommandNodes;
import com.brewmes.common.util.machinenodes.MachineNodes;
import com.brewmes.common.util.machinenodes.StatusNodes;
import com.brewmes.common_repository.BatchRepository;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Subscription implements Runnable {
    private static final Logger LOGGER = Logger.getLogger("com.brewmes.subscription");
    private final List<ManagedDataItem> dataItems = new ArrayList<>();
    private final Connection connection;
    private final BatchRepository batchRepository;
    private OpcUaClient client;
    private ManagedSubscription managedSubscription;
    private MachineData latestMachineData;
    private Batch currentBatch;
    private Ingredients currentIngredients;
    private float desiredSpeed;

    public Subscription(Connection connection, BatchRepository batchRepository) {
        this.connection = connection;
        this.batchRepository = batchRepository;
        this.desiredSpeed = -1;
    }

    @Override
    public void run() {
        subscribe();
    }


    private void subscribe() {
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

        } catch (UaException | InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        } catch (ExecutionException e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
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
        this.client = (OpcUaClient) OpcUaClient.create(cfg.build()).connect().get();

        return ManagedSubscription.create(client, 500);
    }


    public MachineData getLatestMachineData() {
        return latestMachineData;
    }

    /**
     * Adds all relevant endpoints as {@code ManagedDataItem} to a local list of all endpoints the {@code ManagedSubscription} should subscribe to.
     *
     * @param subscription The {@code ManagedSubscription} to add the
     * @throws UaException
     */
    private void createMonitoredItems(ManagedSubscription subscription) throws UaException {
        //command nodes
        dataItems.add(subscription.createDataItem(CommandNodes.SET_MACHINE_SPEED.nodeId));
        dataItems.add(subscription.createDataItem(CommandNodes.SET_PRODUCT_ID_FOR_NEXT_BATCH.nodeId));
        dataItems.add(subscription.createDataItem(CommandNodes.SET_PRODUCT_AMOUNT_IN_NEXT_BATCH.nodeId));

        //status nodes
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
        dataItems.add(subscription.createDataItem(AdminNodes.BATCH_PRODUCT_ID.nodeId));

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
     * It sets the changed values on the respective fields on the associated {@code MachineData} object.
     *
     * @param managedDataItems A {@code List} of the changed items.
     * @param dataValues       A {@code List} of the changed values.
     */
    private void onDataChanged(List<ManagedDataItem> managedDataItems, List<DataValue> dataValues) {
        // Check if latestMachineData exists
        if (this.latestMachineData == null) {
            this.latestMachineData = new MachineData();
            this.currentIngredients = new Ingredients();
            this.latestMachineData.setIngredients(this.currentIngredients);
        }


        // Runs through the all machineData being subscribed to and adds them to latestMachineData
        for (int i = 0; i < managedDataItems.size(); i++) {
            String id = managedDataItems.get(i).getNodeId().toParseableString();

            // admin nodes
            if (AdminNodes.DEFECTIVE_PRODUCTS.nodeId.toParseableString().equals(id)) {
                latestMachineData.setDefectProducts((int) dataValues.get(i).getValue().getValue());
            } else if (AdminNodes.PRODUCED_PRODUCTS.nodeId.toParseableString().equals(id)) {
                int processed = (int) dataValues.get(i).getValue().getValue();
                latestMachineData.setProcessed(processed);
                latestMachineData.setAcceptableProducts(processed - latestMachineData.getDefectProducts());
            }
            // Status nodes
            else if (StatusNodes.VIBRATION.nodeId.toParseableString().equals(id)) {
                latestMachineData.setVibration((float) dataValues.get(i).getValue().getValue());
            } else if (StatusNodes.TEMPERATURE.nodeId.toParseableString().equals(id)) {
                latestMachineData.setTemperature((float) dataValues.get(i).getValue().getValue());
            } else if (StatusNodes.HUMIDITY.nodeId.toParseableString().equals(id)) {
                latestMachineData.setHumidity((float) dataValues.get(i).getValue().getValue());
            } else if (StatusNodes.NORMALIZED_MACHINE_SPEED.nodeId.toParseableString().equals(id)) {
                latestMachineData.setNormSpeed((float) dataValues.get(i).getValue().getValue());
            } else if (StatusNodes.MACHINE_STATE.nodeId.toParseableString().equals(id)) {
                MachineState state = MachineState.valueOf(dataValues.get(i).getValue().getValue().toString());
                latestMachineData.setState(state);
                // if state is == 6. Make a new batch and save.
                if (state == MachineState.EXECUTE) {
                    this.currentBatch = new Batch();
                    this.currentBatch.setData(new ArrayList<>());
                    this.currentBatch.setDesiredSpeed(this.desiredSpeed);

                    // if state is == 17. Save batch and remove the object reference.
                } else if ((state == MachineState.COMPLETE || state == MachineState.ABORTED || state == MachineState.STOPPED) && currentBatch != null) {
                    this.saveBatch();
                    this.currentBatch = null;
                }
                // status nodes written to batch, if it exists.
            } else if (StatusNodes.PRODUCTS_IN_CURRENT_BATCH.nodeId.toParseableString().equals(id) && currentBatch != null) {
                this.currentBatch.setAmountToProduce(Math.round((float) dataValues.get(i).getValue().getValue()));
            } else if (StatusNodes.MACHINE_SPEED.nodeId.toParseableString().equals(id)) {
                float speed = (float) dataValues.get(i).getValue().getValue();
                if (speed > 0) {
                    this.desiredSpeed = speed;
                }
            } else if (MachineNodes.MALT.nodeId.toParseableString().equals(id)) {
                this.currentIngredients.setMalt((float) dataValues.get(i).getValue().getValue());
            } else if (MachineNodes.WHEAT.nodeId.toParseableString().equals(id)) {
                this.currentIngredients.setWheat((float) dataValues.get(i).getValue().getValue());
            } else if (MachineNodes.HOPS.nodeId.toParseableString().equals(id)) {
                this.currentIngredients.setHops((float) dataValues.get(i).getValue().getValue());
            } else if (MachineNodes.BARLEY.nodeId.toParseableString().equals(id)) {
                this.currentIngredients.setBarley((float) dataValues.get(i).getValue().getValue());
            } else if (MachineNodes.YEAST.nodeId.toParseableString().equals(id)) {
                this.currentIngredients.setYeast((float) dataValues.get(i).getValue().getValue());
            } else if (MachineNodes.MAINTENANCE.nodeId.toParseableString().equals(id)) {
                UShort maintenance = (UShort) dataValues.get(i).getValue().getValue();
                this.latestMachineData.setMaintenance(maintenance.doubleValue());
            }
            // Command nodes to batch
            else if (AdminNodes.BATCH_PRODUCT_ID.nodeId.toParseableString().equals(id) && currentBatch != null) {
                this.currentBatch.setProductType(Math.round((float) dataValues.get(i).getValue().getValue()));
            }
        }

        if (this.currentBatch != null) {
            this.saveBatch();
        }
    }

    private void saveBatch() {
        this.latestMachineData.setTimestamp(LocalDateTime.now().plusHours(2));
        this.latestMachineData.setIngredients(new Ingredients((currentIngredients)));
        this.currentBatch.addMachineData(new MachineData(latestMachineData));
        this.currentBatch = this.batchRepository.save(this.currentBatch);
    }

    private UaSubscriptionManager.SubscriptionListener setupSubListener() {
        return new UaSubscriptionManager.SubscriptionListener() {
            @Override
            public void onSubscriptionTransferFailed(UaSubscription subscription, StatusCode statusCode) {
                UaSubscriptionManager.SubscriptionListener.super.onSubscriptionTransferFailed(subscription, statusCode);
                setUpSubscription();
            }
        };
    }

    private void setUpSubscription() {
        try {
            this.managedSubscription = getManagedSubscription();
            this.createMonitoredItems(this.managedSubscription);
            this.managedSubscription.addDataChangeListener(this::onDataChanged);
        } catch (UaException | ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }
    }
}