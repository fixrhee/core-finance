package com.doku.core.finance.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:application.properties")
public class ContextLoader {

	@Value("${core.ws.header.token}")
	private String headerToken;
	@Value("${member.default.group.id}")
	private Integer DefaultGroupID;
	@Value("${p2p.transfer.type.id}")
	private Integer P2PTrfTypeID;
	@Value("${member.trx.credential.type.id}")
	private Integer memberTrxCredentialTypeID;
	@Value("${member.web.credential.type.id}")
	private Integer memberWebCredentialTypeID;
	@Value("${path.file}")
	private String PathFile;
	@Value("${url.file}")
	private String URLFile;
	@Value("${registered.group.id}")
	private Integer RegisteredGroupID;
	@Value("${merchant.default.group.id}")
	private Integer DefaultMerchantGroupID;
	@Value("${core.ws.header.token.non.credential}")
	private String headerTokenNonCredential;
	@Value("${host.ws.url}")
	private String HostWSUrl;
	@Value("${host.ws.port}")
	private String HostWSPort;
	@Value("${secret.key}")
	private String SecreKey;
	@Value("${site.key}")
	private String SiteKey;

	public void setDefaultMerchantGroupID(Integer defaultMerchantGroupID) {
		DefaultMerchantGroupID = defaultMerchantGroupID;
	}

	public String getHeaderToken() {
		return headerToken;
	}

	public void setHeaderToken(String headerToken) {
		this.headerToken = headerToken;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public Integer getDefaultGroupID() {
		return DefaultGroupID;
	}

	public void setDefaultGroupID(Integer defaultGroupID) {
		DefaultGroupID = defaultGroupID;
	}

	public Integer getP2PTrfTypeID() {
		return P2PTrfTypeID;
	}

	public void setP2PTrfTypeID(Integer p2pTrfTypeID) {
		P2PTrfTypeID = p2pTrfTypeID;
	}

	public String getPathFile() {
		return PathFile;
	}

	public void setPathFile(String pathFile) {
		PathFile = pathFile;
	}

	public String getURLFile() {
		return URLFile;
	}

	public void setURLFile(String uRLFile) {
		URLFile = uRLFile;
	}

	public Integer getRegisteredGroupID() {
		return RegisteredGroupID;
	}

	public void setRegisteredGroupID(Integer registeredGroupID) {
		RegisteredGroupID = registeredGroupID;
	}

	public Integer getDefaultMerchantGroupID() {
		return DefaultMerchantGroupID;
	}

	public String getHeaderTokenNonCredential() {
		return headerTokenNonCredential;
	}

	public void setHeaderTokenNonCredential(String headerTokenNonCredential) {
		this.headerTokenNonCredential = headerTokenNonCredential;
	}

	public String getHostWSUrl() {
		return HostWSUrl;
	}

	public void setHostWSUrl(String hostWSUrl) {
		HostWSUrl = hostWSUrl;
	}

	public String getHostWSPort() {
		return HostWSPort;
	}

	public void setHostWSPort(String hostWSPort) {
		HostWSPort = hostWSPort;
	}

	public String getSecreKey() {
		return SecreKey;
	}

	public void setSecreKey(String secreKey) {
		SecreKey = secreKey;
	}

	public String getSiteKey() {
		return SiteKey;
	}

	public void setSiteKey(String siteKey) {
		SiteKey = siteKey;
	}

	public Integer getMemberTrxCredentialTypeID() {
		return memberTrxCredentialTypeID;
	}

	public void setMemberTrxCredentialTypeID(Integer memberTrxCredentialTypeID) {
		this.memberTrxCredentialTypeID = memberTrxCredentialTypeID;
	}

	public Integer getMemberWebCredentialTypeID() {
		return memberWebCredentialTypeID;
	}

	public void setMemberWebCredentialTypeID(Integer memberWebCredentialTypeID) {
		this.memberWebCredentialTypeID = memberWebCredentialTypeID;
	}

}
