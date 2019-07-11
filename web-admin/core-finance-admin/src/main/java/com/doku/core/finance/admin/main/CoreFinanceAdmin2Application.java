package com.doku.core.finance.admin.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan("com.doku.core.finance.admin.controller")
@ImportResource("classpath:hazelcast-context.xml")
public class CoreFinanceAdmin2Application {

	public static void main(String[] args) {
		SpringApplication.run(CoreFinanceAdmin2Application.class, args);
	}

}
