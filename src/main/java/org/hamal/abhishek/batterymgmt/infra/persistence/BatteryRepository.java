package org.hamal.abhishek.batterymgmt.infra.persistence;

import org.hamal.abhishek.batterymgmt.domain.model.Battery;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface BatteryRepository extends R2dbcRepository<Battery, UUID> {
    Flux<Battery> findAllByPostCodeBetween(int rangeStart, int rangeEnd);
}
