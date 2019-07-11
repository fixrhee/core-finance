package com.doku.core.finance.admin.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.commons.io.FilenameUtils;
import core.finance.host.services.ws.members.ConfirmKYCRequest;
import core.finance.host.services.ws.members.ConfirmKYCResponse;
import core.finance.host.services.ws.members.Exception_Exception;
import core.finance.host.services.ws.members.ExternalMemberFields;
import core.finance.host.services.ws.members.LoadKYCRequest;
import core.finance.host.services.ws.members.LoadKYCResponse;
import core.finance.host.services.ws.members.LoadMembersByExternalIDRequest;
import core.finance.host.services.ws.members.LoadMembersByIDRequest;
import core.finance.host.services.ws.members.LoadMembersByUsernameRequest;
import core.finance.host.services.ws.members.LoadMembersRequest;
import core.finance.host.services.ws.members.LoadMembersResponse;
import core.finance.host.services.ws.members.Member;
import core.finance.host.services.ws.members.MemberKYCRequest;
import core.finance.host.services.ws.members.MemberServiceImplService;
import core.finance.host.services.ws.members.RegisterMemberRequest;
import core.finance.host.services.ws.members.UpdateMemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.doku.core.finance.admin.model.UpgradeMember;
import com.doku.core.finance.admin.model.UploadFiles;

@Component
public class MemberProcessor {

	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private GroupProcessor groupProcessor;

	public String loadListMember(String username, Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		HashMap<Integer, String> mapGroup = groupProcessor.getMapGroup();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersByUsernameRequest loadMembersByUsernameRequest = new LoadMembersByUsernameRequest();
		loadMembersByUsernameRequest.setUsername(username);
		LoadMembersResponse loadMembersByUsernameResponse = client.loadMembersByUsername(memberHeaderAuth,
				loadMembersByUsernameRequest);

		LoadMembersResponse loadMembersByExternalIDResponse;

		if (groupProcessor.getGroupNameByID(loadMembersByUsernameResponse.getMembers().get(0).getGroupID())
				.equals("PARTNER")) {
			LoadMembersByExternalIDRequest loadMembersByExternalIDRequest = new LoadMembersByExternalIDRequest();
			loadMembersByExternalIDRequest.setPartnerID(loadMembersByUsernameResponse.getMembers().get(0).getId());
			loadMembersByExternalIDRequest.setCurrentPage(currentPage);
			loadMembersByExternalIDRequest.setPageSize(pageSize);

			loadMembersByExternalIDResponse = client.loadMembersByExternalID(memberHeaderAuth,
					loadMembersByExternalIDRequest);
		} else {
			LoadMembersRequest loadMemberRequest = new LoadMembersRequest();
			loadMemberRequest.setCurrentPage(currentPage);
			loadMemberRequest.setPageSize(pageSize);

			loadMembersByExternalIDResponse = client.loadAllMembers(memberHeaderAuth, loadMemberRequest);
		}

		List<Map<String, Object>> trxList = new LinkedList<Map<String, Object>>();
		for (int i = 0; i < loadMembersByExternalIDResponse.getMembers().size(); i++) {
			Map<String, Object> trxContent = new HashMap<String, Object>();
			trxContent.put("id", loadMembersByExternalIDResponse.getMembers().get(i).getId());
			trxContent.put("username", loadMembersByExternalIDResponse.getMembers().get(i).getUsername());
			trxContent.put("msisdn", loadMembersByExternalIDResponse.getMembers().get(i).getMsisdn());
			trxContent.put("email", loadMembersByExternalIDResponse.getMembers().get(i).getEmail());
			trxContent.put("name", loadMembersByExternalIDResponse.getMembers().get(i).getName());
			trxContent.put("group", mapGroup.get(loadMembersByExternalIDResponse.getMembers().get(i).getGroupID()));
			trxContent.put("createdDate", Utils.formatDate(loadMembersByExternalIDResponse.getMembers().get(i)
					.getCreatedDate().toGregorianCalendar().getTime()));
			trxContent.put("groupID", loadMembersByExternalIDResponse.getMembers().get(i).getId());
			trxList.add(trxContent);
		}

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", trxList);
		if (loadMembersByExternalIDResponse.getStatus().getMessage().equals("PROCESSED")) {
			trxMap.put("recordsTotal", loadMembersByExternalIDResponse.getTotalRecords());
			trxMap.put("recordsFiltered", loadMembersByExternalIDResponse.getTotalRecords());
		} else {
			trxMap.put("recordsTotal", 0);
			trxMap.put("recordsFiltered", 0);
		}

		return Utils.toJSON(trxMap);
	}

