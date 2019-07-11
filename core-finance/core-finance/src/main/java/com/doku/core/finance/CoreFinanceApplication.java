package com.doku.core.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:hazelcast-context.xml", "classpath:app-context.xml" })
public class CoreFinanceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CoreFinanceApplication.class, args);
	}

}
