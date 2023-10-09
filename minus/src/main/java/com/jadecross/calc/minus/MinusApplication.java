package com.jadecross.calc.minus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
@RestController
public class MinusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinusApplication.class, args);
    }

    // 호출예) http://localhost:8080/?num1=10&num2=5
    @GetMapping("/")
    public float minus(@RequestParam(value = "num1") float num1, @RequestParam(value = "num2") float num2){
        float result = num1 - num2;
        System.out.printf("%s : %f - %f = %f\n", (new Date()).toString(), num1, num2, result);
        return result;
    }
}
