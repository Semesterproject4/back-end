package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.IBatchReportService;
import com.brewmes.common.util.Products;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class PrepareData implements IBatchReportService {
    private double calculateOee(int amountToProduce, double machineSpeed,  int maxMachineSpeed, int acceptedProducts) {
        double plannedProductionTime = (amountToProduce / machineSpeed) * 60;
        double idealCycleTime = (1.0/maxMachineSpeed) * 60;
        return ((acceptedProducts * idealCycleTime) / plannedProductionTime) * 100;
    }

    private double findAvgValueInMap(List<Double> valueMap) {
        return valueMap.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double findMinValueInMap(List<Double> valueMap) {
        return valueMap.stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }

    private double findMaxValueInMap(List<Double> valueMap) {
        return valueMap.stream().mapToDouble(Double::doubleValue).max().orElse(0);
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
        List<Double> tempList = new ArrayList<>();
        List<Double> vibList = new ArrayList<>();
        List<Double> humList = new ArrayList<>();

        for (MachineData data : machineData) {
            tempList.add(data.getTemperature());
            vibList.add(data.getVibration());
            humList.add(data.getHumidity());
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

        double oee = calculateOee(batchData.getAmountToProduce(), batchData.getDesiredSpeed(), Products.values()[batchData.getProductType()].speedLimit, machineData.get(machineData.size()-1).getAcceptableProducts());
        dot.setOee(oee);

        return dot;
    }
}
