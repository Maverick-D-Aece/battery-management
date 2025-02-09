package org.hamal.abhishek.batterymgmt.application.dto;

import java.util.List;

public record BatteryAggregateResponse(
        List<String> batteryNames,
        int totalWattCapacity,
        double averageWattCapacity
) {
}
