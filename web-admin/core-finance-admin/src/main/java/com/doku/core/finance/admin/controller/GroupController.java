package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import core.finance.host.services.ws.transfers.InquiryResponse;
import core.finance.host.services.ws.transfers.PaymentResponse;
import core.finance.host.services.ws.transfers.RequestPaymentConfirmationResponse;
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
import com.doku.core.finance.admin.model.Group;
import com.doku.core.finance.admin.model.Member;
import com.doku.core.finance.admin.model.TopupMember;
import com.doku.core.finance.admin.model.Transaction;

@Controller
public class GroupController {
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private MenuProcessor menuProcessor;
	@Autowired
	private MessageProcessor messageProcessor;
	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private TransactionProcessor paymentProcessor;
	@Autowired
	private GroupProcessor groupProcessor;
	@Autowired
	private NotificationProcessor notificationProcessor;

	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public ModelAndView group(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
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

			return new ModelAndView("group");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/groupData", method = RequestMethod.GET)
	public String groupData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = groupProcessor.loadAllGroup(Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/detailGroup", method = RequestMethod.GET)
	public ModelAndView detailGroup(@Valid @RequestParam(value = "username") String username,
			@RequestParam(value = "id") Integer ID,
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
			Map<String, Object> groupDetails = groupProcessor.loadGroupsByID(ID);

			model.addAttribute(member);
			model.addAllAttributes(groupDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("detailGroup");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createGroup", method = RequestMethod.GET)
	public ModelAndView createGroup(
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
		List<String> listNotif = notificationProcessor.getListNotification(0, 100);

		model.addAttribute(member);
		model.addAttribute("creatgroup", new Group());
		model.addAttribute("listNotif", listNotif);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createGroup");
	}

	@RequestMapping(value = "/createGroupForm", method = RequestMethod.POST)
	public ModelAndView createGroupForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("newgroup") Group group, BindingResult result, ModelMap model) {
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

			groupProcessor.createGroup(group);
			model.addAttribute(member);
			return new ModelAndView("redirect:/createGroupResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createGroupResult", method = RequestMethod.GET)
	public ModelAndView createGroupResult(
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
			model.addAttribute("title", "Create Group");
			model.addAttribute("message", "Group has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("group");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
}
