package com.data.classifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.data.classifier" })
public class DataclassifierApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(DataclassifierApplication.class, args);
    }

}
