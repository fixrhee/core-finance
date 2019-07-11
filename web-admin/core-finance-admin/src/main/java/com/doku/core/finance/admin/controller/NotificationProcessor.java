package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import core.finance.host.services.ws.notifications.Notification;
import core.finance.host.services.ws.notifications.NotificationRequest;
import core.finance.host.services.ws.notifications.NotificationResponse;
import core.finance.host.services.ws.notifications.NotificationServiceImplService;
import core.finance.host.services.ws.notifications.TransactionException_Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationProcessor {

	@Autowired
	private ContextLoader contextLoader;

	public String loadAllNotification(Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "notifications?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "NotificationServiceImplService");
		NotificationServiceImplService service = new NotificationServiceImplService(url, qName);
		Notification client = service.getNotificationServiceImplPort();

		core.finance.host.services.ws.notifications.Header headerNotification = new core.finance.host.services.ws.notifications.Header();
		headerNotification.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.notifications.Header> notificationHeaderAuth = new Holder<core.finance.host.services.ws.notifications.Header>();
		notificationHeaderAuth.value = headerNotification;

		NotificationRequest notificationRequest = new NotificationRequest();
		notificationRequest.setCurrentPage(currentPage);
		notificationRequest.setPageSize(pageSize);

		NotificationResponse response = client.loadNotifications(notificationHeaderAuth, notificationRequest);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", response.getNotification());
		trxMap.put("recordsTotal", response.getNotification().size());
		trxMap.put("recordsFiltered", response.getNotification().size());
		return Utils.toJSON(trxMap);
	}

	public List<String> getListNotification(Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "notifications?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "NotificationServiceImplService");
		NotificationServiceImplService service = new NotificationServiceImplService(url, qName);
		Notification client = service.getNotificationServiceImplPort();

		core.finance.host.services.ws.notifications.Header headerNotification = new core.finance.host.services.ws.notifications.Header();
		headerNotification.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.notifications.Header> notificationHeaderAuth = new Holder<core.finance.host.services.ws.notifications.Header>();
		notificationHeaderAuth.value = headerNotification;

		NotificationRequest notificationRequest = new NotificationRequest();
		notificationRequest.setCurrentPage(currentPage);
		notificationRequest.setPageSize(pageSize);

		NotificationResponse response = client.loadNotifications(notificationHeaderAuth, notificationRequest);

		List<String> memberList = new LinkedList<String>();
		if (response.getNotification().size() > 0) {
			for (int i = 0; i < response.getNotification().size(); i++) {
				String composeAccount = response.getNotification().get(i).getId() + " - "
						+ response.getNotification().get(i).getName();
				memberList.add(composeAccount);
			}
			return memberList;
		} else {
			return null;
		}
	}

	public void createNotification(com.doku.core.finance.admin.model.Notification req)
			throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "notifications?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "NotificationServiceImplService");
		NotificationServiceImplService service = new NotificationServiceImplService(url, qName);
		Notification client = service.getNotificationServiceImplPort();

		core.finance.host.services.ws.notifications.Header headerNotification = new core.finance.host.services.ws.notifications.Header();
		headerNotification.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.notifications.Header> notificationHeaderAuth = new Holder<core.finance.host.services.ws.notifications.Header>();
		notificationHeaderAuth.value = headerNotification;

		NotificationRequest notificationRequest = new NotificationRequest();
		notificationRequest.setModuleURL(req.getDestination());
		notificationRequest.setName(req.getName());
		client.createNotifications(notificationHeaderAuth, notificationRequest);
	}

	public void editNotification(com.doku.core.finance.admin.model.Notification req)
			throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "notifications?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "NotificationServiceImplService");
		NotificationServiceImplService service = new NotificationServiceImplService(url, qName);
		Notification client = service.getNotificationServiceImplPort();

		core.finance.host.services.ws.notifications.Header headerNotification = new core.finance.host.services.ws.notifications.Header();
		headerNotification.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.notifications.Header> notificationHeaderAuth = new Holder<core.finance.host.services.ws.notifications.Header>();
		notificationHeaderAuth.value = headerNotification;

		NotificationRequest notificationRequest = new NotificationRequest();
		notificationRequest.setModuleURL(req.getDestination());
		notificationRequest.setName(req.getName());
		notificationRequest.setId(req.getId());
		client.editNotifications(notificationHeaderAuth, notificationRequest);
	}

	public NotificationResponse loadNotificationByID(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "notifications?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "NotificationServiceImplService");
		NotificationServiceImplService service = new NotificationServiceImplService(url, qName);
		Notification client = service.getNotificationServiceImplPort();

		core.finance.host.services.ws.notifications.Header headerNotification = new core.finance.host.services.ws.notifications.Header();
		headerNotification.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.notifications.Header> notificationHeaderAuth = new Holder<core.finance.host.services.ws.notifications.Header>();
		notificationHeaderAuth.value = headerNotification;

		NotificationRequest notificationRequest = new NotificationRequest();
		notificationRequest.setId(id);

		return client.loadNotifications(notificationHeaderAuth, notificationRequest);
	}
}
