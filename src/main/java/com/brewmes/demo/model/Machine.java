package com.brewmes.demo.model;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.time.temporal.ChronoUnit.SECONDS;

@Entity
@Table(name = "Machine")
public class Machine {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "ip")
    private String ip;

    @Transient
    private OpcUaClient connection;

    @Transient
    private Batch currentBatch;

    @Transient
    private double oee;

    @Transient
    private int currentState;

    @Transient
    private LocalDateTime stateTimestamp;

    @Transient
    private int totalProducts;
    @Transient
    private int acceptableProducts;
    @Transient
    private int defectProducts;
    @Transient
    private int amountToProduce;
    @Transient
    private double temperature;
    @Transient
    private double vibration;
    @Transient
    private double humidity;
    @Transient
    private double barley;
    @Transient
    private double hops;
    @Transient
    private double malt;
    @Transient
    private double wheat;
    @Transient
    private double yeast;
    @Transient
    private UUID batchID;
    @Transient
    private double speed;
    @Transient
    private int beerType;
    @Transient
    private int maintenance;

    public Machine(String ipAddress, OpcUaClient connection) {
        this.id = UUID.randomUUID();
        this.ip = ipAddress;
        this.connection = connection;

        this.stateTimestamp = LocalDateTime.now();
    }

    public Machine(String ipAddress, OpcUaClient connection, UUID id) {
        this.ip = ipAddress;
        this.connection = connection;
        this.id = id;

        this.stateTimestamp = LocalDateTime.now();
    }

    public Machine() {

    }

    /**
     * This sends a command to the connected machine.
     *
     * @param command the enum value to send to the machine.
     */
    public void controlMachine(Command command) {
        try {
            //Create nodeID for Control Command.
            NodeId cntrlCmd = new NodeId(6, "::Program:Cube.Command.CntrlCmd");

            // Switch on the enum, writing different values to the machine.
            connection.writeValue(cntrlCmd, DataValue.valueOnly(new Variant(command.label))).get();

            //request change
            changeRequest();

            //Starts a new batch if the machine is commanded to start a new batch.
            if (command == Command.START && readState() == 4) {
                this.currentBatch = new Batch(UUID.randomUUID());
                this.stateTimestamp = LocalDateTime.now();
            }


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }


    //region READ HELPER METHODS

    /**
     * Reads an integer value from the machine via OPCUA
     *
     * @param ns      is the namespace
     * @param address is the address eg. "::Program:Cube.Status.StateCurrent"
     * @return the integer value if found and -1 otherwise
     */
    private int readInt(int ns, String address) {
        try {
            NodeId nodeId = new NodeId(ns, address);
            DataValue dataValue = connection.readValue(0, TimestampsToReturn.Both, nodeId).get();
            Variant variant = dataValue.getValue();
            return (int) variant.getValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Reads a double value from the machine via OPCUA
     *
     * @param ns      is the namespace
     * @param address is the address eg. "::Program:Cube.Status.StateCurrent"
     * @return the double value if found and -1 otherwise
     */
    private double readDouble(int ns, String address) {
        try {
            NodeId nodeId = new NodeId(ns, address);
            DataValue dataValue = connection.readValue(0, TimestampsToReturn.Both, nodeId).get();
            Variant variant = dataValue.getValue();
            return (float) variant.getValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
    //endregion

    //region READ ADMIN ADDRESSES
    public int readProcessedCount() {
        // Read value of the processed product count
        // Ns: 6
        // ::Program:Cube.Admin.ProdProcessedCount
        return readInt(6, "::Program:Cube.Admin.ProdProcessedCount");
    }

    public int readDefectiveCount() {
        // Read value of the processed product count
        // Ns: 6
        // ::Program:Cube.Admin.ProdDefectiveCount
        return readInt(6, "::Program:Cube.Admin.ProdDefectiveCount");
    }

    public int readStopReason() {
        // Reads id of the stop reason
        // Ns: 6
        // ::Program:Cube.Admin.StopReason.ID
        return readInt(6, "::Program:Cube.Admin.StopReason.ID");
    }

    public int readBatchBeerType() {
        // Reads beer type of current batch
        // Ns: 6
        // ::Program:Cube.Admin.Parameter[0].Value
        return (int) readDouble(6, "::Program:Cube.Admin.Parameter[0].Value");
    }
    //endregion

    //region READ STATUS ADDRESSES
    public int readState() {
        // Read value of the state of the machine
        // Ns: 6
        // ::Program:Cube.Status.StateCurrent
        return readInt(6, "::Program:Cube.Status.StateCurrent");
    }

    public int readMachineSpeed() {
        // Reads the current machine speed
        // Ns: 6
        // ::Program:Cube.Status.MachSpeed
        return (int) readDouble(6, "::Program:Cube.Status.MachSpeed");
    }

    public double readNormalizedMachineSpeed() {
        // Reads the normalized machine speed
        // Ns: 6
        // ::Program:Cube.Status.CurMachSpeed
        return readDouble(6, "::Program:Cube.Status.CurMachSpeed");
    }

    public int readBatchCurrentId() {
        // Reads the current batch id
        // Ns: 6
        // ::Program:Cube.Status.Parameter[0].Value
        return (int) readDouble(6, "::Program:Cube.Status.Parameter[0].Value");
    }

    public int readBatchSize() {
        // Reads the current batch size
        // Ns: 6
        // ::Program:Cube.Status.Parameter[1].Value
        return (int) readDouble(6, "::Program:Cube.Status.Parameter[1].Value");
    }

    public double readHumidity() {
        // Reads the current humidity
        // Ns:
        // ::Program:Cube.Status.Parameter[2].Value
        return readDouble(6, "::Program:Cube.Status.Parameter[2].Value");
    }

    public double readTemperature() {
        // Reads the current temperature
        // Ns:
        // ::Program:Cube.Status.Parameter[3].Value
        return readDouble(6, "::Program:Cube.Status.Parameter[3].Value");
    }

    public double readVibration() {
        // Reads the current vibration
        // Ns:
        // ::Program:Cube.Status.Parameter[4].Value
        return readDouble(6, "::Program:Cube.Status.Parameter[4].Value");
    }
    //endregion

    //region READ INGREDIENT ADDRESSES

    public double readBarley() {
        // Reads the current Barley amount
        // Ns: 6
        // ::Program:Inventory.Barley
        return readDouble(6, "::Program:Inventory.Barley");
    }

    public double readHops() {
        // Reads the current Hops amount
        // Ns: 6
        // ::Program:Inventory.Hops
        return readDouble(6, "::Program:Inventory.Hops");
    }

    public double readMalt() {
        // Reads the current Malt amount
        // Ns: 6
        // ::Program:Inventory.Malt
        return readDouble(6, "::Program:Inventory.Malt");
    }

    public double readWheat() {
        // Reads the current Wheat amount
        // Ns: 6
        // ::Program:Inventory.Wheat
        return readDouble(6, "::Program:Inventory.Wheat");
    }

    public double readYeast() {
        // Reads the current Yeast amount
        // Ns: 6
        // ::Program:Inventory.Yeast
        return readDouble(6, "::Program:Inventory.Yeast");
    }

    //endregion

    public int readMaintenance() {
        // Reads the current maintenance value
        // Ns: 6
        // ::Program:Maintenance.Counter
        try {
            NodeId nodeId = new NodeId(6, "::Program:Maintenance.Counter");
            DataValue dataValue = connection.readValue(0, TimestampsToReturn.Both, nodeId).get();
            Variant variant = dataValue.getValue();
            UShort value = (UShort) variant.getValue();

            return value.intValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * This writes the variables to the machine. The machine will not be started automatically.
     *
     * @param speed     the speed of the machine.
     * @param beerType  the beer type for the machine.
     * @param batchSize the amount of beer to produce until stopping
     */
    public void setVariables(double speed, BeerType beerType, int batchSize) {
        try {
            //Set beertype on the machine
            NodeId setBeerType = new NodeId(6, "::Program:Cube.Command.Parameter[1].Value");
            connection.writeValue(setBeerType, DataValue.valueOnly(new Variant((float) beerType.label))).get();

            //Set speed on the machine
            NodeId setSpeed = new NodeId(6, "::Program:Cube.Command.MachSpeed");
            connection.writeValue(setSpeed, DataValue.valueOnly(new Variant((float) speed))).get();

            //Set batch size on the machine
            NodeId setBatchSize = new NodeId(6, "::Program:Cube.Command.Parameter[2].Value");
            connection.writeValue(setBatchSize, DataValue.valueOnly(new Variant((float) batchSize))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public UUID getId() {
        return id;
    }


    public String getIp() {
        return ip;
    }

    public Batch getCurrentBatch() {
        return currentBatch;
    }

    public void setCurrentBatch(Batch currentBatch) {
        this.currentBatch = currentBatch;
    }


    public double getOee() {
        return oee;
    }

    public void setOee(double oee) {
        this.oee = oee;
    }


    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }


    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }


    public int getAcceptableProducts() {
        return acceptableProducts;
    }

    public void setAcceptableProducts(int acceptableProducts) {
        this.acceptableProducts = acceptableProducts;
    }

    public int getDefectProducts() {
        return defectProducts;
    }

    public void setDefectProducts(int defectProducts) {
        this.defectProducts = defectProducts;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getVibration() {
        return vibration;
    }

    public void setVibration(double vibration) {
        this.vibration = vibration;
    }

    public int getAmountToProduce() {
        return amountToProduce;
    }

    public void setAmountToProduce(int amountToProduce) {
        this.amountToProduce = amountToProduce;
    }

    public UUID getBatchID() {
        return batchID;
    }

    public void setBatchID(UUID batchID) {
        this.batchID = batchID;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getBeerType() {
        return beerType;
    }

    public void setBeerType(int beerType) {
        this.beerType = beerType;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getBarley() {
        return barley;
    }

    public void setBarley(double barley) {
        this.barley = barley;
    }

    public double getHops() {
        return hops;
    }

    public void setHops(double hops) {
        this.hops = hops;
    }

    public double getMalt() {
        return malt;
    }

    public void setMalt(double malt) {
        this.malt = malt;
    }

    public double getWheat() {
        return wheat;
    }

    public void setWheat(double wheat) {
        this.wheat = wheat;
    }

    public double getYeast() {
        return yeast;
    }

    public void setYeast(double yeast) {
        this.yeast = yeast;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

    public void readLiveData() {
        this.barley = readBarley();
        this.malt = readMalt();
        this.hops = readHops();
        this.wheat = readWheat();
        this.yeast = readYeast();

        this.maintenance = readMaintenance();

        this.humidity = readHumidity();
        this.vibration = readVibration();
        this.temperature = readTemperature();

        this.amountToProduce = readBatchSize();
        this.totalProducts = readProcessedCount();
        this.defectProducts = readDefectiveCount();
        this.acceptableProducts = readProcessedCount() - this.defectProducts;

        if (this.currentBatch != null) {
            this.batchID = this.currentBatch.getId();
        }
        this.speed = readNormalizedMachineSpeed();
        this.beerType = readBatchBeerType();

        //checks that the batch is not null. It can be null if the program has just been started.
        if (this.currentBatch != null) {
            updateBatch();
        }
        this.currentState = readState();
    }

    /**
     * Writes all the values to the machines current batch.
     * Only writes values to the batch if the state is not 17,
     * as this overwhelms the time in state 17 making the graph ugly and displaying irrelevant data.
     */
    private void updateBatch() {
        if (this.currentState != 17) {
            currentBatch.addHumidity(LocalDateTime.now(), this.humidity);
            currentBatch.addVibration(LocalDateTime.now(), this.vibration);
            currentBatch.addTemperature(LocalDateTime.now(), this.temperature);

            currentBatch.setTotalProducts(this.amountToProduce);
            currentBatch.setProcessedProducts(this.totalProducts);
            currentBatch.setAcceptableProducts(this.acceptableProducts);
            currentBatch.setDefectProducts(this.defectProducts);

            currentBatch.setMachineSpeed(this.readMachineSpeed());
            currentBatch.setNormalizedMachineSpeed(this.speed);
            currentBatch.setProductType(beerType);

            //Checks the difference in time between last state change and the recent state change
            currentBatch.setTimeInState(this.currentState, stateTimestamp.until(LocalDateTime.now(), SECONDS));
            this.stateTimestamp = LocalDateTime.now();

            currentBatch.setMachineId(this.id);
        }
    }

    private void changeRequest() {
        try {
            //Create NodeID for Command Change Request.
            NodeId cmdChangeRequest = new NodeId(6, "::Program:Cube.Command.CmdChangeRequest");

            //write true to the machine, to pass the case from the switch statement.
            connection.writeValue(cmdChangeRequest, DataValue.valueOnly(new Variant(true))).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
