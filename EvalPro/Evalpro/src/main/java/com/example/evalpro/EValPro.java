package com.example.evalpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.evalpro.model"
})
public class EValPro {

    public static void main(String[] args) {
        SpringApplication.run(EValPro.class, args);
    }
}
