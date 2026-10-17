package com.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CinemaReservationAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaReservationAppApplication.class, args);
    }
}
