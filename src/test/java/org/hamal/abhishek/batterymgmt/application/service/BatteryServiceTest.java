package org.hamal.abhishek.batterymgmt.application.service;

import org.hamal.abhishek.batterymgmt.application.mapper.BatteryMapper;
import org.hamal.abhishek.batterymgmt.domain.model.Battery;
import org.hamal.abhishek.batterymgmt.infra.persistence.BatteryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BatteryServiceTest {

    private BatteryRepository repository;
    private BatteryMapper mapper;
    private BatteryService batteryService;

    @BeforeEach
    void setup() {
        repository = mock(BatteryRepository.class);
        mapper = mock(BatteryMapper.class);
        batteryService = new BatteryService(repository, mapper);
    }

    @Test
    void testQueryBatteriesInRange() {
        var battery1 = new Battery("Dartmouth", 2000, 100);
        var battery2 = new Battery("SingleBurg", 2100, 150);

        var batteries = List.of(battery1, battery2);

        Mockito.when(repository.findAllByPostCodeBetween(2000, 2200))
                .thenReturn(Flux.fromIterable(batteries));

        var responseMono = batteryService.fetchBatteriesInRange(
                "2000", "2200", Optional.empty(), Optional.empty()
        );

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.batteryNames().equals(List.of("Dartmouth", "SingleBurg")) &&
                        response.totalWattCapacity() == 250 &&

                        // compensates for faults in double calculations
                        Math.abs(response.averageWattCapacity() - 125.0) < 0.001
                )
                .verifyComplete();
    }

}