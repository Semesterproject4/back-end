package dk.sdu.mmmi.batch;

import com.brewmes.common.entities.Batch;
import com.brewmes.common.entities.MachineData;
import com.brewmes.common.services.IBatchReportService;
import com.brewmes.common.util.Products;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        double oee = calculateOee(batchData.getAmountToProduce(), batchData.getDesiredSpeed(), Products.values()[batchData.getProductType()].speedLimit, machineData.get(machineData.size()-1).getAcceptableProducts());
        dot.setOee(oee);

        return dot;
    }
}
