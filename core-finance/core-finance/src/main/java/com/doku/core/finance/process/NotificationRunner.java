package com.doku.core.finance.process;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.doku.core.finance.data.PaymentDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class NotificationRunner implements Runnable {

	private PaymentDetails paymentDetails;
	private Logger logger = LoggerFactory.getLogger(NotificationRunner.class);

	@Override
	public void run() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			for (int i = 0; i < paymentDetails.getNotification().size(); i++) {
				HttpClient client = HttpClientBuilder.create().build();
				HttpPost httpPost = new HttpPost(paymentDetails.getNotification().get(i).getModuleURL() + "/"
						+ paymentDetails.getNotification().get(i).getNotificationType());
				HttpEntity stringEntity = new StringEntity(mapper.writeValueAsString(paymentDetails),
						ContentType.APPLICATION_JSON);
				httpPost.setEntity(stringEntity);
				client.execute(httpPost);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public PaymentDetails getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

}