	public void createMember(com.doku.core.finance.admin.model.Member member, com.doku.core.finance.admin.model.Member memberWeb,
			String parentUsername) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		RegisterMemberRequest registerMemberRequest = new RegisterMemberRequest();
		registerMemberRequest.setName(member.getName());
		registerMemberRequest.setEmail(member.getEmail());
		String msisdn = member.getMsisdn().replaceFirst("0", "62");
		registerMemberRequest.setMsisdn(msisdn);
		registerMemberRequest.setUsername(member.getUsername());

		if (!memberWeb.getGroupName().equalsIgnoreCase("ADMIN")) {
			registerMemberRequest.setGroupID(contextLoader.getDefaultGroupID());

			ExternalMemberFields externalMemberFields = new ExternalMemberFields();
			externalMemberFields.setDescription(parentUsername);
			externalMemberFields.setExternalID(member.getUsername());
			externalMemberFields.setParentID(memberWeb.getID());
			registerMemberRequest.setExternalMemberFields(externalMemberFields);
		} else {
			String[] groupid = member.getGroupName().split("-");
			registerMemberRequest.setGroupID(Integer.valueOf(groupid[0].trim()));
		}
		client.registerMembers(memberHeaderAuth, registerMemberRequest);
	}

	public Map<String, Object> getMemberDetails(String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersByUsernameRequest loadMembersByUsernameRequest = new LoadMembersByUsernameRequest();
		loadMembersByUsernameRequest.setUsername(username);
		LoadMembersResponse loadMembersByUsernameResponse = client.loadMembersByUsername(memberHeaderAuth,
				loadMembersByUsernameRequest);

		Map<String, Object> memberDetailsContentMap = new HashMap<String, Object>();
		memberDetailsContentMap.put("username", loadMembersByUsernameResponse.getMembers().get(0).getUsername());
		memberDetailsContentMap.put("msisdn", loadMembersByUsernameResponse.getMembers().get(0).getMsisdn());
		memberDetailsContentMap.put("email", loadMembersByUsernameResponse.getMembers().get(0).getEmail());
		memberDetailsContentMap.put("name", loadMembersByUsernameResponse.getMembers().get(0).getName());
		memberDetailsContentMap.put("groupID", loadMembersByUsernameResponse.getMembers().get(0).getGroupID());
		memberDetailsContentMap.put("groupName",
				groupProcessor.getGroupNameByID(loadMembersByUsernameResponse.getMembers().get(0).getGroupID()));
		memberDetailsContentMap.put("createdDate",
				loadMembersByUsernameResponse.getMembers().get(0).getFormattedCreatedDate());
		memberDetailsContentMap.put("address", loadMembersByUsernameResponse.getMembers().get(0).getAddress());
		memberDetailsContentMap.put("idCard", loadMembersByUsernameResponse.getMembers().get(0).getIdCardNo());
		memberDetailsContentMap.put("motherName",
				loadMembersByUsernameResponse.getMembers().get(0).getMotherMaidenName());
		memberDetailsContentMap.put("work", loadMembersByUsernameResponse.getMembers().get(0).getWork());
		memberDetailsContentMap.put("sex", loadMembersByUsernameResponse.getMembers().get(0).getSex());
		memberDetailsContentMap.put("nationality", loadMembersByUsernameResponse.getMembers().get(0).getNationality());
		memberDetailsContentMap.put("pob", loadMembersByUsernameResponse.getMembers().get(0).getPlaceOfBirth());

		if (loadMembersByUsernameResponse.getMembers().get(0).getDateOfBirth() == null) {
			memberDetailsContentMap.put("dob", loadMembersByUsernameResponse.getMembers().get(0).getDateOfBirth());
		} else {
			memberDetailsContentMap.put("dob", Utils.formatSimpleDate(loadMembersByUsernameResponse.getMembers().get(0)
					.getDateOfBirth().toGregorianCalendar().getTime()));
		}

		if (loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().size() > 0) {
			int i;
			for (i = 0; i < loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().size(); i++) {
				memberDetailsContentMap.put(
						loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().get(i).getName(),
						loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().get(i).getValue());
			}
		}

		return memberDetailsContentMap;
	}

	public Map<String, Object> searchMemberDetails(String username, Integer id, String group)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersByUsernameRequest loadMembersByUsernameRequest = new LoadMembersByUsernameRequest();
		loadMembersByUsernameRequest.setUsername(username);
		LoadMembersResponse loadMembersByUsernameResponse = client.loadMembersByUsername(memberHeaderAuth,
				loadMembersByUsernameRequest);

		Map<String, Object> memberDetailsContentMap = new HashMap<String, Object>();

		if (loadMembersByUsernameResponse.getMembers().size() == 0) {
			memberDetailsContentMap.put("fault", "Member Not Found");
			return memberDetailsContentMap;
		}

		memberDetailsContentMap.put("username", loadMembersByUsernameResponse.getMembers().get(0).getUsername());
		memberDetailsContentMap.put("msisdn", loadMembersByUsernameResponse.getMembers().get(0).getMsisdn());
		memberDetailsContentMap.put("email", loadMembersByUsernameResponse.getMembers().get(0).getEmail());
		memberDetailsContentMap.put("name", loadMembersByUsernameResponse.getMembers().get(0).getName());
		memberDetailsContentMap.put("groupID", loadMembersByUsernameResponse.getMembers().get(0).getGroupID());
		memberDetailsContentMap.put("groupName",
				groupProcessor.getGroupNameByID(loadMembersByUsernameResponse.getMembers().get(0).getGroupID()));
		memberDetailsContentMap.put("createdDate",
				loadMembersByUsernameResponse.getMembers().get(0).getFormattedCreatedDate());
		memberDetailsContentMap.put("address", loadMembersByUsernameResponse.getMembers().get(0).getAddress());
		memberDetailsContentMap.put("idCard", loadMembersByUsernameResponse.getMembers().get(0).getIdCardNo());
		memberDetailsContentMap.put("motherName",
				loadMembersByUsernameResponse.getMembers().get(0).getMotherMaidenName());
		memberDetailsContentMap.put("work", loadMembersByUsernameResponse.getMembers().get(0).getWork());
		memberDetailsContentMap.put("sex", loadMembersByUsernameResponse.getMembers().get(0).getSex());
		memberDetailsContentMap.put("nationality", loadMembersByUsernameResponse.getMembers().get(0).getNationality());
		memberDetailsContentMap.put("pob", loadMembersByUsernameResponse.getMembers().get(0).getPlaceOfBirth());

		if (loadMembersByUsernameResponse.getMembers().get(0).getDateOfBirth() == null) {
			memberDetailsContentMap.put("dob", loadMembersByUsernameResponse.getMembers().get(0).getDateOfBirth());
		} else {
			memberDetailsContentMap.put("dob", Utils.formatSimpleDate(loadMembersByUsernameResponse.getMembers().get(0)
					.getDateOfBirth().toGregorianCalendar().getTime()));
		}

		if (loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().size() > 0) {
			int i;
			for (i = 0; i < loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().size(); i++) {
				memberDetailsContentMap.put(
						loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().get(i).getName(),
						loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().get(i).getValue());
			}
		}

		return memberDetailsContentMap;
	}

	public Map<String, Object> searchMemberDetailsPartner(String username, Integer id, String group)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersByExternalIDRequest loadMembersBExtIDRequest = new LoadMembersByExternalIDRequest();
		loadMembersBExtIDRequest.setPartnerID(id);
		loadMembersBExtIDRequest.setUsername(username);
		LoadMembersResponse loadMembersByUsernameResponse = client.loadMembersByExternalID(memberHeaderAuth,
				loadMembersBExtIDRequest);

		Map<String, Object> memberDetailsContentMap = new HashMap<String, Object>();
		memberDetailsContentMap.put("username", loadMembersByUsernameResponse.getMembers().get(0).getUsername());
		memberDetailsContentMap.put("msisdn", loadMembersByUsernameResponse.getMembers().get(0).getMsisdn());
		memberDetailsContentMap.put("email", loadMembersByUsernameResponse.getMembers().get(0).getEmail());
		memberDetailsContentMap.put("name", loadMembersByUsernameResponse.getMembers().get(0).getName());
		memberDetailsContentMap.put("groupID", loadMembersByUsernameResponse.getMembers().get(0).getGroupID());
		memberDetailsContentMap.put("groupName",
				groupProcessor.getGroupNameByID(loadMembersByUsernameResponse.getMembers().get(0).getGroupID()));
		memberDetailsContentMap.put("createdDate",
				loadMembersByUsernameResponse.getMembers().get(0).getFormattedCreatedDate());
		memberDetailsContentMap.put("address", loadMembersByUsernameResponse.getMembers().get(0).getAddress());
		memberDetailsContentMap.put("idCard", loadMembersByUsernameResponse.getMembers().get(0).getIdCardNo());
		memberDetailsContentMap.put("motherName",
				loadMembersByUsernameResponse.getMembers().get(0).getMotherMaidenName());
		memberDetailsContentMap.put("work", loadMembersByUsernameResponse.getMembers().get(0).getWork());
		memberDetailsContentMap.put("sex", loadMembersByUsernameResponse.getMembers().get(0).getSex());
		memberDetailsContentMap.put("nationality", loadMembersByUsernameResponse.getMembers().get(0).getNationality());
		memberDetailsContentMap.put("pob", loadMembersByUsernameResponse.getMembers().get(0).getPlaceOfBirth());

		if (loadMembersByUsernameResponse.getMembers().get(0).getDateOfBirth() == null) {
			memberDetailsContentMap.put("dob", loadMembersByUsernameResponse.getMembers().get(0).getDateOfBirth());
		} else {
			memberDetailsContentMap.put("dob", Utils.formatSimpleDate(loadMembersByUsernameResponse.getMembers().get(0)
					.getDateOfBirth().toGregorianCalendar().getTime()));
		}

		if (loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().size() > 0) {
			int i;
			for (i = 0; i < loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().size(); i++) {
				memberDetailsContentMap.put(
						loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().get(i).getName(),
						loadMembersByUsernameResponse.getMembers().get(0).getCustomFields().get(i).getValue());
			}
		}

		return memberDetailsContentMap;
	}

	public List<String> getListMember(String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersByUsernameRequest loadMembersByUsernameRequest = new LoadMembersByUsernameRequest();
		loadMembersByUsernameRequest.setUsername(username);
		LoadMembersResponse loadMembersByUsernameResponse = client.loadMembersByUsername(memberHeaderAuth,
				loadMembersByUsernameRequest);

		LoadMembersByExternalIDRequest loadMembersByExternalIDRequest = new LoadMembersByExternalIDRequest();
		loadMembersByExternalIDRequest.setPartnerID(loadMembersByUsernameResponse.getMembers().get(0).getId());
		loadMembersByExternalIDRequest.setCurrentPage(0);
		loadMembersByExternalIDRequest.setPageSize(100);
		LoadMembersResponse loadMembersByExternalIDResponse = client.loadMembersByExternalID(memberHeaderAuth,
				loadMembersByExternalIDRequest);

		List<String> memberList = new LinkedList<String>();
		if (loadMembersByExternalIDResponse.getMembers().size() > 0) {
			for (int i = 0; i < loadMembersByExternalIDResponse.getMembers().size(); i++) {
				String composeAccount = loadMembersByExternalIDResponse.getMembers().get(i).getUsername() + "-"
						+ loadMembersByExternalIDResponse.getMembers().get(i).getName();
				memberList.add(composeAccount);
			}

			return memberList;
		} else {
			return null;
		}
	}

	public List<String> getListAllMember(String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersRequest loadMemberRequest = new LoadMembersRequest();
		loadMemberRequest.setCurrentPage(0);
		loadMemberRequest.setPageSize(10000);

		LoadMembersResponse loadMemberRes = client.loadAllMembers(memberHeaderAuth, loadMemberRequest);

		List<String> memberList = new LinkedList<String>();
		if (loadMemberRes.getMembers().size() > 0) {
			for (int i = 0; i < loadMemberRes.getMembers().size(); i++) {
				String composeAccount = loadMemberRes.getMembers().get(i).getUsername() + "-"
						+ loadMemberRes.getMembers().get(i).getName();
				memberList.add(composeAccount);
			}

			return memberList;
		} else {
			return null;
		}
	}

	public void editMember(UpgradeMember member) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		UpdateMemberRequest updateMemberReq = new UpdateMemberRequest();
		updateMemberReq.setUsername(member.getUsername());
		updateMemberReq.setName(member.getName());
		updateMemberReq.setAddress(member.getAddress());
		updateMemberReq.setEmail(member.getEmail());
		updateMemberReq.setMsisdn(member.getMsisdn());
		updateMemberReq.setGroupID(member.getGroupID());

		if (member.getDob() != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(member.getDob());
			XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
			updateMemberReq.setDateOfBirth(xmldate);
			updateMemberReq.setPlaceOfBirth(member.getPob());
		}

		updateMemberReq.setIdCardNo(member.getIdCard());
		updateMemberReq.setMotherMaidenName(member.getMotherName());
		updateMemberReq.setWork(member.getWork());
		updateMemberReq.setSex(member.getSex());
		updateMemberReq.setNationality(member.getNationality());
		
		client.updateMembers(memberHeaderAuth, updateMemberReq);
	}

	public void editProfile(UpgradeMember member) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		UpdateMemberRequest updateMemberReq = new UpdateMemberRequest();
		updateMemberReq.setUsername(member.getUsername());
		updateMemberReq.setName(member.getName());
		updateMemberReq.setEmail(member.getEmail());

		client.updateMembers(memberHeaderAuth, updateMemberReq);
	}

	public String loadKycRequest(Integer currentPage, Integer pageSize)
			throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;
		LoadKYCRequest loadKYCRequest = new LoadKYCRequest();
		loadKYCRequest.setCurrentPage(currentPage);
		loadKYCRequest.setPageSize(pageSize);

		LoadKYCResponse loadKYCResponse = client.loadKYCRequest(memberHeaderAuth, loadKYCRequest);

		List<Map<String, Object>> trxList = new LinkedList<Map<String, Object>>();
		for (int i = 0; i < loadKYCResponse.getMemberKYC().size(); i++) {
			Map<String, Object> trxContent = new HashMap<String, Object>();
			trxContent.put("id", loadKYCResponse.getMemberKYC().get(i).getId());
			trxContent.put("username", loadKYCResponse.getMemberKYC().get(i).getFromMember().getUsername());
			trxContent.put("group", loadKYCResponse.getMemberKYC().get(i).getGroup().getName());
			trxContent.put("date", loadKYCResponse.getMemberKYC().get(i).getFormattedRequestedDate());
			trxContent.put("status", loadKYCResponse.getMemberKYC().get(i).getStatus());
			trxList.add(trxContent);
		}

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", trxList);
		trxMap.put("recordsTotal", loadKYCResponse.getTotalRecords());
		trxMap.put("recordsFiltered", loadKYCResponse.getTotalRecords());

		return Utils.toJSON(trxMap);
	}

	public void requestKycMember(UpgradeMember upgrade, UploadFiles uploadFile)
			throws Exception_Exception, DatatypeConfigurationException, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		MemberKYCRequest memberKYCRequest = new MemberKYCRequest();
		memberKYCRequest.setAddress(upgrade.getAddress());
		memberKYCRequest.setUsername(upgrade.getMsisdn());

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(upgrade.getDob());
		XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

		memberKYCRequest.setDateOfBirth(xmldate);
		memberKYCRequest.setPlaceOfBirth(upgrade.getPob());
		memberKYCRequest.setIdCardNo(upgrade.getIdCard());
		memberKYCRequest.setGroupID(contextLoader.getRegisteredGroupID());
		memberKYCRequest.setMotherMaidenName(upgrade.getMotherName());
		memberKYCRequest.setImagePath1(contextLoader.getURLFile() + "1-" + upgrade.getMsisdn() + "."
				+ FilenameUtils.getExtension(uploadFile.getFiles().get(0).getOriginalFilename()));
		memberKYCRequest.setImagePath2(contextLoader.getURLFile() + "2-" + upgrade.getMsisdn() + "."
				+ FilenameUtils.getExtension(uploadFile.getFiles().get(1).getOriginalFilename()));
		memberKYCRequest.setImagePath3(contextLoader.getURLFile() + "3-" + upgrade.getMsisdn() + "."
				+ FilenameUtils.getExtension(uploadFile.getFiles().get(2).getOriginalFilename()));

		client.membersKYCRequest(memberHeaderAuth, memberKYCRequest);

	}

	public ConfirmKYCResponse approveKycRequest(int id, String username)
			throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		boolean accepted = true;

		ConfirmKYCRequest confirmKYCRequest = new ConfirmKYCRequest();
		confirmKYCRequest.setAccepted(accepted);
		confirmKYCRequest.setId(id);
		confirmKYCRequest.setUsername(username);
		ConfirmKYCResponse confirmKYCResponse = client.confirmKYCRequest(memberHeaderAuth, confirmKYCRequest);

		return confirmKYCResponse;
	}

	public ConfirmKYCResponse rejectKycRequest(int id, String username, String description)
			throws Exception_Exception, MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		boolean accepted = false;

		ConfirmKYCRequest confirmKYCRequest = new ConfirmKYCRequest();
		confirmKYCRequest.setAccepted(accepted);
		confirmKYCRequest.setId(id);
		confirmKYCRequest.setUsername(username);
		confirmKYCRequest.setDescription(description);
		ConfirmKYCResponse confirmKYCResponse = client.confirmKYCRequest(memberHeaderAuth, confirmKYCRequest);

		return confirmKYCResponse;
	}

	public Map<String, Object> getMemberKycDetails(int id) throws Exception_Exception, IOException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadKYCRequest loadKycRequest = new LoadKYCRequest();
		loadKycRequest.setId(id);
		LoadKYCResponse loadKycResponse = client.loadKYCRequest(memberHeaderAuth, loadKycRequest);

		Map<String, Object> memberDetailsContentMap = new HashMap<String, Object>();
		memberDetailsContentMap.put("id", loadKycResponse.getMemberKYC().get(0).getId());
		memberDetailsContentMap.put("statusApproval", loadKycResponse.getMemberKYC().get(0).getStatus());
		memberDetailsContentMap.put("requestedDate", loadKycResponse.getMemberKYC().get(0).getFormattedRequestedDate());
		memberDetailsContentMap.put("username", loadKycResponse.getMemberKYC().get(0).getFromMember().getUsername());
		memberDetailsContentMap.put("msisdn", loadKycResponse.getMemberKYC().get(0).getFromMember().getMsisdn());
		memberDetailsContentMap.put("email", loadKycResponse.getMemberKYC().get(0).getFromMember().getEmail());
		memberDetailsContentMap.put("name", loadKycResponse.getMemberKYC().get(0).getFromMember().getName());
		memberDetailsContentMap.put("groupName",
				groupProcessor.getGroupNameByID(loadKycResponse.getMemberKYC().get(0).getFromMember().getGroupID()));
		memberDetailsContentMap.put("createdDate",
				loadKycResponse.getMemberKYC().get(0).getFromMember().getFormattedCreatedDate());
		memberDetailsContentMap.put("address", loadKycResponse.getMemberKYC().get(0).getFromMember().getAddress());
		memberDetailsContentMap.put("idCard", loadKycResponse.getMemberKYC().get(0).getFromMember().getIdCardNo());
		memberDetailsContentMap.put("motherName",
				loadKycResponse.getMemberKYC().get(0).getFromMember().getMotherMaidenName());
		memberDetailsContentMap.put("work", loadKycResponse.getMemberKYC().get(0).getFromMember().getWork());
		memberDetailsContentMap.put("sex", loadKycResponse.getMemberKYC().get(0).getFromMember().getSex());
		memberDetailsContentMap.put("nationality", loadKycResponse.getMemberKYC().get(0).getFromMember().getNationality());
		memberDetailsContentMap.put("pob", loadKycResponse.getMemberKYC().get(0).getFromMember().getPlaceOfBirth());
		memberDetailsContentMap.put("dob", Utils.formatSimpleDate(loadKycResponse.getMemberKYC().get(0).getFromMember()
				.getDateOfBirth().toGregorianCalendar().getTime()));
		memberDetailsContentMap.put("description", loadKycResponse.getMemberKYC().get(0).getDescription());

		memberDetailsContentMap.put("path1", loadKycResponse.getMemberKYC().get(0).getImagePath1());
		memberDetailsContentMap.put("path2", loadKycResponse.getMemberKYC().get(0).getImagePath2());
		memberDetailsContentMap.put("path3", loadKycResponse.getMemberKYC().get(0).getImagePath3());

		return memberDetailsContentMap;
	}

	public void changeGroup(UpgradeMember member) throws Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		String[] group = member.getGroupName().split("-");

		UpdateMemberRequest updateMemberReq = new UpdateMemberRequest();
		updateMemberReq.setUsername(member.getMsisdn());
		updateMemberReq.setGroupID(Integer.parseInt(group[0].trim()));

		client.updateMembers(memberHeaderAuth, updateMemberReq);
	}

	public LoadMembersResponse loadMember(String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersByUsernameRequest loadMemberReq = new LoadMembersByUsernameRequest();
		loadMemberReq.setUsername(username);
		LoadMembersResponse loadMembersByUsernameResponse = client.loadMembersByUsername(memberHeaderAuth,
				loadMemberReq);

		return loadMembersByUsernameResponse;
	}

	public LoadMembersResponse loadMemberByID(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
		MemberServiceImplService service = new MemberServiceImplService(url, qName);
		Member client = service.getMemberServiceImplPort();

		core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
		headerMember.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
		memberHeaderAuth.value = headerMember;

		LoadMembersByIDRequest loadMemberReq = new LoadMembersByIDRequest();
		loadMemberReq.setId(id);
		LoadMembersResponse loadMembersByUsernameResponse = client.loadMembersByID(memberHeaderAuth, loadMemberReq);
		return loadMembersByUsernameResponse;
	}
}
