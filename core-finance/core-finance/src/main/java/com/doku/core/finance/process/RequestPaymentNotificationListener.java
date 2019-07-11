package com.doku.core.finance.process;

import com.doku.core.finance.data.PaymentDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;

@Component
public class RequestPaymentNotificationListener implements EntryListener<String, PaymentDetails> {

	@Autowired
	private BaseRepository baseRepository;
	private Logger logger = LoggerFactory.getLogger(RequestPaymentNotificationListener.class);

	@Override
	public void entryAdded(EntryEvent<String, PaymentDetails> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entryUpdated(EntryEvent<String, PaymentDetails> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entryRemoved(EntryEvent<String, PaymentDetails> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entryEvicted(EntryEvent<String, PaymentDetails> event) {
		logger.info("[Expired Request Payment Key : " + event.getKey() + ", TrxNo : "
				+ event.getOldValue().getTransactionNumber() + "]");
		if (event.getOldValue().getRequest().getRequestEviction().isDeleteRequestOnEviction()) {
			logger.info("[DELETE Request With Payment Key : " + event.getKey() + ", TrxNo : "
					+ event.getOldValue().getTransactionNumber() + "]");
			baseRepository.getTransfersRepository().deletePendingTransfers(event.getOldValue().getTransactionNumber());
		}
	}

	@Override
	public void mapCleared(MapEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mapEvicted(MapEvent arg0) {
		// TODO Auto-generated method stub

	}

}
