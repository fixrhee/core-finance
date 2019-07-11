package com.doku.core.finance.process;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import com.doku.core.finance.data.WebServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthCodecFilter {

	@Autowired
	private JWTProcessor jwt;
	@Autowired
	private BaseRepository baseRepository;

	public WebServices Authenticate(String token) {
		try {
			String id = jwt.decodeJWTHMAC256(token);
			WebServices ws = baseRepository.getWebServicesRepository().loadSecretByUsername(id);
			jwt.verifyJWTHMAC256(token, ws.getHash());
			return ws;
		} catch (Exception e) {
			return null;
		}
	}

	public String decode(String s) {
		return StringUtils.newStringUtf8(Base64.decodeBase64(s));
	}

	public static String encode(String s) {
		return Base64.encodeBase64String(StringUtils.getBytesUtf8(s));
	}

}
