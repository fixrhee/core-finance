package com.doku.core.finance.admin.controller;

import java.net.URL;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.doku.core.finance.admin.model.Login;
import com.doku.core.finance.admin.model.Member;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.json.JSONObject;

@Controller
@RequestMapping("/")
public class LoginController {

	@Autowired
	private SessionProcessor sessionProcessor;
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private ContextLoader contextLoader;

	@RequestMapping("/")
	public String home(Model model) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("key", contextLoader.getSiteKey());
		model.addAttribute("login", new Login());
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String login(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String cookieValue,
			HttpServletResponse response, Model model) {
		Cookie cookie = new Cookie("SessionID", "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		model.addAttribute("key", contextLoader.getSiteKey());
		return "login";
	}

	@RequestMapping(value = "/submitLogin", method = RequestMethod.POST)
	public String submitLogin(@Valid @ModelAttribute("login") Login login, BindingResult result, ModelMap model,
			@RequestParam(value = "g-recaptcha-response", required = false) String gRecaptchaResponse,
			HttpServletResponse response) {
		try {
			if (result.hasErrors()) {
				return "page_500";
			}

			IMap<String, Member> memberMap = instance.getMap("Member");

			String url = "https://www.google.com/recaptcha/api/siteverify?" + "secret=" + contextLoader.getSecreKey()
					+ "&response=" + gRecaptchaResponse;
			InputStream res = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(res, Charset.forName("UTF-8")));

			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
			String jsonText = sb.toString();
			res.close();

			JSONObject json = new JSONObject(jsonText);
			if (json.getBoolean("success")) {
				Member member = sessionProcessor.doLogin(login);
				if (member == null) {
					return "page_500";
				}
				if (member.getStatus().equalsIgnoreCase("VALID")) {
					String sessionID = UUID.randomUUID().toString();
					memberMap.put(sessionID, member);
					Cookie cookie = new Cookie("SessionID", sessionID);
					cookie.setMaxAge(3600);
					response.addCookie(cookie);

					model.addAttribute("member", member);
					return "loginSuccess";
				} else {
					model.addAttribute("key", contextLoader.getSiteKey());
					model.addAttribute("status", member.getDescription());
					return "login";
				}
			} else {
				model.addAttribute("key", contextLoader.getSiteKey());
				model.addAttribute("status", "Re-captcha Failed");
				return "login";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "page_500";
		}
	}
}
