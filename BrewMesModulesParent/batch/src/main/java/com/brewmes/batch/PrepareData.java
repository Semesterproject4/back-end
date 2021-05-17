package com.brewmes.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.IBatchReportService;

import java.util.ArrayList;
import java.util.List;

public abstract class PrepareData implements IBatchReportService {
    private double calculateOee(int amountToProduce, double machineSpeed, int maxMachineSpeed, int acceptedProducts) {
        double plannedProductionTime = (amountToProduce / machineSpeed) * 60;
        double idealCycleTime = (1.0 / maxMachineSpeed) * 60;
        return ((acceptedProducts * idealCycleTime) / plannedProductionTime) * 100;
    }

    private double findAvgValueInList(List<Double> valueList) {
        return valueList.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double findMinValueInList(List<Double> valueList) {
        return valueList.stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }

    private double findMaxValueInList(List<Double> valueList) {
        return valueList.stream().mapToDouble(Double::doubleValue).max().orElse(0);
    }


    /**
     * Prepares the data for the IBatchReportService
     *
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

        dot.setAvgTemp(findAvgValueInList(tempList));
        dot.setMaxTemp(findMaxValueInList(tempList));
        dot.setMinTemp(findMinValueInList(tempList));

        dot.setAvgVibration(findAvgValueInList(vibList));
        dot.setMaxVibration(findMaxValueInList(vibList));
        dot.setMinVibration(findMinValueInList(vibList));

        dot.setAvgHumidity(findAvgValueInList(humList));
        dot.setMaxHumidity(findMaxValueInList(humList));
        dot.setMinHumidity(findMinValueInList(humList));

        double oee = calculateOee(batchData.getAmountToProduce(), batchData.getDesiredSpeed(), batchData.getProductType().speedLimit, machineData.get(machineData.size() - 1).getAcceptableProducts());
        dot.setOee(oee);

        return dot;
    }
}
