package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import core.finance.host.services.ws.access.Access;
import core.finance.host.services.ws.access.AccessServiceImplService;
import core.finance.host.services.ws.access.ValidateCredentialRequest;
import core.finance.host.services.ws.access.ValidateCredentialResponse;
import core.finance.host.services.ws.groups.Group;
import core.finance.host.services.ws.groups.GroupServiceImplService;
import core.finance.host.services.ws.groups.LoadGroupsByIDRequest;
import core.finance.host.services.ws.groups.LoadGroupsByIDResponse;
import core.finance.host.services.ws.members.LoadMembersByUsernameRequest;
import core.finance.host.services.ws.members.LoadMembersResponse;
import core.finance.host.services.ws.members.Member;
import core.finance.host.services.ws.members.MemberServiceImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doku.core.finance.admin.model.Login;

@Component
public class SessionProcessor {

	@Autowired
	private ContextLoader contextLoader;

	public com.doku.core.finance.admin.model.Member doLogin(Login request) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "access?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccessServiceImplService");
		AccessServiceImplService as = new AccessServiceImplService(url, qName);
		Access client = as.getAccessServiceImplPort();

		core.finance.host.services.ws.access.Header headerAccess = new core.finance.host.services.ws.access.Header();
		headerAccess.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.access.Header> headerAccessAuth = new Holder<core.finance.host.services.ws.access.Header>();
		headerAccessAuth.value = headerAccess;

		ValidateCredentialRequest vcr = new ValidateCredentialRequest();
		vcr.setAccessTypeID(contextLoader.getMemberWebCredentialTypeID());
		vcr.setUsername(request.getUsername());
		vcr.setCredential(request.getPassword());
		ValidateCredentialResponse response = client.validateCredential(headerAccessAuth, vcr);

		if (response.getStatus().getMessage().equalsIgnoreCase("VALID")) {
			URL urlMember = new URL(contextLoader.getHostWSUrl() + "members?wsdl");
			QName qNameMember = new QName(contextLoader.getHostWSPort(), "MemberServiceImplService");
			MemberServiceImplService ms = new MemberServiceImplService(urlMember, qNameMember);
			LoadMembersByUsernameRequest loadMembersByUsername = new LoadMembersByUsernameRequest();
			loadMembersByUsername.setUsername(request.getUsername());
			Member memberClient = ms.getMemberServiceImplPort();
			core.finance.host.services.ws.members.Header headerMember = new core.finance.host.services.ws.members.Header();
			headerMember.setToken(contextLoader.getHeaderToken());
			Holder<core.finance.host.services.ws.members.Header> memberHeaderAuth = new Holder<core.finance.host.services.ws.members.Header>();
			memberHeaderAuth.value = headerMember;
			LoadMembersResponse memberResponse = memberClient.loadMembersByUsername(memberHeaderAuth,
					loadMembersByUsername);

			URL urlGroup = new URL(contextLoader.getHostWSUrl() + "groups?wsdl");
			QName qNameGroup = new QName(contextLoader.getHostWSPort(), "GroupServiceImplService");
			GroupServiceImplService gs = new GroupServiceImplService(urlGroup, qNameGroup);
			Group groupClient = gs.getGroupServiceImplPort();
			LoadGroupsByIDRequest groupbyID = new LoadGroupsByIDRequest();
			groupbyID.setId(memberResponse.getMembers().get(0).getGroupID());

			core.finance.host.services.ws.groups.Header headerGroup = new core.finance.host.services.ws.groups.Header();
			headerGroup.setToken(contextLoader.getHeaderToken());
			Holder<core.finance.host.services.ws.groups.Header> groupHeaderAuth = new Holder<core.finance.host.services.ws.groups.Header>();
			groupHeaderAuth.value = headerGroup;
			LoadGroupsByIDResponse groupResponse = groupClient.loadGroupsByID(groupHeaderAuth, groupbyID);

			com.doku.core.finance.admin.model.Member memberView = new com.doku.core.finance.admin.model.Member();
			memberView.setName(memberResponse.getMembers().get(0).getName());
			memberView.setID(memberResponse.getMembers().get(0).getId());
			memberView.setUsername(memberResponse.getMembers().get(0).getUsername());
			memberView.setEmail(memberResponse.getMembers().get(0).getEmail());
			memberView.setGroupID(memberResponse.getMembers().get(0).getGroupID());
			memberView.setGroupName(groupResponse.getGroups().getName());
			memberView.setStatus(response.getStatus().getMessage());

			return memberView;

		} else if (response.getStatus().getMessage().equalsIgnoreCase("BLOCKED")) {
			com.doku.core.finance.admin.model.Member memberView = new com.doku.core.finance.admin.model.Member();
			memberView.setStatus(response.getStatus().getMessage());
			memberView.setDescription("Member Blocked");

			return memberView;
		} else {
			com.doku.core.finance.admin.model.Member memberView = new com.doku.core.finance.admin.model.Member();
			memberView.setStatus(response.getStatus().getMessage());
			memberView.setDescription("Invalid Username/Password");

			return memberView;
		}

	}

}
