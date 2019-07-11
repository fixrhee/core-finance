package com.doku.core.finance.admin.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.FilenameUtils;
import core.finance.host.services.ws.access.CredentialStatusResponse;
import core.finance.host.services.ws.access.LoadAccessTypeResponse;
import core.finance.host.services.ws.access.ValidateCredentialResponse;
import core.finance.host.services.ws.accounts.AccountPermissions;
import core.finance.host.services.ws.accounts.CurrencyResponse;
import core.finance.host.services.ws.accounts.LoadAccountPermissionsResponse;
import core.finance.host.services.ws.accounts.LoadAccountsByIDResponse;
import core.finance.host.services.ws.accounts.TransactionException_Exception;
import core.finance.host.services.ws.members.ConfirmKYCResponse;
import core.finance.host.services.ws.members.Exception_Exception;
import core.finance.host.services.ws.members.LoadMembersResponse;
import core.finance.host.services.ws.transfers.InquiryResponse;
import core.finance.host.services.ws.transfers.PaymentResponse;
import core.finance.host.services.ws.transfers.ReversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.doku.core.finance.admin.model.AccessType;
import com.doku.core.finance.admin.model.Account;
import com.doku.core.finance.admin.model.AccountPermission;
import com.doku.core.finance.admin.model.AdminMenu;
import com.doku.core.finance.admin.model.ChangeCredential;
import com.doku.core.finance.admin.model.Currency;
import com.doku.core.finance.admin.model.Group;
import com.doku.core.finance.admin.model.Member;
import com.doku.core.finance.admin.model.MemberCredential;
import com.doku.core.finance.admin.model.ReversePayment;
import com.doku.core.finance.admin.model.SendMessage;
import com.doku.core.finance.admin.model.Transaction;
import com.doku.core.finance.admin.model.UpgradeMember;
import com.doku.core.finance.admin.model.UploadFiles;

@Controller
public class DashboardController {

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

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView login(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) throws MalformedURLException {
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
		String welcomeMenu = adminMenu.getWelcomeMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("welcomeMenu", welcomeMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) throws MalformedURLException {
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

		return new ModelAndView("search");
	}

	@RequestMapping(value = "/accountSelect", method = RequestMethod.GET)
	public ModelAndView accountSelect(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) throws MalformedURLException {
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
		List<String> accounts = accountProcessor.getAccountFromGroupID(member.getGroupID(), member.getUsername());
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("account", accounts);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);

		return new ModelAndView("accountSelect");
	}

	@RequestMapping(value = "/transactionHistory", method = RequestMethod.GET)
	public ModelAndView transactionHistory(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, @RequestParam(value = "GroupID") Integer groupID,
			@RequestParam(value = "username") String username, @RequestParam(value = "AccountID") String accountID,
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
		Map<String, Object> accountDetails = accountProcessor.getAccountDetails(username, Integer.valueOf(accountID),
				groupID);
		Map<String, Object> getMemberDetails = memberProcessor.getMemberDetails(username);

		model.addAttribute(member);
		model.addAttribute("username", getMemberDetails.get("username"));
		model.addAttribute("groupID", getMemberDetails.get("groupID"));
		model.addAttribute("groupName", getMemberDetails.get("groupName"));
		model.addAttribute("name", getMemberDetails.get("name"));
		model.addAttribute("email", getMemberDetails.get("email"));
		model.addAttribute("accountID", accountID);
		model.addAttribute("accountName", accountDetails.get("accountName"));
		model.addAttribute("fromDate", Utils.GetDate("yyyy-MM-dd"));
		model.addAttribute("toDate", Utils.GetFutureDate("yyyy-MM-dd", 1));
		model.addAttribute("creditLimit", accountDetails.get("creditLimit"));
		model.addAttribute("upperCreditLimit", accountDetails.get("upperCreditLimit"));
		model.addAttribute("balance", accountDetails.get("balance"));
		model.addAttribute("reservedAmount", accountDetails.get("reservedAmount"));
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);

