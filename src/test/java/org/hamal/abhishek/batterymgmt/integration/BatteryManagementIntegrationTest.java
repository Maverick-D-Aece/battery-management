package org.hamal.abhishek.batterymgmt.integration;

import org.hamal.abhishek.batterymgmt.BatteryManagementApplication;
import org.hamal.abhishek.batterymgmt.domain.model.Battery;
import org.hamal.abhishek.batterymgmt.infra.persistence.BatteryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SpringBootTest(classes = { BatteryManagementApplication.class })
@Testcontainers
class BatteryManagementIntegrationTest {

    @Autowired
    BatteryRepository repository;

    @Test
    void testBatterySaveAndFetch() {
        var battery = new Battery("Cybernetics", 3000, 200);

        var savedResultFlux = repository.save(battery);

        StepVerifier
                .create(savedResultFlux)
                .expectNextMatches(saved -> saved.getId() != null)
                .verifyComplete();

        var fetchByPostCodeRangeFlux = repository
                .findAllByPostCodeBetween(2900, 3100)
                .delaySubscription(Duration.of(1, ChronoUnit.SECONDS));

        StepVerifier
                .create(fetchByPostCodeRangeFlux)
                .expectNextMatches(b -> b.getName().equals("Cybernetics"))
                .verifyComplete();
    }

}
