package org.hamal.abhishek.batterymgmt;

import org.springframework.boot.SpringApplication;

public class TestBatteryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.from(BatteryManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
