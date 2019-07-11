package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.xml.datatype.XMLGregorianCalendar;

import core.finance.host.services.ws.accounts.LoadAccountsByIDResponse;
import core.finance.host.services.ws.members.LoadMembersResponse;
import core.finance.host.services.ws.notifications.NotificationResponse;
import core.finance.host.services.ws.transfertypes.FeeRequest;
import core.finance.host.services.ws.transfertypes.LoadBrokerResponse;
import core.finance.host.services.ws.transfertypes.LoadFeesByIDResponse;
import core.finance.host.services.ws.transfertypes.LoadTransferTypesByIDResponse;
import core.finance.host.services.ws.transfertypes.TransactionException_Exception;
import core.finance.host.services.ws.transfertypes.TransferTypeNotificationResponse;
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
import com.doku.core.finance.admin.model.Broker;
import com.doku.core.finance.admin.model.Fee;
import com.doku.core.finance.admin.model.Member;
import com.doku.core.finance.admin.model.MemberFiltering;
import com.doku.core.finance.admin.model.TransferNotification;
import com.doku.core.finance.admin.model.TransferType;
import com.doku.core.finance.admin.model.TransferTypePermission;

@Controller
public class TransferTypeController {

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

	@RequestMapping(value = "/transferType", method = RequestMethod.GET)
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
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transferTypeData", method = RequestMethod.GET)
	public String memberData(@RequestParam(value = "start") Integer start,
			@RequestParam(value = "length") Integer length) throws MalformedURLException {
		String jsonData = transferTypeProcessor.loadTransferType(Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/transferTypeDetail", method = RequestMethod.GET)
	public ModelAndView transferTypeDetail(@RequestParam(value = "id") Integer id,
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

			LoadTransferTypesByIDResponse loadTransferTypesByIDResponse = transferTypeProcessor.loadTransferType(id);

			model.addAttribute("transferTypeID", loadTransferTypesByIDResponse.getTransferTypes().getId());
			model.addAttribute("name", loadTransferTypesByIDResponse.getTransferTypes().getName());
			model.addAttribute("fromAccount", loadTransferTypesByIDResponse.getTransferTypes().getFromAccountName());
			model.addAttribute("toAccount", loadTransferTypesByIDResponse.getTransferTypes().getToAccountName());
			model.addAttribute("description", loadTransferTypesByIDResponse.getTransferTypes().getDescription());
			model.addAttribute("minAmount", loadTransferTypesByIDResponse.getTransferTypes().getMinAmount());
			model.addAttribute("maxAmount", loadTransferTypesByIDResponse.getTransferTypes().getMaxAmount());
			model.addAttribute("otpThreshold", loadTransferTypesByIDResponse.getTransferTypes().getOtpThreshold());
			model.addAttribute("maxTransaction", loadTransferTypesByIDResponse.getTransferTypes().getMaxCount());

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferTypeDetail");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transferTypePermissionData", method = RequestMethod.GET)
	public String transferTypePermissionData(@RequestParam(value = "id") Integer transferTypeData,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "length", required = false) Integer length)
			throws MalformedURLException, TransactionException_Exception {
		String jsonData = transferTypeProcessor.loadTransferTypePermission(transferTypeData);
		return jsonData;
	}

	@ResponseBody
	@RequestMapping(value = "/feeData", method = RequestMethod.GET)
	public String feeData(@RequestParam(value = "transferTypeID") Integer transferTypeID,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "length", required = false) Integer length)
			throws MalformedURLException, TransactionException_Exception {
		String jsonData = transferTypeProcessor.loadFee(transferTypeID);
		return jsonData;
	}

