package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import core.finance.host.services.ws.message.Exception_Exception;
import core.finance.host.services.ws.message.LoadMessageByIDRequest;
import core.finance.host.services.ws.message.LoadMessageByUsernameRequest;
import core.finance.host.services.ws.message.LoadMessageResponse;
import core.finance.host.services.ws.message.Message;
import core.finance.host.services.ws.message.MessageRequest;
import core.finance.host.services.ws.message.MessageServiceImplService;
import core.finance.host.services.ws.message.NotificationMessage;
import core.finance.host.services.ws.message.SendMessageRequest;
import core.finance.host.services.ws.message.UnreadMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {

	@Autowired
	private ContextLoader contextLoader;

	public Integer getCountUnreadMessages(String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "message?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MessageServiceImplService");
		MessageServiceImplService service = new MessageServiceImplService(url, qName);
		Message message = service.getMessageServiceImplPort();
		LoadMessageByUsernameRequest lmr = new LoadMessageByUsernameRequest();
		lmr.setUsername(username);

		core.finance.host.services.ws.message.Header headerMessage = new core.finance.host.services.ws.message.Header();
		headerMessage.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.message.Header> headerMessageAuth = new Holder<core.finance.host.services.ws.message.Header>();
		headerMessageAuth.value = headerMessage;

		UnreadMessage unread = message.countUnreadMessage(headerMessageAuth, lmr);
		return unread.getUnread();
	}

	public List<String> loadUnreadMessages(String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "message?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MessageServiceImplService");
		MessageServiceImplService service = new MessageServiceImplService(url, qName);
		Message message = service.getMessageServiceImplPort();
		LoadMessageByUsernameRequest lmr = new LoadMessageByUsernameRequest();
		lmr.setUsername(username);
		lmr.setCurrentPage(0);
		lmr.setPageSize(5);

		core.finance.host.services.ws.message.Header headerMessage = new core.finance.host.services.ws.message.Header();
		headerMessage.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.message.Header> headerMessageAuth = new Holder<core.finance.host.services.ws.message.Header>();
		headerMessageAuth.value = headerMessage;

		LoadMessageResponse loadMessageResponse = message.loadMessageByUsername(headerMessageAuth, lmr);
		List<NotificationMessage> notifList = loadMessageResponse.getMessage();

		List<String> messageList = new LinkedList<String>();
		for (int i = 0; i < notifList.size(); i++) {
			String composeMessage = "<a href=\"message?MessageID=" + notifList.get(i).getId()
					+ "\"><span class=\"image\"><img src=\"images/user.png\" alt=\"Profile Image\" /></span>"
					+ "<span><span><b>" + notifList.get(i).getFromName() + "</b></span>" + "<span class=\"time\">"
					+ Utils.toRelative(notifList.get(i).getDate().toGregorianCalendar().getTime(), new Date(), 1)
					+ "</span>" + "</span>" + "<span class=\"message\">" + notifList.get(i).getSubject()
					+ "</span></a>";
			messageList.add(composeMessage);
		}
		return messageList;
	}

	public List<String> loadMessages(String username, String session) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "message?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MessageServiceImplService");
		MessageServiceImplService service = new MessageServiceImplService(url, qName);
		Message message = service.getMessageServiceImplPort();
		LoadMessageByUsernameRequest lmr = new LoadMessageByUsernameRequest();
		lmr.setUsername(username);
		lmr.setCurrentPage(0);
		lmr.setPageSize(10);

		core.finance.host.services.ws.message.Header headerMessage = new core.finance.host.services.ws.message.Header();
		headerMessage.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.message.Header> headerMessageAuth = new Holder<core.finance.host.services.ws.message.Header>();
		headerMessageAuth.value = headerMessage;

		LoadMessageResponse loadMessageResponse = message.loadMessageByUsername(headerMessageAuth, lmr);
		List<NotificationMessage> notifList = loadMessageResponse.getMessage();

		List<String> messageList = new LinkedList<String>();
		for (int i = 0; i < notifList.size(); i++) {
			String unreadDots = notifList.get(i).isRead() == false ? "<i class=\"fa fa-circle\"></i>" : "<i></i>";
			String composeMessage = "<a href=\"message?MessageID=" + notifList.get(i).getId() + "\">"
					+ "<div class=\"mail_list\">" + " <div class=\"left\">" + unreadDots
					+ "<i class=\"fa fa-edit\"></i>" + "</div><div class=\"right\">" + "<h3>"
					+ notifList.get(i).getFromName() + "<small>"
					+ Utils.toRelative(notifList.get(i).getDate().toGregorianCalendar().getTime(), new Date(), 1)
					+ "</small></h3>" + "<p>" + notifList.get(i).getSubject() + "</p>" + "</div></div></a>";
			messageList.add(composeMessage);
		}

		return messageList;
	}

	public String loadMessageByID(Integer id, String session) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "message?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MessageServiceImplService");
		MessageServiceImplService service = new MessageServiceImplService(url, qName);
		Message message = service.getMessageServiceImplPort();
		LoadMessageByIDRequest lmid = new LoadMessageByIDRequest();
		lmid.setId(id);
		core.finance.host.services.ws.message.Header headerMessage = new core.finance.host.services.ws.message.Header();
		headerMessage.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.message.Header> headerMessageAuth = new Holder<core.finance.host.services.ws.message.Header>();
		headerMessageAuth.value = headerMessage;

		LoadMessageResponse loadMessageResponse = message.loadMessageByID(headerMessageAuth, lmid);
		List<NotificationMessage> notifList = loadMessageResponse.getMessage();

		if (notifList.get(0).isRead() == false) {
			try {
				MessageRequest messageReq = new MessageRequest();
				messageReq.setId(id);
				message.flagMessageReadByID(headerMessageAuth, messageReq);
			} catch (Exception_Exception e) {
				e.printStackTrace();
			}
		}

		String composeMessage = "<div class=\"col-sm-9 mail_view\"><div class=\"inbox-body\"><div class=\"mail_heading row\">"
				+ "<div class=\"col-md-8\"><div class=\"btn-group\"><button class=\"btn btn-sm btn-default\" type=\"button\" data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Print\"><i class=\"fa fa-print\"></i></button><button class=\"btn btn-sm btn-default\" type=\"button\" data-placement=\"top\" data-toggle=\"tooltip\" "
				+ "data-original-title=\"Trash\" onclick=\"location.href = 'message?MessageID="
				+ notifList.get(0).getId() + "&Action=delete';\"><i class=\"fa fa-trash-o\"></i></button></div></div>"

				+ "<div class=\"col-md-4 text-right\">" + "<p class=\"date\">"
				+ notifList.get(0).getDate().toGregorianCalendar().getTime() + "</p>" + "</div>"
				+ "<div class=\"col-md-12\">" + "<h4>" + notifList.get(0).getSubject() + "</h4>" + "</div></div>"

				+ "<div class=\"sender-info\">" + "<div class=\"row\">" + "<div class=\"col-md-12\">" + "<strong>"
				+ notifList.get(0).getFromName() + "</strong>" + " to " + "<strong>me </strong>"
				+ "<a class=\"sender-dropdown\"><i class=\"fa fa-chevron-down\"></i></a></div></div></div>"

				+ "<div class=\"view-mail\">" + "<p>" + notifList.get(0).getBody() + "</p></div><br/>"
				+ "<div class=\"btn-group\"><button class=\"btn btn-sm btn-default\" type=\"button\" data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Print\"><i class=\"fa fa-print\"></i></button><button class=\"btn btn-sm btn-default\" type=\"button\" data-placement=\"top\" data-toggle=\"tooltip\" data-original-title=\"Trash\" onclick=\"location.href = 'message?MessageID="
				+ notifList.get(0).getId()
				+ "&Action=delete';\"><i class=\"fa fa-trash-o\"></i></button></div></div></div>";

		return composeMessage;
	}

	public void deleteMessage(Integer id) throws MalformedURLException {
		try {
			URL url = new URL(contextLoader.getHostWSUrl() + "message?wsdl");
			QName qName = new QName(contextLoader.getHostWSPort(), "MessageServiceImplService");
			MessageServiceImplService service = new MessageServiceImplService(url, qName);
			Message message = service.getMessageServiceImplPort();
			LoadMessageByIDRequest lmid = new LoadMessageByIDRequest();
			lmid.setId(id);
			core.finance.host.services.ws.message.Header headerMessage = new core.finance.host.services.ws.message.Header();
			headerMessage.setToken(contextLoader.getHeaderToken());
			Holder<core.finance.host.services.ws.message.Header> headerMessageAuth = new Holder<core.finance.host.services.ws.message.Header>();
			headerMessageAuth.value = headerMessage;
			MessageRequest messageReq = new MessageRequest();
			messageReq.setId(id);
			message.deleteMessage(headerMessageAuth, messageReq);
		} catch (Exception_Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String fromMember, String toMember, String subject, String body)
			throws MalformedURLException, Exception_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "message?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MessageServiceImplService");
		MessageServiceImplService service = new MessageServiceImplService(url, qName);
		Message client = service.getMessageServiceImplPort();

		core.finance.host.services.ws.message.Header headerMessage = new core.finance.host.services.ws.message.Header();
		headerMessage.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.message.Header> headerMessageAuth = new Holder<core.finance.host.services.ws.message.Header>();
		headerMessageAuth.value = headerMessage;

		SendMessageRequest sendMsgReq = new SendMessageRequest();
		sendMsgReq.setFromUsername(fromMember);
		sendMsgReq.setToUsername(toMember);
		sendMsgReq.setSubject(subject);
		sendMsgReq.setBody(body);

		client.sendMessage(headerMessageAuth, sendMsgReq);
	}
}
