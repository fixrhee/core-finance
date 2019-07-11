package com.doku.core.finance.admin.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import core.finance.host.services.ws.access.CredentialStatusResponse;
import core.finance.host.services.ws.members.Exception_Exception;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.doku.core.finance.admin.model.AdminMenu;
import com.doku.core.finance.admin.model.Member;
import com.doku.core.finance.admin.model.UpgradeMember;

@Controller
public class MemberController {

	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private MenuProcessor menuProcessor;
	@Autowired
	private AccountProcessor accountProcessor;
	@Autowired
	private MessageProcessor messageProcessor;
	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private TransactionProcessor paymentProcessor;
	@Autowired
	private AccessProcessor accessProcessor;
	@Autowired
	private GroupProcessor groupProcessor;

	@RequestMapping(value = "/member", method = RequestMethod.GET)
	public ModelAndView member(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				return new ModelAndView("memberAdmin");
			} else {
				return new ModelAndView("member");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/memberData", method = RequestMethod.GET)
	public String memberData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = memberProcessor.loadListMember(username, Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/createMember", method = RequestMethod.GET)
	public ModelAndView createMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
		List<String> listGroup = groupProcessor.getListGroup();

		model.addAttribute(member);
		model.addAttribute("createmember", new Member());
		model.addAttribute("listGroup", listGroup);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
			return new ModelAndView("createMember");
		} else {
			model.addAttribute("createMemberGroupID", contextLoader.getDefaultGroupID());
			return new ModelAndView("createMemberPartner");
		}
	}

	@RequestMapping(value = "/createMemberForm", method = RequestMethod.POST)
	public ModelAndView createMemberForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("newmember") Member members, BindingResult result, ModelMap model) {
		try {
			if (result.hasErrors()) {
				return new ModelAndView("page_500");
			}
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}

			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);

			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			memberProcessor.createMember(members, member, member.getUsername());
			model.addAttribute(member);
			return new ModelAndView("redirect:/createMemberResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createMemberResult", method = RequestMethod.GET)
	public ModelAndView createMemberResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Create Member");
			model.addAttribute("message", "Member has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("member");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editMember", method = RequestMethod.GET)
	public ModelAndView editMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "username") String username, Model model) throws Exception_Exception, IOException {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.getMemberDetails(username);

			model.addAttribute(member);
			model.addAttribute("upgrademember", new UpgradeMember());
			model.addAllAttributes(memberDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editMember");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editMemberForm", method = RequestMethod.POST)
	public ModelAndView editProfileMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editmember") UpgradeMember editmember, BindingResult result, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			memberProcessor.editMember(editmember);
			return new ModelAndView("redirect:/editMemberResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editMemberResult", method = RequestMethod.GET)
	public ModelAndView editMemberResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Edit Member");
			model.addAttribute("message", "Member has been updated successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("memberAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/detailMember", method = RequestMethod.GET)
	public ModelAndView detailMember(@Valid @ModelAttribute("member") Member members,
			@RequestParam(value = "username") String username,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.searchMemberDetails(username, member.getID(),
					member.getGroupName());

			if (memberDetails.get("fault") != null) {
				return new ModelAndView("redirect:/searchMemberResult?fault=" + memberDetails.get("fault"));
			}

			CredentialStatusResponse csRes = accessProcessor.credentialStatus(username);

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);

			try {
				boolean access = csRes.getAccessStatus().isBlocked();
				model.addAttribute("isBlocked", access);
			} catch (NullPointerException ex) {
				model.addAttribute("isBlocked", "N/A");
			}
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				return new ModelAndView("detailMemberAdmin");
			} else {
				return new ModelAndView("detailMember");
			}
		} catch (Exception ex) {
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/searchMemberResult", method = RequestMethod.GET)
	public ModelAndView serachMemberResult(@RequestParam(value = "fault", required = false) String fault,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			model.addAttribute("notification", "error");
			model.addAttribute("title", "Search Member Failed");
			model.addAttribute("message", fault);
			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				return new ModelAndView("memberAdmin");
			} else {
				return new ModelAndView("member");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/detailMemberPartner", method = RequestMethod.GET)
	public ModelAndView detailMemberPartner(@Valid @ModelAttribute("member") Member members,
			@RequestParam(value = "username") String username,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}
			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			Map<String, Object> memberDetails = memberProcessor.searchMemberDetailsPartner(username, member.getID(),
					member.getGroupName());
			CredentialStatusResponse csRes = accessProcessor.credentialStatus(username);

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);
			model.addAttribute("isBlocked", csRes.getAccessStatus().isBlocked());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				return new ModelAndView("detailMemberAdmin");
			} else {
				return new ModelAndView("detailMember");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			if (ex.getMessage().equalsIgnoreCase("Index: 0, Size: 0")) {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Search Member");
				model.addAttribute("message", "Member Not Found");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Search Member");
				model.addAttribute("message", ex.getMessage());
			}
			return new ModelAndView("member");
		}
	}

	@RequestMapping(value = "/memberKyc", method = RequestMethod.GET)
	public ModelAndView kyc(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			Model model) throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
			return new ModelAndView("memberKycAdmin");
		} else {
			return new ModelAndView("memberKyc");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/kycListData", method = RequestMethod.GET)
	public String kycListData(@RequestParam(value = "start") Integer start,
			@RequestParam(value = "length") Integer length, @RequestParam(value = "username") String username)
			throws Exception_Exception, MalformedURLException {
		String kyc = memberProcessor.loadKycRequest(start, length);
		return kyc;
	}

	@RequestMapping(value = "/createKyc", method = RequestMethod.GET)
	public ModelAndView createKyc(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);

		return new ModelAndView("createKYC");
	}

}
