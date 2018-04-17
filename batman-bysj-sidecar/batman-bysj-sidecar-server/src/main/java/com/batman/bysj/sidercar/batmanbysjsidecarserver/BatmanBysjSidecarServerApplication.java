package com.batman.bysj.sidercar.batmanbysjsidecarserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;

@EnableSidecar
@SpringBootApplication
public class BatmanBysjSidecarServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatmanBysjSidecarServerApplication.class, args);
	}
}
