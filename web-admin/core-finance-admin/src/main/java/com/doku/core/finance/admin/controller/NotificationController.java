package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.util.List;

import javax.validation.Valid;

import core.finance.host.services.ws.notifications.NotificationResponse;
import core.finance.host.services.ws.transfertypes.LoadFeesByIDResponse;
import core.finance.host.services.ws.transfertypes.LoadTransferTypesByIDResponse;
import core.finance.host.services.ws.transfertypes.TransactionException_Exception;
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
import com.doku.core.finance.admin.model.Notification;

@Controller
public class NotificationController {

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
	@Autowired
	private TransferTypeProcessor transferTypeProcessor;
	@Autowired
	private NotificationProcessor notificationProcessor;

	@RequestMapping(value = "/notification", method = RequestMethod.GET)
	public ModelAndView transferType(
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

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("notification");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/notificationData", method = RequestMethod.GET)
	public String memberData(@RequestParam(value = "start") Integer start,
			@RequestParam(value = "length") Integer length) throws MalformedURLException {
		String jsonData = notificationProcessor.loadAllNotification(start, length);
		return jsonData;
	}

	@RequestMapping(value = "/createNotification", method = RequestMethod.GET)
	public ModelAndView createNotification(
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
		model.addAttribute("createnotification", new Notification());
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createNotification");
	}

	@RequestMapping(value = "/createNotificationForm", method = RequestMethod.POST)
	public ModelAndView createGroupForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createnotification") Notification notif, BindingResult result, ModelMap model) {
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

			notificationProcessor.createNotification(notif);
			model.addAttribute(member);
			return new ModelAndView("redirect:/createNotificationResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createNotificationResult", method = RequestMethod.GET)
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
			model.addAttribute("title", "Create Notification");
			model.addAttribute("message", "Notification has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("notification");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editNotification", method = RequestMethod.GET)
	public ModelAndView editNotification(@RequestParam(value = "id") Integer id,
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
		NotificationResponse notificationResponse = notificationProcessor.loadNotificationByID(id);
		model.addAttribute("notifID", notificationResponse.getNotification().get(0).getId());
		model.addAttribute("name", notificationResponse.getNotification().get(0).getName());
		model.addAttribute("destination", notificationResponse.getNotification().get(0).getModuleURL());
		model.addAttribute(member);
		model.addAttribute("createnotification", new Notification());
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("editNotification");
	}

	@RequestMapping(value = "/editNotificationForm", method = RequestMethod.POST)
	public ModelAndView editNotificationForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createnotification") Notification notif, BindingResult result, ModelMap model) {
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

			notificationProcessor.editNotification(notif);
			model.addAttribute(member);
			return new ModelAndView("redirect:/editNotificationResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
	
	@RequestMapping(value = "/editNotificationResult", method = RequestMethod.GET)
	public ModelAndView editNotificationResult(
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
			model.addAttribute("title", "Edit Notification");
			model.addAttribute("message", "Notification has been updated successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("notification");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
}