		if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
			return new ModelAndView("transactionHistoryAdmin");
		} else {
			return new ModelAndView("transactionHistory");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/transactionHistoryData", method = RequestMethod.GET)
	public String transactionHistoryData(@RequestParam(value = "accountID") Integer accountID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "username") String username, @RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate,
			@RequestParam(value = "search[value]", required = false) String search) throws MalformedURLException {

		String jsonData;
		if (search == "") {
			jsonData = accountProcessor.getTransactionHistory(username, accountID, fromDate, toDate, start, length);
		} else {
			jsonData = accountProcessor.searchTransactionHistory(username, accountID, fromDate, toDate, start, length,
					search);
		}
		return jsonData;
	}

	@RequestMapping(value = "/reverseInquiry", method = RequestMethod.GET)
	public ModelAndView reverseInquiry(@Valid @RequestParam("traceNumber") String tracenumber,
			@RequestParam(value = "amount") String amount, @RequestParam(value = "toFromMember") String toFromMember,
			@RequestParam(value = "traceNumber") String traceNumber,
			@RequestParam("transactionDate") String transactionDate, @RequestParam(value = "remark") String remark,
			@RequestParam(value = "transactionNumber") String transactionNumber,
			@RequestParam(value = "description") String description,
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
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

			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("traceNumber", traceNumber);
				model.addAttribute("amount", amount);
				model.addAttribute("transactionDate", transactionDate);
				model.addAttribute("toFromMember", toFromMember);
				model.addAttribute("remark", remark);
				model.addAttribute("transactionNumber", transactionNumber);
				model.addAttribute("description", description);
				return new ModelAndView("reversePayment");
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Reversal");
				model.addAttribute("message", "UNAUTHORIZED_MEMBER_ACCESS : You don't have access to specified member");
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("member");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/reversePayment", method = RequestMethod.POST)
	public ModelAndView reversePayment(@Valid @ModelAttribute("reversepayment") ReversePayment reverse,
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
			ValidateCredentialResponse vcRes = accessProcessor.validateCredential(member.getUsername(),
					reverse.getCredential());

			if (vcRes.getStatus().getMessage().equalsIgnoreCase("VALID")) {
				ReversalResponse reversalRes = paymentProcessor.reversePayment(reverse.getTransactionNumber());
				if (reversalRes.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
					model.addAttribute(member);
					model.addAttribute("notification", "success");
					model.addAttribute("title", "Reversal");
					model.addAttribute("message", "Transaction has been reversed successfully");
					model.addAttribute("mainMenu", mainMenu);
					model.addAttribute("unreadMessage", unread);
					model.addAttribute("messageSummary", messageSummary);
					model.addAttribute("traceNumber", reverse.getTracenumber());
					model.addAttribute("amount", reverse.getAmount());
					model.addAttribute("transactionDate", reverse.getTransactionDate());
					model.addAttribute("toFromMember", reverse.getToFromMember());
					model.addAttribute("remark", reverse.getRemark());
					model.addAttribute("transactionNumber", reverse.getTransactionNumber());
					model.addAttribute("description", reverse.getDescription());
					return new ModelAndView("memberAdmin");
				} else {
					model.addAttribute(member);
					model.addAttribute("notification", "error");
					model.addAttribute("title", "Reversal");
					model.addAttribute("message",
							reversalRes.getStatus().getDescription() + ": " + reversalRes.getStatus().getMessage());
					model.addAttribute("mainMenu", mainMenu);
					model.addAttribute("unreadMessage", unread);
					model.addAttribute("messageSummary", messageSummary);
					return new ModelAndView("memberAdmin");
				}
			} else {
				model.addAttribute(member);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Reversal");
				model.addAttribute("message",
						vcRes.getStatus().getDescription() + ": " + vcRes.getStatus().getMessage());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				return new ModelAndView("memberAdmin");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public ModelAndView message(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "MessageID", required = false) String messageID,
			@RequestParam(value = "Action", required = false) String action, Model model)
			throws NumberFormatException, MalformedURLException {
		if (sessionID.equalsIgnoreCase("defaultCookieValue") || sessionID.equalsIgnoreCase(null)) {
			return new ModelAndView("redirect:/login");
		}
		IMap<String, Member> memberMap = instance.getMap("Member");
		Member member = memberMap.get(sessionID);
		if (member == null) {
			return new ModelAndView("redirect:/login");
		}
		String messageContent = "<div class=\"col-sm-9 mail_view\"><div class=\"inbox-body\"><div class=\"mail_heading row\">"
				+ "<div class=\"col-md-8\"><div class=\"btn-group\"><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Print\"><i class=\"fa fa-print\"></i></button><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Trash\"><i class=\"fa fa-trash-o\"></i></button></div></div>"
				+ "<div class=\"col-md-4 text-right\"><p class=\"date\"></p></div><div class=\"col-md-12\"><h4></h4></div></div>"
				+ "<div class=\"sender-info\"><div class=\"row\"><div class=\"col-md-12\"></div></div></div><div class=\"view-mail\"></div><br/>"
				+ "<div class=\"btn-group\"><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Print\"><i class=\"fa fa-print\"></i></button><button class=\"btn btn-sm btn-default\" type=\"button\" disabled data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Trash\"><i class=\"fa fa-trash-o\"></i></button></div></div></div>";

		if (messageID != null) {
			if (action != null && action.equalsIgnoreCase("delete")) {
				messageProcessor.deleteMessage(Integer.valueOf(messageID));
			} else {
				messageContent = messageProcessor.loadMessageByID(Integer.valueOf(messageID), sessionID);
			}
		}

		AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
		List<String> mainMenu = adminMenu.getMainMenu();
		Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
		String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
		List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
		List<String> messageList = messageProcessor.loadMessages(member.getUsername(), sessionID);

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		model.addAttribute("messageList", messageList);
		model.addAttribute("messageContent", messageContent);
		return new ModelAndView("message");
	}

	@RequestMapping(value = "/composeMessage", method = RequestMethod.GET)
	public ModelAndView composeMessage(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, Model model)
			throws NumberFormatException, MalformedURLException {
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
		List<String> messageList = messageProcessor.loadMessages(member.getUsername(), sessionID);

		model.addAttribute(member);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		model.addAttribute("messageList", messageList);
		return new ModelAndView("sendMessage");
	}

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public ModelAndView sendMessage(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("sendmessage") SendMessage sendMessage, Model model) throws NumberFormatException,
			MalformedURLException, core.finance.host.services.ws.message.Exception_Exception {
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
			List<String> messageList = messageProcessor.loadMessages(member.getUsername(), sessionID);

			LoadMembersResponse loadMembersRes = memberProcessor.loadMember(sendMessage.getToMember());
			if (loadMembersRes.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				messageProcessor.sendMessage(member.getUsername(), sendMessage.getToMember(), sendMessage.getSubject(),
						sendMessage.getBody());
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("messageList", messageList);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Send Message");
				model.addAttribute("message", "Your message has been send");
				return new ModelAndView("message");
			} else {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("messageList", messageList);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Send Message");
				model.addAttribute("message", loadMembersRes.getStatus().getDescription());
				return new ModelAndView("message");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public ModelAndView setting(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			Model model) throws MalformedURLException {
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
			model.addAttribute("name", member.getName());
			model.addAttribute("email", member.getEmail());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("setting");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editProfile", method = RequestMethod.POST)
	public ModelAndView editProfile(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editmember") UpgradeMember editmember, ModelMap model) {
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
			memberProcessor.editProfile(editmember);

			model.addAttribute(member);
			model.addAttribute("email", editmember.getEmail());
			model.addAttribute("name", editmember.getName());
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Edit Profile");
			model.addAttribute("message", "Profile has been updated successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("setting");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transferMember", method = RequestMethod.GET)
	public ModelAndView transferToMember(
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

			// List<String> listMember;
			// if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
			// listMember = memberProcessor.getListAllMember(member.getUsername());
			// } else {
			// listMember = memberProcessor.getListMember(member.getUsername());
			// }
			// model.addAttribute("listMember", listMember);

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transferMember");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/accountData", method = RequestMethod.GET)
	public String accountData(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "groupID", required = false) Integer groupID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws MalformedURLException {
		String jsonData = accountProcessor.loadAccountMember(groupID, Integer.valueOf(start), Integer.valueOf(length));
		return jsonData;
	}

	@RequestMapping(value = "/changeCredential", method = RequestMethod.GET)
	public ModelAndView changeCredential(
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
			model.addAttribute("username", member.getUsername());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("changeCredential");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/changeCredentialResult", method = RequestMethod.POST)
	public ModelAndView changeCredentialResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("changecredential") ChangeCredential changecredential, ModelMap model) {
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
			String welcomeMenu = adminMenu.getWelcomeMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(changecredential.getMsisdn());
			accessProcessor.changeCredential(changecredential);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Change Credential");
			model.addAttribute("message", "Credential has been updated successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			model.addAttribute("welcomeMenu", welcomeMenu);

			return new ModelAndView("index");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/topupMember", method = RequestMethod.GET)
	public ModelAndView topupMember(
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
		return new ModelAndView("topupMember");
	}

	@RequestMapping(value = "/cashoutMember", method = RequestMethod.GET)
	public ModelAndView cashoutMember(
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
		return new ModelAndView("cashoutMember");
	}

	@RequestMapping(value = "/searchMember", method = RequestMethod.GET)
	public ModelAndView searchMember(@Valid @ModelAttribute("searchmember") UpgradeMember search,
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
			Map<String, Object> memberDetails = memberProcessor.getMemberDetails(search.getMsisdn());

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("searchMember");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/upgradeMember", method = RequestMethod.POST)
	public ModelAndView upgradeMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("uploadFiles") UploadFiles uploadFiles,
			@Valid @ModelAttribute("upgrademember") UpgradeMember upgradeMember, Model model)
			throws IllegalStateException, IOException, Exception_Exception, DatatypeConfigurationException {
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

			List<MultipartFile> files = uploadFiles.getFiles();
			List<String> fileNames = new ArrayList<String>();
			int count = 0;
			if (null != files && files.size() > 0) {
				for (MultipartFile multipartFile : files) {
					count = count + 1;
					byte[] bytes = multipartFile.getBytes();
					String fileName = multipartFile.getOriginalFilename();
					if (!"".equalsIgnoreCase(fileName)) {
						fileNames.add(fileName);
						String rootPath = contextLoader.getPathFile();
						try {
							File serverFile = new File(rootPath + count + "-" + upgradeMember.getMsisdn() + "."
									+ FilenameUtils.getExtension(fileName));

							BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
							stream.write(bytes);
							stream.close();

							BufferedImage originalImage = ImageIO.read(serverFile);
							int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
									: originalImage.getType();

							BufferedImage resizedImage = new BufferedImage(500, 500, type);
							Graphics2D g = resizedImage.createGraphics();
							g.drawImage(originalImage, 0, 0, 500, 500, null);
							g.dispose();
							ImageIO.write(resizedImage, "png", serverFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			memberProcessor.requestKycMember(upgradeMember, uploadFiles);

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "KYC Member");
			model.addAttribute("message", "Member KYC has been requested successfully");

			if (member.getGroupName().equalsIgnoreCase("ADMIN")) {
				return new ModelAndView("memberKycAdmin");
			} else {
				return new ModelAndView("memberKyc");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editKyc", method = RequestMethod.GET)
	public ModelAndView editKyc(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "id") int id, Model model) throws Exception_Exception, IOException {
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
			Map<String, Object> memberDetails = memberProcessor.getMemberKycDetails(id);

			model.addAttribute(member);
			model.addAllAttributes(memberDetails);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editKyc");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=confirmkyc")
	public ModelAndView detailKyc(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("confirmkyc") UpgradeMember upgradeMember, Model model)
			throws Exception_Exception, IOException {
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
		Map<String, Object> memberDetails = memberProcessor.getMemberKycDetails(upgradeMember.getId());

		model.addAttribute(member);
		model.addAllAttributes(memberDetails);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		if (member.getGroupName().equals("ADMIN")) {
			return new ModelAndView("detailKycAdmin");
		} else {
			return new ModelAndView("detailKyc");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=approve")
	public ModelAndView approveKYC(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("confirmkyc") UpgradeMember upgradeMember, Model model)
			throws IllegalStateException, IOException, Exception_Exception {
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
			ConfirmKYCResponse confirmKYCResponse = memberProcessor.approveKycRequest(upgradeMember.getId(),
					member.getUsername());

			if (confirmKYCResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message", "Member KYC has been confirmed successfully");
			} else {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message",
						"KYC member confirmed has been failed : " + confirmKYCResponse.getStatus().getMessage());
			}
			return new ModelAndView("memberKycAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=reject")
	public ModelAndView rejectKYC(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("confirmkyc") UpgradeMember upgradeMember, Model model)
			throws IllegalStateException, IOException, Exception_Exception {
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
			ConfirmKYCResponse confirmKYCResponse = memberProcessor.rejectKycRequest(upgradeMember.getId(),
					member.getUsername(), upgradeMember.getDescription());

			if (confirmKYCResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "success");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message", "Member KYC has been confirmed successfully");
			} else {
				model.addAttribute(member);
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute("notification", "error");
				model.addAttribute("title", "KYC Member");
				model.addAttribute("message",
						"KYC member confirmed has been failed : " + confirmKYCResponse.getStatus().getMessage());
			}
			return new ModelAndView("memberKycAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/kyc", method = RequestMethod.POST, params = "confirm=edit")
	public ModelAndView editMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editmember") UpgradeMember editmember, ModelMap model) {
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
			memberProcessor.editMember(editmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Edit Member");
			model.addAttribute("message", "Profile has been updated successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			if (editmember.getType().equalsIgnoreCase("member")) {
				return new ModelAndView("member");
			} else {
				return new ModelAndView("memberKycAdmin");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=unblockPin")
	public ModelAndView unblockCredential(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("accessmember") UpgradeMember accessmember, ModelMap model) {
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
			accessProcessor.unblockCredential(accessmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Unblock PIN");
			model.addAttribute("message", "Unblock PIN was successfull");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);

			return new ModelAndView("memberAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=resetPin")
	public ModelAndView changeCredential(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("accessmember") UpgradeMember accessmember, ModelMap model) {
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
			accessProcessor.resetCredential(accessmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Reset PIN");
			model.addAttribute("message", "Reset PIN has been submitted");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("memberAdmin");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=changeGroup")
	public ModelAndView changeGroup(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("accessmember") UpgradeMember accessmember, ModelMap model) {
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
			Map<String, Object> memberDetails = memberProcessor.getMemberDetails(accessmember.getMsisdn());
			List<String> listGroup = groupProcessor.getListGroup();

			model.addAttribute(member);
			model.addAttribute("username", accessmember.getMsisdn());
			model.addAttribute("groupName", memberDetails.get("groupName"));
			model.addAttribute("name", memberDetails.get("name"));
			model.addAttribute("email", memberDetails.get("email"));
			model.addAttribute("listGroup", listGroup);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("changeGroup");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/changeGroup", method = RequestMethod.POST)
	public ModelAndView confirmChangeGroup(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("upgrademember") UpgradeMember accessmember, ModelMap model) {
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
			memberProcessor.changeGroup(accessmember);

			model.addAttribute(member);
			model.addAttribute("notification", "success");
			model.addAttribute("title", "Change Group");
			model.addAttribute("message", "Group has been updated successfully");
			model.addAttribute("username", accessmember.getMsisdn());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("memberAdmin");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessMember", method = RequestMethod.POST, params = "access=changePin")
	public ModelAndView changePin(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("upgrademember") UpgradeMember accessmember, ModelMap model) {
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
			model.addAttribute("username", accessmember.getMsisdn());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("changeCredential");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/accessType", method = RequestMethod.GET)
	public ModelAndView accessType(
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

			return new ModelAndView("accessType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/accessTypeData", method = RequestMethod.GET)
	public String groupData(@RequestParam(value = "username", required = false) String username)
			throws MalformedURLException, core.finance.host.services.ws.access.Exception_Exception {
		String jsonData = accessProcessor.loadAccessType();
		return jsonData;
	}

	@RequestMapping(value = "/createAccessType", method = RequestMethod.GET)
	public ModelAndView createAccessType(
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
		model.addAttribute("createaccesstypemodel", new AccessType());
		model.addAttribute("listGroup", listGroup);
		model.addAttribute("mainMenu", mainMenu);
		model.addAttribute("unreadMessage", unread);
		model.addAttribute("messageSummary", messageSummary);
		return new ModelAndView("createAccessType");
	}

	@RequestMapping(value = "/createAccessTypeForm", method = RequestMethod.POST)
	public ModelAndView createAccessTypeForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createaccesstypemodel") AccessType access, BindingResult result, ModelMap model) {
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

			accessProcessor.createAccessType(access);
			return new ModelAndView("redirect:/createAccessTypeResult", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createAccessTypeResult", method = RequestMethod.GET)
	public ModelAndView createAccessTypeResult(
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
			model.addAttribute("title", "Create Access Type");
			model.addAttribute("message", "Access Type has been created successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("accessType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editAccessType", method = RequestMethod.GET)
	public ModelAndView editAccessType(@RequestParam(value = "id", required = true) Integer id,
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

			LoadAccessTypeResponse loadAccessTypeResponse = accessProcessor.loadAccessTypeByID(id);

			model.addAttribute(member);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			model.addAttribute("name", loadAccessTypeResponse.getAccessType().get(0).getName());
			model.addAttribute("internalName", loadAccessTypeResponse.getAccessType().get(0).getInternalName());
			model.addAttribute("description", loadAccessTypeResponse.getAccessType().get(0).getDescription());
			model.addAttribute("accessTypeID", id);
			model.addAttribute("editaccesstypemodel", new AccessType());
			return new ModelAndView("editAccessType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editAccessTypeForm", method = RequestMethod.POST)
	public ModelAndView editAccessTypeForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editaccesstypemodel") AccessType access, BindingResult result, ModelMap model) {
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

			accessProcessor.editAcessType(access);

			return new ModelAndView("redirect:/editAccessTypeResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editAccessTypeResult", method = RequestMethod.GET)
	public ModelAndView editAccessTypeResult(
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
			model.addAttribute("title", "Create Access Type");
			model.addAttribute("message", "Access Type has been updated successfully");
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("accessType");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createMemberCredential", method = RequestMethod.GET)
	public ModelAndView memberCredentialForm(
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
			List<String> accessTypes = accessProcessor.listAccessType();

			model.addAttribute(member);
			model.addAttribute("memberCredentialModel", new MemberCredential());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			model.addAttribute("listAccessType", accessTypes);
			return new ModelAndView("memberCredential", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createMemberCredentialForm", method = RequestMethod.POST)
	public ModelAndView createMemberCredentialForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("memberCredentialModel") MemberCredential memberCredential, BindingResult result,
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

			if (!memberCredential.getCredential().equals(memberCredential.getConfirmCredential())) {
				return new ModelAndView("redirect:/createMemberCredentialResult?status=1", model);
			}

			String[] accessType = memberCredential.getAccessType().split("-");
			Integer accType = Integer.valueOf(accessType[0].trim());
			accessProcessor.createCredential(memberCredential.getUsername(), memberCredential.getConfirmCredential(),
					accType);
			model.addAttribute(member);
			return new ModelAndView("redirect:/createMemberCredentialResult?status=0", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createMemberCredentialResult?status=2&fault=" + ex.getMessage(), model);
		}
	}

	@RequestMapping(value = "/createMemberCredentialResult", method = RequestMethod.GET)
	public ModelAndView createMemberCredentialResult(
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
			List<String> accessTypes = accessProcessor.listAccessType();

			model.addAttribute(member);
			if (status == 0) {
				model.addAttribute("notification", "success");
				model.addAttribute("title", "Create Member Credential");
				model.addAttribute("message", "Member Credential has been created successfully");
			} else if (status == 1) {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Member Credential Failed");
				model.addAttribute("message", "Credential not equals to Confirm Credential");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Create Member Credential Failed");
				model.addAttribute("message", fault);
			}
			model.addAttribute(member);
			model.addAttribute("memberCredentialModel", new MemberCredential());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			model.addAttribute("listAccessType", accessTypes);
			return new ModelAndView("memberCredential");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/currency", method = RequestMethod.GET)
	public ModelAndView createAccountPermission(
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
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("currency");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/currencyData", method = RequestMethod.GET)
	public String currencyData() throws MalformedURLException, TransactionException_Exception {
		String jsonData = accountProcessor.loadCurrency();
		return jsonData;
	}

	@RequestMapping(value = "/createCurrency", method = RequestMethod.GET)
	public ModelAndView createCurrency(
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
			model.addAttribute("createcurrency", new Currency());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("createCurrency");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/createCurrencyForm", method = RequestMethod.POST)
	public ModelAndView createCurrencyForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("createcurrency") Currency currency, BindingResult result, ModelMap model) {
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

			accountProcessor.createCurrency(currency);

			model.addAttribute(member);
			return new ModelAndView("redirect:/createCurrencyResult?status=0");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/createCurrencyResult?status=2&fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/createCurrencyResult", method = RequestMethod.GET)
	public ModelAndView createCurrencyResult(
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
				model.addAttribute("title", "Create Currency");
				model.addAttribute("message", "Currency has been created successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Currency Creation Failed");
				model.addAttribute("message", fault);
			}
			return new ModelAndView("currency");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editCurrency", method = RequestMethod.GET)
	public ModelAndView editCurrency(@RequestParam(value = "id") Integer id,
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

			CurrencyResponse response = accountProcessor.loadCurrencyByID(id);

			model.addAttribute(member);
			model.addAttribute("editcurrency", new Currency());
			model.addAttribute("id", id);
			model.addAttribute("name", response.getCurrency().get(0).getName());
			model.addAttribute("code", response.getCurrency().get(0).getCode());
			model.addAttribute("prefix", response.getCurrency().get(0).getPrefix());
			model.addAttribute("trailer", response.getCurrency().get(0).getTrailer());
			model.addAttribute("format", response.getCurrency().get(0).getFormat());
			model.addAttribute("grouping", response.getCurrency().get(0).getGrouping());
			model.addAttribute("decimal", response.getCurrency().get(0).getDecimal());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("editCurrency");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/editCurrencyForm", method = RequestMethod.POST)
	public ModelAndView editCurrencyForm(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("editcurrency") Currency currency, BindingResult result, ModelMap model) {
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

			accountProcessor.editCurrency(currency);

			model.addAttribute(member);
			return new ModelAndView("redirect:/editCurrencyResult?status=0");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/editCurrencyResult?status=2&fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/editCurrencyResult", method = RequestMethod.GET)
	public ModelAndView editCurrencyResult(
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
				model.addAttribute("title", "Update Currency");
				model.addAttribute("message", "Currency has been updated successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Currency Update Failed");
				model.addAttribute("message", fault);
			}
			return new ModelAndView("currency");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
}