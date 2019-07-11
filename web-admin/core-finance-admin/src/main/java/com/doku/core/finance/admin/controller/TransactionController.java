package com.doku.core.finance.admin.controller;

import java.util.List;

import javax.validation.Valid;

import core.finance.host.services.ws.access.ValidateCredentialResponse;
import core.finance.host.services.ws.transfers.InquiryResponse;
import core.finance.host.services.ws.transfers.PaymentResponse;
import core.finance.host.services.ws.transfertypes.LoadTransferTypesByUsernameResponse;
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
import com.doku.core.finance.admin.model.Ticket;
import com.doku.core.finance.admin.model.TopupMember;
import com.doku.core.finance.admin.model.Transaction;

@Controller
public class TransactionController {
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private MenuProcessor menuProcessor;
	@Autowired
	private MessageProcessor messageProcessor;
	@Autowired
	private TransactionProcessor transactionProcessor;
	@Autowired
	private TransferTypeProcessor transferTypeProcessor;
	@Autowired
	private AccessProcessor accessProcessor;

	@RequestMapping(value = "/transaction", method = RequestMethod.GET)
	public ModelAndView transfer(
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
			List<String> transferType = transferTypeProcessor.loadTransferType(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("transaction", new Transaction());
			model.addAttribute("name", member.getName());
			model.addAttribute("fromMember", member.getUsername());
			model.addAttribute("listTransfer", transferType);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transaction");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/transactionInquiry", method = RequestMethod.POST)
	public ModelAndView transactionInquiry(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("newgroup") Transaction transaction, BindingResult result, ModelMap model) {
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
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());
			InquiryResponse inquiry = transactionProcessor.transactionInquiry(transaction, member.getUsername());

			if (inquiry.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute("transactionTypeName", inquiry.getTransferType().getName());
				model.addAttribute("transferTypeID", inquiry.getTransferType().getId());
				model.addAttribute("fromMember", inquiry.getFromMember().getUsername());
				model.addAttribute("toMember", inquiry.getToMember().getUsername());
				model.addAttribute("fromName", inquiry.getFromMember().getName());
				model.addAttribute("toName", inquiry.getToMember().getName());

				model.addAttribute("trxAmount", inquiry.getFormattedTransactionAmount());
				model.addAttribute("fee", inquiry.getFormattedTotalFees());
				model.addAttribute("totalAmount", inquiry.getFormattedFinalAmount());

				model.addAttribute("amount", inquiry.getFinalAmount());
				model.addAttribute("description", transaction.getDescription());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute(member);
				return new ModelAndView("transactionInquiry");
			} else {
				return new ModelAndView("redirect:/transactionPaymentResult?fault=" + inquiry.getStatus().getMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/transactionPaymentResult?fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/transactionPayment", method = RequestMethod.POST)
	public ModelAndView transactionPayment(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("newgroup") Transaction transaction, BindingResult result, ModelMap model) {
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

			PaymentResponse paymentResponse = transactionProcessor.transactionPayment(member.getUsername(),
					transaction.getAmount(), transaction.getToMember(), transaction.getDescription(),
					transaction.getCredential(), transaction.getTransferTypeID());

			if (paymentResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				return new ModelAndView("redirect:/transactionPaymentResult");
			} else {
				return new ModelAndView(
						"redirect:/transactionPaymentResult?fault=" + paymentResponse.getStatus().getMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/transactionPaymentResult?fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/transactionPaymentResult", method = RequestMethod.GET)
	public ModelAndView transactionPaymentResult(@RequestParam(value = "fault", required = false) String fault,
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
				model.addAttribute("title", "Transaction");
				model.addAttribute("message", "Transaction has been executed successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Transaction Failed");
				model.addAttribute("message", fault);
			}

			List<String> transferType = transferTypeProcessor.loadTransferType(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("transaction", new Transaction());
			model.addAttribute("name", member.getName());
			model.addAttribute("fromMember", member.getUsername());
			model.addAttribute("listTransfer", transferType);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("transaction");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/ticket", method = RequestMethod.GET)
	public ModelAndView ticketConfirmation(
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
			model.addAttribute("ticket", new Transaction());
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("ticket");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}

	@RequestMapping(value = "/ticketInquiry", method = RequestMethod.POST)
	public ModelAndView ticketInquiry(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, ModelMap model) {
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
			AdminMenu adminMenu = menuProcessor.getMenuList(member.getGroupID());
			List<String> mainMenu = adminMenu.getMainMenu();
			Integer unreadMessage = messageProcessor.getCountUnreadMessages(member.getUsername());
			String unread = unreadMessage != 0 ? String.valueOf(unreadMessage) : "";
			List<String> messageSummary = messageProcessor.loadUnreadMessages(member.getUsername());

			InquiryResponse inquiry = transactionProcessor.ticketInquiry(ticket, member.getUsername());

			if (inquiry.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				model.addAttribute("transactionTypeName", inquiry.getTransferType().getName());
				model.addAttribute("transferTypeID", inquiry.getTransferType().getId());
				model.addAttribute("fromMember", inquiry.getFromMember().getUsername());
				model.addAttribute("toMember", inquiry.getToMember().getUsername());
				model.addAttribute("fromName", inquiry.getFromMember().getName());
				model.addAttribute("toName", inquiry.getToMember().getName());

				model.addAttribute("trxAmount", inquiry.getFormattedTransactionAmount());
				model.addAttribute("fee", inquiry.getFormattedTotalFees());
				model.addAttribute("totalAmount", inquiry.getFormattedFinalAmount());

				model.addAttribute("ticketID", ticket.getTicketID());
				model.addAttribute("amount", inquiry.getFinalAmount());
				model.addAttribute("mainMenu", mainMenu);
				model.addAttribute("unreadMessage", unread);
				model.addAttribute("messageSummary", messageSummary);
				model.addAttribute(member);
				return new ModelAndView("ticketInquiry");
			} else {
				return new ModelAndView("redirect:/ticketConfirmationResult?fault=" + inquiry.getStatus().getMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/ticketConfirmationResult?fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/ticketConfirmation", method = RequestMethod.POST)
	public ModelAndView ticketConfirmation(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, ModelMap model) {
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

			ValidateCredentialResponse vcr = accessProcessor.validateCredential(member.getUsername(),
					ticket.getCredential());
			if (!vcr.getStatus().getMessage().equalsIgnoreCase("VALID")) {
				return new ModelAndView("redirect:/ticketConfirmationResult?fault=" + vcr.getStatus().getMessage());
			}

			PaymentResponse paymentResponse = transactionProcessor.ticketPayment(ticket.getTicketID());

			if (paymentResponse.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
				return new ModelAndView("redirect:/ticketConfirmationResult");
			} else {
				return new ModelAndView(
						"redirect:/ticketConfirmationResult?fault=" + paymentResponse.getStatus().getMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("redirect:/ticketConfirmationResult?fault=" + ex.getMessage());
		}
	}

	@RequestMapping(value = "/ticketConfirmationResult", method = RequestMethod.GET)
	public ModelAndView ticketConfirmationResult(@RequestParam(value = "fault", required = false) String fault,
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
				model.addAttribute("title", "Ticket Confirmation");
				model.addAttribute("message", "Ticket has been confirmed successfully");
			} else {
				model.addAttribute("notification", "error");
				model.addAttribute("title", "Ticket Confirmation Failed");
				model.addAttribute("message", fault);
			}

			List<String> transferType = transferTypeProcessor.loadTransferType(member.getUsername());

			model.addAttribute(member);
			model.addAttribute("ticket", new Ticket());
			model.addAttribute("name", member.getName());
			model.addAttribute("fromMember", member.getUsername());
			model.addAttribute("listTransfer", transferType);
			model.addAttribute("mainMenu", mainMenu);
			model.addAttribute("unreadMessage", unread);
			model.addAttribute("messageSummary", messageSummary);
			return new ModelAndView("ticket");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("page_500");
		}
	}
}
