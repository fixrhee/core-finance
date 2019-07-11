package com.doku.core.finance;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.ws.Endpoint;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.doku.core.finance.process.AccessServiceImpl;
import com.doku.core.finance.process.AccountServiceImpl;
import com.doku.core.finance.process.GroupServiceImpl;
import com.doku.core.finance.process.MemberServiceImpl;
import com.doku.core.finance.process.MenuServiceImpl;
import com.doku.core.finance.process.MessageServiceImpl;
import com.doku.core.finance.process.NotificationServiceImpl;
import com.doku.core.finance.process.TransferServiceImpl;
import com.doku.core.finance.process.TransferTypeServiceImpl;
import com.doku.core.finance.process.WebserviceImpl;

@Configuration
public class ServiceConfiguration {

	@Autowired
	private SpringBus springBus;

	@Bean
	public SpringBus springBus() {
		springBus = new SpringBus();
		springBus.setFeatures(new ArrayList<>(Arrays.asList(loggingFeature())));
		return springBus;
	}

	@Bean
	public LoggingFeature loggingFeature() {
		LoggingFeature loggingFeature = new LoggingFeature();
		loggingFeature.setPrettyLogging(true);
		return loggingFeature;
	}

	@Bean
	public AccessServiceImpl accessImpl() {
		return new AccessServiceImpl();
	}

	@Bean
	public Endpoint accessEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), accessImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/access");
		return endpoint;
	}

	@Bean
	public AccountServiceImpl accountImpl() {
		return new AccountServiceImpl();
	}

	@Bean
	public Endpoint accountEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), accountImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/accounts");
		return endpoint;
	}

	@Bean
	public GroupServiceImpl groupImpl() {
		return new GroupServiceImpl();
	}

	@Bean
	public Endpoint groupEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), groupImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/groups");
		return endpoint;
	}

	@Bean
	public MemberServiceImpl memberImpl() {
		return new MemberServiceImpl();
	}

	@Bean
	public Endpoint memberEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), memberImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/members");
		return endpoint;
	}

	@Bean
	public MenuServiceImpl menuImpl() {
		return new MenuServiceImpl();
	}

	@Bean
	public Endpoint menuEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), menuImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/menu");
		return endpoint;
	}

	@Bean
	public MessageServiceImpl messageImpl() {
		return new MessageServiceImpl();
	}

	@Bean
	public Endpoint messageEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), messageImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/message");
		return endpoint;
	}

	@Bean
	public NotificationServiceImpl notificationImpl() {
		return new NotificationServiceImpl();
	}

	@Bean
	public Endpoint notificationEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), notificationImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/notifications");
		return endpoint;
	}

	@Bean
	public TransferServiceImpl transferImpl() {
		return new TransferServiceImpl();
	}

	@Bean
	public Endpoint transferEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), transferImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/transfers");
		return endpoint;
	}

	@Bean
	public TransferTypeServiceImpl transferTypeImpl() {
		return new TransferTypeServiceImpl();
	}

	@Bean
	public Endpoint transferTypeEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), transferTypeImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/transfertypes");
		return endpoint;
	}

	@Bean
	public WebserviceImpl webImpl() {
		return new WebserviceImpl();
	}

	@Bean
	public Endpoint webserviceEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), webImpl());
		endpoint.publish("http://localhost:8081/core/finance/host/services/ws/webservices");
		return endpoint;
	}

}
