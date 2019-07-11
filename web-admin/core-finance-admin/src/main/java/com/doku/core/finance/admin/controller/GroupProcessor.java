package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import core.finance.host.services.ws.groups.CreateGroupsRequest;
import core.finance.host.services.ws.groups.Exception_Exception;
import core.finance.host.services.ws.groups.Group;
import core.finance.host.services.ws.groups.GroupServiceImplService;
import core.finance.host.services.ws.groups.Groups;
import core.finance.host.services.ws.groups.LoadGroupsByIDRequest;
import core.finance.host.services.ws.groups.LoadGroupsByIDResponse;
import core.finance.host.services.ws.groups.LoadGroupsRequest;
import core.finance.host.services.ws.groups.LoadGroupsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupProcessor {
	@Autowired
	private ContextLoader contextLoader;

	public String loadAllGroup(Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupServiceImplService");
		GroupServiceImplService service = new GroupServiceImplService(url, qName);
		Group client = service.getGroupServiceImplPort();

		core.finance.host.services.ws.groups.Header headerGroup = new core.finance.host.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.groups.Header> groupHeaderAuth = new Holder<core.finance.host.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;
		LoadGroupsRequest loadGroupsReq = new LoadGroupsRequest();
		loadGroupsReq.setCurrentPage(currentPage);
		loadGroupsReq.setPageSize(pageSize);

		LoadGroupsResponse loadGroupsRes = client.loadAllGroups(groupHeaderAuth, loadGroupsReq);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", loadGroupsRes.getGroupsList());
		trxMap.put("recordsTotal", loadGroupsRes.getTotalRecords());
		trxMap.put("recordsFiltered", loadGroupsRes.getTotalRecords());
		return Utils.toJSON(trxMap);
	}

	public String getGroupNameByID(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupServiceImplService");
		GroupServiceImplService service = new GroupServiceImplService(url, qName);
		Group client = service.getGroupServiceImplPort();

		core.finance.host.services.ws.groups.Header headerGroup = new core.finance.host.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.groups.Header> groupHeaderAuth = new Holder<core.finance.host.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;

		LoadGroupsByIDRequest loadGroupsByIDRequest = new LoadGroupsByIDRequest();
		loadGroupsByIDRequest.setId(id);

		LoadGroupsByIDResponse loadGroupsByIDResponse = client.loadGroupsByID(groupHeaderAuth, loadGroupsByIDRequest);
		String groupName = loadGroupsByIDResponse.getGroups().getName();

		return groupName;
	}

	public List<String> getListGroup() throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupServiceImplService");
		GroupServiceImplService service = new GroupServiceImplService(url, qName);
		Group client = service.getGroupServiceImplPort();

		core.finance.host.services.ws.groups.Header headerGroup = new core.finance.host.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.groups.Header> groupHeaderAuth = new Holder<core.finance.host.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;

		LoadGroupsRequest loadGroupsRequest = new LoadGroupsRequest();
		loadGroupsRequest.setCurrentPage(0);
		loadGroupsRequest.setPageSize(100);
		LoadGroupsResponse loadGroupsResponse = client.loadAllGroups(groupHeaderAuth, loadGroupsRequest);

		List<String> groupList = new LinkedList<String>();
		if (loadGroupsResponse.getGroupsList().size() > 0) {
			int i;
			for (i = 0; i < loadGroupsResponse.getGroupsList().size(); i++) {
				groupList.add(loadGroupsResponse.getGroupsList().get(i).getId() + " - "
						+ loadGroupsResponse.getGroupsList().get(i).getName());
			}
		}

		return groupList;
	}

	public HashMap<Integer, String> getMapGroup() throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupServiceImplService");
		GroupServiceImplService service = new GroupServiceImplService(url, qName);
		Group client = service.getGroupServiceImplPort();

		core.finance.host.services.ws.groups.Header headerGroup = new core.finance.host.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.groups.Header> groupHeaderAuth = new Holder<core.finance.host.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;

		LoadGroupsRequest loadGroupsRequest = new LoadGroupsRequest();
		loadGroupsRequest.setCurrentPage(0);
		loadGroupsRequest.setPageSize(100);
		LoadGroupsResponse loadGroupsResponse = client.loadAllGroups(groupHeaderAuth, loadGroupsRequest);

		HashMap<Integer, String> groupMap = new HashMap<Integer, String>();
		if (loadGroupsResponse.getGroupsList().size() > 0) {
			int i;
			for (i = 0; i < loadGroupsResponse.getGroupsList().size(); i++) {
				groupMap.put(loadGroupsResponse.getGroupsList().get(i).getId(),
						loadGroupsResponse.getGroupsList().get(i).getName());
			}
		}
		
		return groupMap;
	}

	public Map<String, Object> loadGroupsByID(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupServiceImplService");
		GroupServiceImplService service = new GroupServiceImplService(url, qName);
		Group client = service.getGroupServiceImplPort();

		core.finance.host.services.ws.groups.Header headerGroup = new core.finance.host.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.groups.Header> groupHeaderAuth = new Holder<core.finance.host.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;

		LoadGroupsByIDRequest loadGroupsByIDReq = new LoadGroupsByIDRequest();
		loadGroupsByIDReq.setId(id);
		LoadGroupsByIDResponse loadGroupsByIDRes = client.loadGroupsByID(groupHeaderAuth, loadGroupsByIDReq);

		Map<String, Object> groupDetailsContentMap = new HashMap<String, Object>();
		groupDetailsContentMap.put("id", loadGroupsByIDRes.getGroups().getId());
		groupDetailsContentMap.put("createdDate", loadGroupsByIDRes.getGroups().getFormattedCreatedDate());
		groupDetailsContentMap.put("name", loadGroupsByIDRes.getGroups().getName());
		groupDetailsContentMap.put("description", loadGroupsByIDRes.getGroups().getDescription());
		groupDetailsContentMap.put("notificationID", loadGroupsByIDRes.getGroups().getNotificationID());
		groupDetailsContentMap.put("maxPinRetry", loadGroupsByIDRes.getGroups().getMaxPinTries());
		groupDetailsContentMap.put("pinLength", loadGroupsByIDRes.getGroups().getPinLength());

		return groupDetailsContentMap;
	}

	public void createGroup(com.doku.core.finance.admin.model.Group groupRequest)
			throws MalformedURLException, Exception_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupServiceImplService");
		GroupServiceImplService service = new GroupServiceImplService(url, qName);
		Group client = service.getGroupServiceImplPort();

		core.finance.host.services.ws.groups.Header headerGroup = new core.finance.host.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.groups.Header> groupHeaderAuth = new Holder<core.finance.host.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;

		String notif[] = groupRequest.getNotification().split("-");
		Integer notifID = Integer.valueOf(notif[0].trim());
		CreateGroupsRequest createGroupsReq = new CreateGroupsRequest();
		Groups gr = new Groups();
		gr.setName(groupRequest.getName());
		gr.setDescription(groupRequest.getDescription());
		gr.setMaxPinTries(groupRequest.getMaxPinAttempt());
		gr.setPinLength(groupRequest.getPinLength());
		gr.setNotificationID(notifID);
		createGroupsReq.setGroups(gr);
		client.createGroups(groupHeaderAuth, createGroupsReq);

	}

}
