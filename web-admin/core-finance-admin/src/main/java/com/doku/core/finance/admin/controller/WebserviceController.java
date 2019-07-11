package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.util.List;

import javax.validation.Valid;

import core.finance.host.services.ws.transfertypes.LoadFeesByIDResponse;
import core.finance.host.services.ws.transfertypes.LoadTransferTypesByIDResponse;
import core.finance.host.services.ws.transfertypes.TransactionException_Exception;
import core.finance.host.services.ws.webservices.LoadWebserviceResponse;
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
import com.doku.core.finance.admin.model.Webservice;
import com.doku.core.finance.admin.model.WebservicePermission;

@Controller
public class WebserviceController {

	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private MenuProcessor menuProcessor;
	@Autowired
	private MessageProcessor messageProcessor;
	@Autowired
	private WebserviceProcessor webserviceProcessor;
	@Autowired
	private GroupProcessor groupProcessor;

	@RequestMapping(value = "/webservice", method = RequestMethod.GET)
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
			return new ModelAndView("webservice");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/webserviceDetail", method = RequestMethod.GET)
	public ModelAndView webserviceDetail(@RequestParam(value = "id") Integer id,
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
			LoadWebserviceResponse loadWebserviceResponse = webserviceProcessor.loadwebserviceByID(id);

			model.addAttribute(member);
			model.addAttribute("id", id);
			model.addAttribute("username", loadWebserviceResponse.getWebservice().get(0).getUsername());
			model.addAttribute("name", loadWebserviceResponse.getWebservice().get(0).getName());
			model.addAttribute("username", loadWebserviceResponse.getWebservice().get(0).getUsername());
			model.addAttribute("hash", loadWebserviceResponse.getWebservice().get(0).getHash());
			model.addAttribute("enabled", loadWebserviceResponse.getWebservice().get(0).isActive());
			model.addAttribute("secure", loadWebserviceResponse.getWebservice().get(0).isSecureTransaction());

			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("webserviceDetail");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/webserviceData", method = RequestMethod.GET)
	public String webserviceData(@RequestParam(value = "start") Integer start,
			@RequestParam(value = "length") Integer length) throws MalformedURLException {
		String jsonData = webserviceProcessor.loadAllWebservice(start, length);
		return jsonData;
	}

	@ResponseBody
	@RequestMapping(value = "/webservicePermissionData", method = RequestMethod.GET)
	public String webservicePermissionData(@RequestParam(value = "id") Integer id) throws MalformedURLException {
		String jsonData = webserviceProcessor.loadWebservicePermission(id);
		return jsonData;
	}

	@RequestMapping(value = "/createWebservice", method = RequestMethod.GET)
	public ModelAndView createWebservice(
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
			model.addAttribute("createwebservice", new Webservice());
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("createWebservice");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createWebserviceForm", method = RequestMethod.POST)
	public ModelAndView createMemberForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createwebservice") Webservice ws, BindingResult result, ModelMap model) {
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

			webserviceProcessor.createWebservice(ws);
			model.addAttribute(member);
			return new ModelAndView("redirect:/createWebserviceResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createWebserviceResult", method = RequestMethod.GET)
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
			model.addAttribute("title", "Create Webservice");
			model.addAttribute("message", "Webservice has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("webservice");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}

	}

	@RequestMapping(value = "/editWebservice", method = RequestMethod.GET)
	public ModelAndView editWebservice(@RequestParam(value = "id") Integer id,
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
			LoadWebserviceResponse LoadWebserviceResponse = webserviceProcessor.loadwebserviceByID(id);

			model.addAttribute(member);
			model.addAttribute("wsID", LoadWebserviceResponse.getWebservice().get(0).getId());
			model.addAttribute("name", LoadWebserviceResponse.getWebservice().get(0).getName());
			model.addAttribute("enabled", LoadWebserviceResponse.getWebservice().get(0).isActive());
			model.addAttribute("secure", LoadWebserviceResponse.getWebservice().get(0).isSecureTransaction());
			model.addAttribute("hash", LoadWebserviceResponse.getWebservice().get(0).getHash());
			model.addAttribute("username", LoadWebserviceResponse.getWebservice().get(0).getUsername());
			model.addAttribute("password", LoadWebserviceResponse.getWebservice().get(0).getPassword());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("createwebservice", new Webservice());
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editWebservice");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editWebserviceForm", method = RequestMethod.POST)
	public ModelAndView editWebserviceForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createwebservice") Webservice ws, BindingResult result, ModelMap model) {
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

			webserviceProcessor.editWebservice(ws);
			model.addAttribute(member);
			return new ModelAndView("redirect:/editWebserviceResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editWebserviceResult", method = RequestMethod.GET)
	public ModelAndView editWebserviceResult(
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
			model.addAttribute("title", "Update Webservice");
			model.addAttribute("message", "Webservice has been updated successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("webservice");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createWebservicePermission", method = RequestMethod.GET)
	public ModelAndView createWebservicePermission(@RequestParam(value = "id") Integer id,
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
			List<String> listGroup = groupProcessor.getListGroup();

			model.addAttribute(member);
			model.addAttribute("id", id);
			model.addAttribute("listGroup", listGroup);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("createwebservice", new Webservice());
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("createWebservicePermission");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createWebservicePermissionForm", method = RequestMethod.POST)
	public ModelAndView createWebservicePermissionForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("wspermission") WebservicePermission ws, BindingResult result, ModelMap model) {
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

			webserviceProcessor.addWebservicePermission(ws);
			model.addAttribute(member);
			return new ModelAndView("redirect:/createWebservicePermissionResult", model);
		} catch (Exception ex) {
			return new ModelAndView("redirect:/createWebservicePermissionResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createWebservicePermissionResult", method = RequestMethod.GET)
	public ModelAndView createWebservicePermissionResult(@RequestParam(value = "fault", required = false) String fault,
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

			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			if (fault == null) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Webservice Permission");
				model.addAttribute("message", "Webservice Permission has been created successfully");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Webservice Permission Failed");
				model.addAttribute("message", fault);
			}
			return new ModelAndView("webservice");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteWebservicePermission", method = RequestMethod.GET)
	public ModelAndView deleteWebservicePermission(@RequestParam(value = "id") Integer id,
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

			webserviceProcessor.deleteWebservicePermission(id);
			return new ModelAndView("redirect:/deleteWebservicePermissionResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/deleteWebservicePermissionResult?fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/deleteWebservicePermissionResult", method = RequestMethod.GET)
	public ModelAndView deleteWebservicePermissionResult(@RequestParam(value = "fault", required = false) String fault,
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

			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			if (fault == null) {
				model.addAttribute(member);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Delete Webservice Permission");
				model.addAttribute("message", "Webservice Permission has been deleted successfully");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Delete Webservice Permission Failed");
				model.addAttribute("message", fault);
			}
			return new ModelAndView("webservice");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
}
