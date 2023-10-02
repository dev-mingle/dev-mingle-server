package com.example.dm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DevMingleApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(DevMingleApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
