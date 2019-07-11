package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import core.finance.host.services.ws.accounts.AccountPermissions;
import core.finance.host.services.ws.accounts.LoadAccountPermissionsResponse;
import core.finance.host.services.ws.accounts.LoadAccountsByIDResponse;
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
import com.doku.core.finance.admin.model.Account;
import com.doku.core.finance.admin.model.AccountPermission;
import com.doku.core.finance.admin.model.AdminMenu;
import com.doku.core.finance.admin.model.Member;

@Controller
public class AccountController {

	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private MenuProcessor menuProcessor;
	@Autowired
	private AccountProcessor accountProcessor;
	@Autowired
	private MessageProcessor messageProcessor;
	@Autowired
	private GroupProcessor groupProcessor;

	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public ModelAndView account(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
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

			return new ModelAndView("account");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/accountDataAll", method = RequestMethod.GET)
	public String accountData(@RequestParam(value = "start") Integer start,
			@RequestParam(value = "length") Integer length) throws MalformedURLException {
		String jsonData = accountProcessor.loadAllAccount(Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/accountDetail", method = RequestMethod.GET)
	public ModelAndView accountDetail(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "id") Integer id, Model model) {
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
			LoadAccountsByIDResponse labid = accountProcessor.loadAccountDetail(id);

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			model.addAttribute("id", labid.getAccount().getId());
			model.addAttribute("name", labid.getAccount().getName());
			model.addAttribute("description", labid.getAccount().getDescription());
			model.addAttribute("systemAccount", labid.getAccount().isSystemAccount());
			model.addAttribute("currency", labid.getAccount().getCurrency().getName());
			model.addAttribute("date", labid.getAccount().getCreatedDate());

			return new ModelAndView("accountDetail");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createAccount", method = RequestMethod.GET)
	public ModelAndView createAccount(
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
			List<String> listCurrency = accountProcessor.getListCurrency();

			model.addAttribute(member);
			model.addAttribute("listCurrency", listCurrency);
			model.addAttribute("createAccountModel", new Account());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("createAccount");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createAccountForm", method = RequestMethod.POST)
	public ModelAndView createAccountForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createAccountModel") Account account, BindingResult result, ModelMap model) {
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

			accountProcessor.createAccount(account);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createAccountResult?status=0", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createAccountResult?status=2&fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createAccountResult", method = RequestMethod.GET)
	public ModelAndView createAccountResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "fault", required = false) String fault, ModelMap model) {
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
			if (status == 0) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Account");
				model.addAttribute("message", "New Account has been created successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Account Failed");
				model.addAttribute("message", fault);
			}
			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("account");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editAccount", method = RequestMethod.GET)
	public ModelAndView editAccount(@RequestParam(value = "id") Integer accountID,
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
			List<String> listCurrency = accountProcessor.getListCurrency();
			LoadAccountsByIDResponse loadAccountsByIDResponse = accountProcessor.loadAccountDetail(accountID);

			model.addAttribute(member);
			model.addAttribute("listCurrency", listCurrency);
			model.addAttribute("accountID", accountID);
			model.addAttribute("currencyID", loadAccountsByIDResponse.getAccount().getCurrency().getId());
			model.addAttribute("currencyName", loadAccountsByIDResponse.getAccount().getCurrency().getId() + " - "
					+ loadAccountsByIDResponse.getAccount().getCurrency().getName());
			model.addAttribute("name", loadAccountsByIDResponse.getAccount().getName());
			model.addAttribute("description", loadAccountsByIDResponse.getAccount().getDescription());
			model.addAttribute("systemAccount", loadAccountsByIDResponse.getAccount().isSystemAccount());
			model.addAttribute("createAccountModel", new Account());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editAccount");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editAccountForm", method = RequestMethod.POST)
	public ModelAndView editAccountForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createAccountModel") Account account, BindingResult result, ModelMap model) {
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

			accountProcessor.editAccount(account);

			model.addAttribute(member);
			return new ModelAndView("redirect:/editAccountResult?status=0", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/editAccountResult?status=2&fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/editAccountResult", method = RequestMethod.GET)
	public ModelAndView editAccountResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "fault", required = false) String fault, ModelMap model) {
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
			if (status == 0) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Account");
				model.addAttribute("message", "Account has been updated successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Update Account Failed");
				model.addAttribute("message", fault);
			}
			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("account");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/accountPermissionData", method = RequestMethod.GET)
	public String accountPermissionData(@RequestParam(value = "id") Integer id) throws MalformedURLException {
		String jsonData = accountProcessor.loadAccountPermission(id);
		return jsonData;
	}

