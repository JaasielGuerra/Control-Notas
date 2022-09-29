package com.umg.controlnotas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
public class ControlnotasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlnotasApplication.class, args);


    }

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guatemala"));
    }

}
