package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.IBatchReportService;
import com.brewmes.common.util.Products;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class PrepareData implements IBatchReportService {
    private double calculateOee(int amountToProduce, double machineSpeed,  int maxMachineSpeed, int acceptedProducts) {
        double plannedProductionTime = (amountToProduce / machineSpeed) * 60;
        double idealCycleTime = (1.0/maxMachineSpeed) * 60;
        return ((acceptedProducts * idealCycleTime) / plannedProductionTime) * 100;
    }

    private double findAvgValueInMap(Map<LocalDateTime, Double> valueMap) {
        return valueMap.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double findMinValueInMap(Map<LocalDateTime, Double> valueMap) {
        return valueMap.values().stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }

    private double findMaxValueInMap(Map<LocalDateTime, Double> valueMap) {
        return valueMap.values().stream().mapToDouble(Double::doubleValue).max().orElse(0);
    }

    private TreeMap<LocalDateTime, Double> sortHumidity(Batch batch) {
        TreeMap<LocalDateTime, Double> map = new TreeMap<>();

        for(MachineData data : batch.getData()) {
            map.put(data.getTimestamp(), data.getHumidity());
        }

        return map;
    }

    private TreeMap<LocalDateTime, Double> sortVibration(Batch batch) {
        TreeMap<LocalDateTime, Double> map = new TreeMap<>();

        for(MachineData data : batch.getData()) {
            map.put(data.getTimestamp(), data.getVibration());
        }

        return map;
    }

    private TreeMap<LocalDateTime, Double> sortTemperature(Batch batch) {
        TreeMap<LocalDateTime, Double> map = new TreeMap<>();

        for(MachineData data : batch.getData()) {
            map.put(data.getTimestamp(), data.getTemperature());
        }

        return map;
    }

    private TreeMap<LocalDateTime, Integer> sortTimeInStates(Batch batch) {
        TreeMap<LocalDateTime, Integer> map = new TreeMap<>();

        for(MachineData data : batch.getData()) {
            map.put(data.getTimestamp(), data.getState());
        }

        return map;
    }

    /**
     * Prepares the data for the IBatchReportService
     * @param batchData The Batch object to be calculated on.
     * @return {@code DataOverTime} object with the calculated values.
     */
    protected DataOverTime prepareData(Batch batchData) {
        DataOverTime dot = new DataOverTime();
        dot.setBatch(batchData);
        List<MachineData> machineData = dot.getBatch().getData();
        Map<LocalDateTime, Double> tempList = new LinkedHashMap<>();
        Map<LocalDateTime, Double> vibList = new LinkedHashMap<>();
        Map<LocalDateTime, Double> humList = new LinkedHashMap<>();

        for (MachineData data : machineData) {
            tempList.put(data.getTimestamp(), data.getTemperature());
            vibList.put(data.getTimestamp(), data.getVibration());
            humList.put(data.getTimestamp(), data.getHumidity());
        }

        dot.setAvgTemp(findAvgValueInMap(tempList));
        dot.setMaxTemp(findMaxValueInMap(tempList));
        dot.setMinTemp(findMinValueInMap(tempList));

        dot.setAvgVibration(findAvgValueInMap(vibList));
        dot.setMaxVibration(findMaxValueInMap(vibList));
        dot.setMinVibration(findMinValueInMap(vibList));

        dot.setAvgHumidity(findAvgValueInMap(humList));
        dot.setMaxHumidity(findMaxValueInMap(humList));
        dot.setMinHumidity(findMinValueInMap(humList));

        dot.setSortedHumidity(sortHumidity(batchData));
        dot.setSortedVibration(sortVibration(batchData));
        dot.setSortedTemperature(sortTemperature(batchData));
        dot.setSortedTimeInStates(sortTimeInStates(batchData));

        double oee = calculateOee(batchData.getAmountToProduce(), batchData.getDesiredSpeed(), Products.values()[batchData.getProductType()].speedLimit, machineData.get(machineData.size()-1).getAcceptableProducts());
        dot.setOee(oee);

        return dot;
    }
}