	@RequestMapping(value = "/feeDetail", method = RequestMethod.GET)
	public ModelAndView feeDetail(@RequestParam(value = "id") Integer id,
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

			LoadFeesByIDResponse loadFeesByIDResponse = transferTypeProcessor.loadFeeByID(id);

			model.addAttribute("feeID", loadFeesByIDResponse.getFee().getId());
			model.addAttribute("name", loadFeesByIDResponse.getFee().getName());
			model.addAttribute("description", loadFeesByIDResponse.getFee().getDescription());
			model.addAttribute("fromMemberName", loadFeesByIDResponse.getFee().getFromMemberName());
			model.addAttribute("fromAccountName", loadFeesByIDResponse.getFee().getFromAccountName());
			model.addAttribute("toMemberName", loadFeesByIDResponse.getFee().getToMemberName());
			model.addAttribute("toAccountName", loadFeesByIDResponse.getFee().getToAccountName());
			model.addAttribute("deductAmount", loadFeesByIDResponse.getFee().isDeductAmount());
			model.addAttribute("fixedAmount", loadFeesByIDResponse.getFee().getFixedAmount());
			model.addAttribute("percentageValue", loadFeesByIDResponse.getFee().getPercentageValue());

			if (loadFeesByIDResponse.getFee().getStartDate() != null) {
				model.addAttribute("startDate", loadFeesByIDResponse.getFee().getStartDate());
			} else {
				model.addAttribute("startDate", "N/A");
			}

			if (loadFeesByIDResponse.getFee().getStartDate() != null) {
				model.addAttribute("endDate", loadFeesByIDResponse.getFee().getEndDate());
			} else {
				model.addAttribute("endDate", "N/A");
			}

			model.addAttribute("filterSource", !loadFeesByIDResponse.getFee().isFromAllMember());
			model.addAttribute("filterDestination", !loadFeesByIDResponse.getFee().isToAllMember());

			model.addAttribute("initialRangeAmount", loadFeesByIDResponse.getFee().getInitialRangeAmount());
			model.addAttribute("maximumRangeAmount", loadFeesByIDResponse.getFee().getMaximumRangeAmount());
			model.addAttribute("priority", loadFeesByIDResponse.getFee().isPriority());
			model.addAttribute("enabled", loadFeesByIDResponse.getFee().isEnabled());

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("feeDetail");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transferNotificationData", method = RequestMethod.GET)
	public String transferNotificationData(@RequestParam(value = "transferTypeID") Integer transferTypeID)
			throws MalformedURLException, TransactionException_Exception {
		String jsonData = transferTypeProcessor.loadTransferNotification(transferTypeID);
		return jsonData;
	}

	@RequestMapping(value = "/createTransferType", method = RequestMethod.GET)
	public ModelAndView createTransferType(
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
		List<String> listAccount = accountProcessor.listAllAccount(0, 1000);

		model.addAttribute(member);
		model.addAttribute("createTransferTypeModel", new TransferType());
		model.addAttribute("listAccount", listAccount);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createTransferType");
	}

	@RequestMapping(value = "/createTransferTypeForm", method = RequestMethod.POST)
	public ModelAndView createTransferTypeForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createTransferTypeModel") TransferType transferType, BindingResult result,
			ModelMap model) {
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

			transferTypeProcessor.createTransferType(transferType);
			model.addAttribute(member);
			return new ModelAndView("redirect:/createTransferTypeResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createTransferTypeResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createTransferTypeResult", method = RequestMethod.GET)
	public ModelAndView createMemberResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Transfer Type");
				model.addAttribute("message", "Transfer Type has been created successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Transfer Type Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editTransferType", method = RequestMethod.GET)
	public ModelAndView editTransferType(@RequestParam(value = "id") Integer id,
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
		List<String> listAccount = accountProcessor.listAllAccount(0, 1000);

		LoadTransferTypesByIDResponse response = transferTypeProcessor.loadTransferType(id);
		model.addAttribute("id", response.getTransferTypes().getId());
		model.addAttribute("name", response.getTransferTypes().getName());
		model.addAttribute("description", response.getTransferTypes().getDescription());
		model.addAttribute("fromAccount", response.getTransferTypes().getFromAccounts() + " - "
				+ response.getTransferTypes().getFromAccountName());
		model.addAttribute("toAccount",
				response.getTransferTypes().getToAccounts() + " - " + response.getTransferTypes().getToAccountName());
		model.addAttribute("minAmount", response.getTransferTypes().getMinAmount());
		model.addAttribute("maxAmount", response.getTransferTypes().getMaxAmount());
		model.addAttribute("otpThreshold", response.getTransferTypes().getOtpThreshold());
		model.addAttribute("maxCount", response.getTransferTypes().getMaxCount());

		model.addAttribute(member);
		model.addAttribute("editTransferTypeModel", new TransferType());
		model.addAttribute("listAccount", listAccount);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("editTransferType");
	}

	@RequestMapping(value = "/editTransferTypeForm", method = RequestMethod.POST)
	public ModelAndView editTransferTypeForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editTransferTypeModel") TransferType transferType, BindingResult result,
			ModelMap model) {
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

			transferTypeProcessor.editTransferType(transferType);
			model.addAttribute(member);
			return new ModelAndView("redirect:/editTransferTypeResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/editTransferTypeResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/editTransferTypeResult", method = RequestMethod.GET)
	public ModelAndView editTransferTypeResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Update Transfer Type");
				model.addAttribute("message", "Transfer Type has been updated successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Update Transfer Type Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createTransferTypePermission", method = RequestMethod.GET)
	public ModelAndView createTransferTypePermission(@RequestParam(value = "id") Integer id,
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
		model.addAttribute("id", id);
		model.addAttribute("createTransferTypePermissionModel", new TransferTypePermission());
		model.addAttribute("listGroup", listGroup);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createTransferTypePermission");
	}

	@RequestMapping(value = "/createTransferTypePermissionForm", method = RequestMethod.POST)
	public ModelAndView createTransferTypePermissionForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editTransferTypeModel") TransferTypePermission permission, BindingResult result,
			ModelMap model) {
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

			Integer groupID = Integer.valueOf(permission.getGroup().split("-")[0].trim());
			transferTypeProcessor.createTransferTypePermission(groupID, permission.getTransferTypeID());
			model.addAttribute(member);
			return new ModelAndView("redirect:/createTransferTypePermissionResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createTransferTypePermissionResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createTransferTypePermissionResult", method = RequestMethod.GET)
	public ModelAndView createTransferTypePermissionResult(
			@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Transfer Type Permission");
				model.addAttribute("message", "Permission has been added successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer Type Permission Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteTransferTypePermission", method = RequestMethod.GET)
	public ModelAndView deleteTransferTypePermission(@RequestParam(value = "id") Integer id,
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

			transferTypeProcessor.deleteTransferTypePermission(id);
			model.addAttribute(member);
			return new ModelAndView("redirect:/deleteTransferTypePermissionResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/deleteTransferTypePermissionResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/deleteTransferTypePermissionResult", method = RequestMethod.GET)
	public ModelAndView deleteTransferTypePermissionResult(
			@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Transfer Type Permission");
				model.addAttribute("message", "Permission has been deleted successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer Type Permission Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createFee", method = RequestMethod.GET)
	public ModelAndView createFee(@RequestParam(value = "transferTypeID") Integer id,
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
		List<String> listAccount = accountProcessor.listAllAccount(0, 1000);

		model.addAttribute(member);
		model.addAttribute("createFeeModel", new Fee());
		model.addAttribute("transferTypeID", id);
		model.addAttribute("listAccount", listAccount);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createFee");
	}

	@RequestMapping(value = "/createFeeForm", method = RequestMethod.POST)
	public ModelAndView createTransferTypePermissionForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createFeeModel") Fee fee, BindingResult result, ModelMap model) {
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

			Map<String, Object> trxMap = new HashMap<String, Object>();
			trxMap.put("data", fee);

			FeeRequest fees = new FeeRequest();

			fees.setName(fee.getName());
			fees.setDescription(fee.getDescription());
			fees.setTransferTypeID(fee.getTransferTypeID());

			if (fee.getFromMemberOpt() == 0) {
				fees.setFromMemberID(0);
			} else if (fee.getFromMemberOpt() == -1) {
				fees.setFromMemberID(-1);
			} else if (fee.getFromMemberOpt() == -2) {
				fees.setFromMemberID(-2);
			} else if (fee.getFromMemberOpt() == -3) {
				fees.setFromMemberID(-3);
			} else {
				LoadMembersResponse lmr = memberProcessor.loadMember(fee.getFromMember());
				if (lmr.getMembers().size() > 0) {
					fees.setFromMemberID(lmr.getMembers().get(0).getId());
				} else {
					model.addAttribute(member);
					return new ModelAndView("redirect:/createFeeResult?fault=INVALID_SOURCE_MEMBER", model);
				}
			}

			if (fee.getFromAccountOpt() == 0) {
				fees.setFromAccountID(0);
			} else if (fee.getFromAccountOpt() == -1) {
				fees.setFromAccountID(-1);
			} else if (fee.getFromAccountOpt() == -2) {
				fees.setFromAccountID(-2);
			} else if (fee.getFromAccountOpt() == -3) {
				fees.setFromAccountID(-3);
			} else {
				fees.setFromAccountID(Integer.valueOf(fee.getFromAccountName().split("-")[0].trim()));
			}

			if (fee.getToMemberOpt() == 0) {
				fees.setToMemberID(0);
			} else if (fee.getToMemberOpt() == -1) {
				fees.setToMemberID(-1);
			} else if (fee.getToMemberOpt() == -2) {
				fees.setToMemberID(-2);
			} else if (fee.getToMemberOpt() == -3) {
				fees.setToMemberID(-3);
			} else {
				LoadMembersResponse lmr = memberProcessor.loadMember(fee.getToMember());
				if (lmr.getMembers().size() > 0) {
					fees.setToMemberID(lmr.getMembers().get(0).getId());
				} else {
					model.addAttribute(member);
					return new ModelAndView("redirect:/createFeeResult?fault=INVALID_DESTINATION_MEMBER", model);
				}
			}

			if (fee.getToAccountOpt() == 0) {
				fees.setToAccountID(0);
			} else if (fee.getToAccountOpt() == -1) {
				fees.setToAccountID(-1);
			} else if (fee.getToAccountOpt() == -2) {
				fees.setToAccountID(-2);
			} else if (fee.getToAccountOpt() == -3) {
				fees.setToAccountID(-3);
			} else {
				fees.setToAccountID(Integer.valueOf(fee.getToAccountName().split("-")[0].trim()));
			}

			fees.setInitialRangeAmount(fee.getMinAmount());
			fees.setMaxRangeAmount(fee.getMaxAmount());

			if (fee.isPeriode()) {
				fees.setStartDate(Utils.stringDateToXML(fee.getStartDate()));
				fees.setEndDate(Utils.stringDateToXML(fee.getEndDate()));
			}

			fees.setDeductAmount(fee.isDeductAmount());
			fees.setFixedAmount(fee.getFixedAmount());
			fees.setPercentage(fee.getPercentage());
			fees.setEnabled(fee.isEnabled());
			fees.setPriority(fee.isPriority());

			fees.setFromAllMember(!fee.isAllowFromAllMember());
			fees.setToAllMember(!fee.isAllowToAllMember());

			transferTypeProcessor.createFee(fees);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createFeeResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createFeeResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createFeeResult", method = RequestMethod.GET)
	public ModelAndView createFeeResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Fee");
				model.addAttribute("message", "New Fee has been created successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Fee Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editFee", method = RequestMethod.GET)
	public ModelAndView editFee(@RequestParam(value = "id") Integer id,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException, TransactionException_Exception, ParseException {
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

		LoadFeesByIDResponse loadFeesByIDResponse = transferTypeProcessor.loadFeeByID(id);

		model.addAttribute("feeID", loadFeesByIDResponse.getFee().getId());
		model.addAttribute("name", loadFeesByIDResponse.getFee().getName());
		model.addAttribute("description", loadFeesByIDResponse.getFee().getDescription());
		model.addAttribute("fromMemberName", loadFeesByIDResponse.getFee().getFromMemberName());
		model.addAttribute("fromAccountName", loadFeesByIDResponse.getFee().getFromAccountName());
		model.addAttribute("toMemberName", loadFeesByIDResponse.getFee().getToMemberName());
		model.addAttribute("toAccountName", loadFeesByIDResponse.getFee().getToAccountName());
		model.addAttribute("deductAmount", loadFeesByIDResponse.getFee().isDeductAmount());
		model.addAttribute("fixedAmount", loadFeesByIDResponse.getFee().getFixedAmount());
		model.addAttribute("percentageValue", loadFeesByIDResponse.getFee().getPercentageValue());
		model.addAttribute("transferTypeID", loadFeesByIDResponse.getFee().getTransferTypeID());

		// 02/26/2019 9:36 PM

		if (loadFeesByIDResponse.getFee().getStartDate() != null) {
			model.addAttribute("startDate", formatDate(loadFeesByIDResponse.getFee().getStartDate()));
			model.addAttribute("feePeriode", true);
		} else {
			model.addAttribute("feePeriode", false);
		}

		if (loadFeesByIDResponse.getFee().getStartDate() != null) {
			model.addAttribute("endDate", formatDate(loadFeesByIDResponse.getFee().getEndDate()));
			model.addAttribute("feePeriode", true);
		} else {
			model.addAttribute("feePeriode", false);
		}

		model.addAttribute("filterSource", !loadFeesByIDResponse.getFee().isFromAllMember());
		model.addAttribute("filterDestination", !loadFeesByIDResponse.getFee().isToAllMember());

		model.addAttribute("initialRangeAmount", loadFeesByIDResponse.getFee().getInitialRangeAmount());
		model.addAttribute("maximumRangeAmount", loadFeesByIDResponse.getFee().getMaximumRangeAmount());
		model.addAttribute("priority", loadFeesByIDResponse.getFee().isPriority());
		model.addAttribute("enabled", loadFeesByIDResponse.getFee().isEnabled());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("editFee");
	}

	@RequestMapping(value = "/editFeeForm", method = RequestMethod.POST)
	public ModelAndView editFeeForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,

			@Valid @ModelAttribute("createFeeModel") Fee fee, BindingResult result, ModelMap model) {
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

			LoadFeesByIDResponse loadFeesByIDResponse = transferTypeProcessor.loadFeeByID(fee.getId());
			FeeRequest fees = new FeeRequest();
			fees.setDeductAmount(fee.isDeductAmount());
			fees.setDescription(fee.getDescription());
			fees.setEnabled(fee.isEnabled());

			if (fee.isPeriode() && fee.getStartDate().equalsIgnoreCase("Please Select Date") == false
					|| fee.isPeriode() && fee.getEndDate().equalsIgnoreCase("Please Select Date") == false) {
				fees.setStartDate(Utils.stringDateToXML(fee.getStartDate()));
				fees.setEndDate(Utils.stringDateToXML(fee.getEndDate()));
			}

			fees.setFromAccountID(loadFeesByIDResponse.getFee().getFromAccountID());
			fees.setFromAllMember(fee.isAllowFromAllMember());
			fees.setFromMemberID(loadFeesByIDResponse.getFee().getFromMemberID());
			fees.setInitialRangeAmount(fee.getMinAmount());
			fees.setMaxRangeAmount(fee.getMaxAmount());
			fees.setName(fee.getName());
			fees.setPercentage(fee.getPercentage());
			fees.setFixedAmount(fee.getFixedAmount());
			fees.setPriority(fee.isPriority());
			fees.setToAccountID(loadFeesByIDResponse.getFee().getToAccountID());
			fees.setToAllMember(fee.isAllowToAllMember());
			fees.setToMemberID(loadFeesByIDResponse.getFee().getToMemberID());
			fees.setTransferTypeID(fee.getTransferTypeID());

			transferTypeProcessor.editFee(fees);

			model.addAttribute(member);
			return new ModelAndView("redirect:/editFeeResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/editFeeResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/editFeeResult", method = RequestMethod.GET)
	public ModelAndView editFeeResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Update Fee");
				model.addAttribute("message", "Fee has been updated successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Update Fee Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/brokerDetail", method = RequestMethod.GET)
	public ModelAndView brokerDetail(@RequestParam(value = "id") Integer id,
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
		LoadBrokerResponse lbr = transferTypeProcessor.loadBrokerByID(id);

		LoadAccountsByIDResponse loadAccountsByIDResponse = accountProcessor
				.loadAccountDetail(lbr.getBrokers().get(0).getToAccountID());
		LoadMembersResponse loadMembersResponse = memberProcessor
				.loadMemberByID(lbr.getBrokers().get(0).getToMemberID());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);

		model.addAttribute("id", id);
		model.addAttribute("feeID", lbr.getBrokers().get(0).getFeeID());
		model.addAttribute("deductAllFee", lbr.getBrokers().get(0).isDeductAllFee());
		model.addAttribute("name", lbr.getBrokers().get(0).getName());
		model.addAttribute("description", lbr.getBrokers().get(0).getDescription());
		model.addAttribute("enabled", lbr.getBrokers().get(0).isEnabled());
		model.addAttribute("fixedAmount", lbr.getBrokers().get(0).getFixedAmount());
		model.addAttribute("percentageValue", lbr.getBrokers().get(0).getPercentageValue());

		if (lbr.getBrokers().get(0).getFromAccountID().equals(-1)) {
			model.addAttribute("fromAccount", "DESTINATION_ACCOUNT");
		} else {
			model.addAttribute("fromAccount", lbr.getBrokers().get(0).getFromAccountID());
		}

		if (lbr.getBrokers().get(0).getFromAccountID().equals(-1)) {
			model.addAttribute("fromMember", "DESTINATION_MEMBER");
		} else {
			model.addAttribute("fromMember", lbr.getBrokers().get(0).getFromMemberID());
		}

		model.addAttribute("toAccount", loadAccountsByIDResponse.getAccount().getName());
		model.addAttribute("toMember", loadMembersResponse.getMembers().get(0).getUsername());

		return new ModelAndView("brokerDetail");
	}

	@RequestMapping(value = "/createBroker", method = RequestMethod.GET)
	public ModelAndView createBroker(@RequestParam(value = "feeID") Integer id,
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
		List<String> listAccount = accountProcessor.listAllAccount(0, 1000);

		model.addAttribute(member);
		model.addAttribute("createBrokerModel", new Fee());
		model.addAttribute("feeID", id);
		model.addAttribute("listAccount", listAccount);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createBroker");
	}

	@RequestMapping(value = "/createBrokerForm", method = RequestMethod.POST)
	public ModelAndView createBrokerForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createBrokerModel") Broker broker, BindingResult result, ModelMap model) {
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

			transferTypeProcessor.createBroker(broker);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createBrokerResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createBrokerResult?fault=" + ex.getMessage(), model);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/brokerData", method = RequestMethod.GET)
	public String brokerData(@RequestParam(value = "id") Integer feeID,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "length", required = false) Integer length)
			throws MalformedURLException, TransactionException_Exception {
		String jsonData = transferTypeProcessor.loadBroker(feeID);
		return jsonData;
	}

	@RequestMapping(value = "/createBrokerResult", method = RequestMethod.GET)
	public ModelAndView createBrokerResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Broker");
				model.addAttribute("message", "New Broker has been created successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Broker Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editBroker", method = RequestMethod.GET)
	public ModelAndView editBroker(@RequestParam(value = "id") Integer id,
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

		LoadBrokerResponse loadBrokerResponse = transferTypeProcessor.loadBrokerByID(id);
		LoadAccountsByIDResponse loadAccountsByIDResponse = accountProcessor
				.loadAccountDetail(loadBrokerResponse.getBrokers().get(0).getToAccountID());
		LoadMembersResponse loadMembersResponse = memberProcessor
				.loadMemberByID(loadBrokerResponse.getBrokers().get(0).getToMemberID());

		model.addAttribute(member);
		model.addAttribute("createBrokerModel", new Broker());
		model.addAttribute("id", id);
		model.addAttribute("feeID", loadBrokerResponse.getBrokers().get(0).getFeeID());
		model.addAttribute("name", loadBrokerResponse.getBrokers().get(0).getName());
		model.addAttribute("description", loadBrokerResponse.getBrokers().get(0).getDescription());
		model.addAttribute("toMember", loadMembersResponse.getMembers().get(0).getUsername());
		model.addAttribute("listAccount", loadAccountsByIDResponse.getAccount().getId() + " - "
				+ loadAccountsByIDResponse.getAccount().getName());
		model.addAttribute("deductAllFee", loadBrokerResponse.getBrokers().get(0).isDeductAllFee());
		model.addAttribute("fixedAmount", loadBrokerResponse.getBrokers().get(0).getFixedAmount());
		model.addAttribute("percentage", loadBrokerResponse.getBrokers().get(0).getPercentageValue());
		model.addAttribute("enabled", loadBrokerResponse.getBrokers().get(0).isEnabled());

		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("editBroker");
	}

	@RequestMapping(value = "/editBrokerForm", method = RequestMethod.POST)
	public ModelAndView editBrokerForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createBrokerModel") Broker broker, BindingResult result, ModelMap model) {
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

			transferTypeProcessor.editBroker(broker);

			model.addAttribute(member);
			return new ModelAndView("redirect:/editBrokerResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/editBrokerResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/editBrokerResult", method = RequestMethod.GET)
	public ModelAndView editBrokerResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Broker");
				model.addAttribute("message", "Broker has been updated successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Update Broker Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/feeMemberFilterData", method = RequestMethod.GET)
	public String feeMemberFilterData(@RequestParam(value = "feeID") Integer feeID)
			throws MalformedURLException, TransactionException_Exception {
		String jsonData = transferTypeProcessor.loadFeeByMember(feeID);
		return jsonData;
	}

	@RequestMapping(value = "/deleteFeeMemberFilter", method = RequestMethod.GET)
	public ModelAndView deleteFeeMemberFilter(@RequestParam(value = "id") Integer id,
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

		try {
			transferTypeProcessor.deleteFeeByMember(id);
			return new ModelAndView("redirect:/deleteFeeMemberFilterResult");
		} catch (TransactionException_Exception e) {
			return new ModelAndView("redirect:/deleteFeeMemberFilterResult?fault=" + e.getMessage());
		}
	}

	@RequestMapping(value = "/deleteFeeMemberFilterResult", method = RequestMethod.GET)
	public ModelAndView deleteFeeMemberFilterResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Delete Member Fee Filtering");
				model.addAttribute("message", "Member Fee Filtering has been deleted successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Delete Member Fee Filtering Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	public static String formatDate(XMLGregorianCalendar xmlcalendar) {
		SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		GregorianCalendar calendar = xmlcalendar.toGregorianCalendar();
		fmt.setCalendar(calendar);
		String dateFormatted = fmt.format(calendar.getTime());
		return dateFormatted;
	}

	@RequestMapping(value = "/createTransferNotification", method = RequestMethod.GET)
	public ModelAndView createTransferNotification(@RequestParam(value = "id") Integer id,
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
		model.addAttribute("createTransferNotificationModel", new TransferNotification());
		model.addAttribute("transferTypeID", id);
		model.addAttribute("listNotif", listNotif);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createTransferNotification");
	}

	@RequestMapping(value = "/createTransferNotificationForm", method = RequestMethod.POST)
	public ModelAndView createTransferNotificationForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createTransferNotificationModel") TransferNotification notif, BindingResult result,
			ModelMap model) {
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

			transferTypeProcessor.createTransferNotification(notif);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createTransferNotificationResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createTransferNotificationResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createTransferNotificationResult", method = RequestMethod.GET)
	public ModelAndView createTransferNotificationResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Transfer Notification");
				model.addAttribute("message", "Transfer Notification has been created successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transfer Notification Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editTransferNotification", method = RequestMethod.GET)
	public ModelAndView editTransferNotification(@RequestParam(value = "id") Integer id,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws MalformedURLException, TransactionException_Exception {
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
		// List<String> listNotif = notificationProcessor.getListNotification(0, 100);

		TransferTypeNotificationResponse notifResponse = transferTypeProcessor.loadTransferNotificationByID(id);

		model.addAttribute(member);
		model.addAttribute("createTransferNotificationModel", new TransferNotification());
		model.addAttribute("id", id);
		model.addAttribute("transferTypeID", notifResponse.getNotification().get(0).getTransferTypeID());
		model.addAttribute("notificationType", notifResponse.getNotification().get(0).getNotificationType());
		model.addAttribute("listNotif", notifResponse.getNotification().get(0).getNotificationID() + " - "
				+ notifResponse.getNotification().get(0).getNotificationName());
		model.addAttribute("enabled", notifResponse.getNotification().get(0).isEnabled());
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("editTransferNotification");
	}

	@RequestMapping(value = "/editTransferNotificationForm", method = RequestMethod.POST)
	public ModelAndView editTransferNotificationForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createTransferNotificationModel") TransferNotification notif, BindingResult result,
			ModelMap model) {
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

			transferTypeProcessor.editTransferNotification(notif);

			model.addAttribute(member);
			return new ModelAndView("redirect:/editTransferNotificationResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/editTransferNotificationResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/editTransferNotificationResult", method = RequestMethod.GET)
	public ModelAndView editTransferNotificationResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Update Transfer Notification");
				model.addAttribute("message", "Transfer Notification has been updated successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Update Transfer Notification Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteTransferNotificationForm", method = RequestMethod.GET)
	public ModelAndView deleteTransferNotificationForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "id") Integer id, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}

			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);

			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			transferTypeProcessor.deleteTransferNotification(id);
			return new ModelAndView("redirect:/deleteTransferNotificationResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/deleteTransferNotificationResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/deleteTransferNotificationResult", method = RequestMethod.GET)
	public ModelAndView deleteTransferNotificationResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Transfer Notification");
				model.addAttribute("message", "Transfer notification has been deleted successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Delete Transfer Notification Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createMemberFiltering", method = RequestMethod.GET)
	public ModelAndView createMemberFiltering(@RequestParam(value = "feeID") Integer id,
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
		model.addAttribute("creatememberfilteringmodel", new MemberFiltering());
		model.addAttribute("feeID", id);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createMemberFiltering");
	}

