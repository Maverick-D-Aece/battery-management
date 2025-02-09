package org.hamal.abhishek.batterymgmt.application.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamal.abhishek.batterymgmt.application.dto.BatteryAggregateResponse;
import org.hamal.abhishek.batterymgmt.application.dto.BatteryDto;
import org.hamal.abhishek.batterymgmt.application.mapper.BatteryMapper;
import org.hamal.abhishek.batterymgmt.domain.model.Battery;
import org.hamal.abhishek.batterymgmt.infra.persistence.BatteryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatteryService {

    private final BatteryRepository repository;
    private final BatteryMapper mapper;

    public Flux<Battery> register(List<BatteryDto> batteryDtoList) {
        log.info("Registering {} batteries", batteryDtoList.size());
        return repository
                .saveAll(mapper.toDomainList(batteryDtoList))
                .doOnError(e ->
                        log.error("Error while saving batteries{}", e.getMessage(), e)
                );
    }

    @SneakyThrows
    public Mono<BatteryAggregateResponse> fetchBatteriesInRange(
            String rangeStart, String rangeEnd,
            Optional<Integer> minWatt,
            Optional<Integer> maxWatt
    ) {
        log.info("Querying batteries with postCode between {} and {}", rangeStart, rangeEnd);

        var start = Integer.parseInt(rangeStart);
        var end = Integer.parseInt(rangeEnd);
        return repository.findAllByPostCodeBetween(start, end)
                .filter(battery ->
                        minWatt.map(min -> battery.getWattCapacity() >= min).orElse(true)
                )
                .filter(battery ->
                        maxWatt.map(max -> battery.getWattCapacity() <= max).orElse(true)
                )
                .collectList()
                .flatMap(batteries -> {
                    if (batteries.isEmpty()) {
                        return Mono.error(new IllegalArgumentException(
                                "No batteries found in the specified range")
                        );
                    }

                    var sortedNames = batteries.stream()
                            .map(Battery::getName)
                            .sorted(Comparator.naturalOrder())
                            .toList();

                    var totalWattCapacity = batteries.stream()
                            .mapToInt(Battery::getWattCapacity)
                            .sum();

                    var averageWattCapacity = batteries.stream()
                            .mapToInt(Battery::getWattCapacity)
                            .average()
                            .orElse(0.0);

                    var response = new BatteryAggregateResponse(
                            sortedNames, totalWattCapacity, averageWattCapacity
                    );

                    return Mono.just(response);
                })
                .doOnError(e -> log.error("Error querying batteries", e));
    }

}
