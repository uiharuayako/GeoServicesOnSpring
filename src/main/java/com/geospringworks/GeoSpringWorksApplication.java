package com.geospringworks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EntityScan(basePackages = {"com.geospringworks.domain.entity"})
@EnableCaching
public class GeoSpringWorksApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoSpringWorksApplication.class, args);
	}
}
