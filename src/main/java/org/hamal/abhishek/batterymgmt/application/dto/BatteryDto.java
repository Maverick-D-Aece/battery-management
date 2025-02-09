package org.hamal.abhishek.batterymgmt.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BatteryDto(
        String name,

        @JsonProperty("postcode")
        String postCode,

        @JsonProperty("capacity")
        int wattCapacity
) {
}