	@RequestMapping(value = "/createAccountPermission", method = RequestMethod.GET)
	public ModelAndView createAccountPermission(@RequestParam(value = "id") Integer accountID,
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
			List<String> listGroup = groupProcessor.getListGroup();

			model.addAttribute(member);
			model.addAttribute("listGroup", listGroup);
			model.addAttribute("accountid", accountID);
			model.addAttribute("createAccountPermissionModel", new AccountPermission());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("createAccountPermission");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createAccountPermissionForm", method = RequestMethod.POST)
	public ModelAndView createAccountPermissionForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createAccountPermissionModel") AccountPermission accountPermission,
			BindingResult result, ModelMap model) {
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

			accountProcessor.createAccountPermission(accountPermission);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createAccountPermissionResult?status=0");
		} catch (Exception ex) {
			ex.printStackTrace();
			if (ex.getMessage().equalsIgnoreCase("For input string: \"SELECT GROUP\"")) {
				return new ModelAndView(
						"redirect:/createAccountPermissionResult?status=2&fault=Please%20Select%20Group");
			} else {
				return new ModelAndView("redirect:/createAccountPermissionResult?status=2&fault=" + ex.getMessage());
			}
		}
	}

	@RequestMapping(value = "/createAccountPermissionResult", method = RequestMethod.GET)
	public ModelAndView createAccountPermissionResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "fault", required = false) String fault, ModelMap model) {
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
			if (status == 0) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Account Permission");
				model.addAttribute("message", "Account Permission has been added successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Account Permission Failed");
				model.addAttribute("message", fault);
			}
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("account");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editAccountPermission", method = RequestMethod.GET)
	public ModelAndView editAccountPermission(@RequestParam(value = "id") Integer id,
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

			LoadAccountPermissionsResponse accountPermissions = accountProcessor.loadAccountPermissionByID(id);
			AccountPermissions accPermission = accountPermissions.getAccountPermission().get(0);

			model.addAttribute(member);
			model.addAttribute("permissionID", id);
			model.addAttribute("creditLimit", accPermission.getAccount().getCreditLimit());
			model.addAttribute("upperCreditLimit", accPermission.getAccount().getUpperCreditLimit());
			model.addAttribute("lowerCreditLimit", accPermission.getAccount().getLowerCreditLimit());
			model.addAttribute("groupID", accPermission.getAccount().getGroup().getId());
			model.addAttribute("groupName", accPermission.getAccount().getGroup().getName());
			model.addAttribute("editAccountPermissionModel", new AccountPermission());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editAccountPermission");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editAccountPermissionForm", method = RequestMethod.POST)
	public ModelAndView editAccountPermissionForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createAccountPermissionModel") AccountPermission accountPermission,
			BindingResult result, ModelMap model) {
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

			accountProcessor.editAccountPermission(accountPermission);

			model.addAttribute(member);
			return new ModelAndView("redirect:/editAccountPermissionResult?status=0");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/editAccountPermissionResult?status=2&fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/editAccountPermissionResult", method = RequestMethod.GET)
	public ModelAndView editAccountPermissionResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "fault", required = false) String fault, ModelMap model) {
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
			if (status == 0) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Account Permission");
				model.addAttribute("message", "Account Permission has been updated successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Account Permission Failed");
				model.addAttribute("message", fault);
			}
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("account");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteAccountPermission", method = RequestMethod.GET)
	public ModelAndView deleteAccountPermission(@RequestParam(value = "id") Integer id,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, ModelMap model,
			HttpServletRequest request) {
		try {
			if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
				return new ModelAndView("redirect:/login");
			}

			IMap<String, Member> memberMap = instance.getMap("Member");
			Member member = memberMap.get(sessionID);

			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			accountProcessor.deleteAccountPermission(id);
			return new ModelAndView("redirect:/deleteAccountPermissionResult?status=0");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/deleteAccountPermissionResult", method = RequestMethod.GET)
	public ModelAndView deleteAccountPermissionResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "fault", required = false) String fault, ModelMap model) {
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

			if (status == 0) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Account Permission");
				model.addAttribute("message", "Account Permission has been deleted successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Account Permission Failed");
				model.addAttribute("message", fault);
			}
			return new ModelAndView("account");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
}