	@RequestMapping(value = "/createMemberFilteringForm", method = RequestMethod.POST)
	public ModelAndView createMemberFilteringForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createTransferNotificationModel") MemberFiltering filter, BindingResult result,
			ModelMap model) {
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

			transferTypeProcessor.createFeeByMember(filter);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createMemberFilteringResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createMemberFilteringResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createMemberFilteringResult", method = RequestMethod.GET)
	public ModelAndView createMemberFilteringResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Member Filtering");
				model.addAttribute("message", "Member Filtering has been created successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Add Member Filtering Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteMemberFilteringForm", method = RequestMethod.GET)
	public ModelAndView deleteMemberFilteringForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "id") Integer id, ModelMap model) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}

			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);

			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			transferTypeProcessor.deleteFeeByMember(id);

			model.addAttribute(member);
			return new ModelAndView("redirect:/deleteMemberFilteringResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/deleteMemberFilteringResult?fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/deleteMemberFilteringResult", method = RequestMethod.GET)
	public ModelAndView deleteMemberFilteringResult(@RequestParam(value = "fault", required = false) String fault,
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

			if (fault == null) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Member Filtering");
				model.addAttribute("message", "Member Filtering has been deleted successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Delete Member Filtering Failed");
				model.addAttribute("message", fault);
			}

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
}
