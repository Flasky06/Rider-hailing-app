package com.tritva.percel_delivery;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PercelDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(PercelDeliveryApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));
    }
}
