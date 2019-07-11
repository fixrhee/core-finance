package com.doku.core.finance.process;

import java.util.List;

import javax.xml.ws.Holder;

import com.doku.core.finance.data.Members;
import com.doku.core.finance.data.NotificationMessage;
import com.doku.core.finance.data.Status;
import com.doku.core.finance.data.StatusBuilder;
import com.doku.core.finance.data.TransactionException;
import com.doku.core.finance.services.Header;
import com.doku.core.finance.services.LoadMessageByIDRequest;
import com.doku.core.finance.services.LoadMessageByUsernameRequest;
import com.doku.core.finance.services.LoadMessageResponse;
import com.doku.core.finance.services.Message;
import com.doku.core.finance.services.MessageRequest;
import com.doku.core.finance.services.SendMessageRequest;
import com.doku.core.finance.services.UnreadMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageServiceImpl implements Message {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private BaseRepository baseRepository;

	@Override
	public UnreadMessage countUnreadMessage(Holder<Header> headerParam, LoadMessageByUsernameRequest req) {
		UnreadMessage unread = new UnreadMessage();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			Integer count = baseRepository.getMessageRepository().countUnreadMessageByUsername(member.getId());
			unread.setUnread(count);
			unread.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return unread;
		} catch (TransactionException ex) {
			unread.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return unread;
		}
	}

	@Override
	public LoadMessageResponse loadMessageByUsername(Holder<Header> headerParam, LoadMessageByUsernameRequest req) {
		LoadMessageResponse loadMessageResponse = new LoadMessageResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			List<NotificationMessage> notifMessage = baseRepository.getMessageRepository()
					.loadMessageByMemberID(member.getId(), req.getCurrentPage(), req.getPageSize());
			loadMessageResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			loadMessageResponse.setMessage(notifMessage);
			return loadMessageResponse;
		} catch (TransactionException ex) {
			loadMessageResponse.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return loadMessageResponse;
		}
	}

	@Override
	public void flagMessageReadByID(Holder<Header> headerParam, MessageRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getMessageRepository().flagReadByID(req.getId());
	}

	@Override
	public void sendMessage(Holder<Header> headerParam, SendMessageRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		Members fromMember = memberValidation.validateMember(req.getFromUsername(), true);
		Members toMember = memberValidation.validateMember(req.getToUsername(), false);
		baseRepository.getMessageRepository().sendMessage(fromMember.getId(), toMember.getId(), req.getSubject(),
				req.getBody());

	}

	@Override
	public void deleteMessage(Holder<Header> headerParam, MessageRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getMessageRepository().deleteMessage(req.getId());

	}

	@Override
	public LoadMessageResponse loadMessageByID(Holder<Header> headerParam, LoadMessageByIDRequest req) {
		LoadMessageResponse loadMessageResponse = new LoadMessageResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<NotificationMessage> notifMessage = baseRepository.getMessageRepository().loadMessageByID(req.getId());
			loadMessageResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			loadMessageResponse.setMessage(notifMessage);
			return loadMessageResponse;
		} catch (TransactionException ex) {
			loadMessageResponse.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return loadMessageResponse;
		}
	}

}
