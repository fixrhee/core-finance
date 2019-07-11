package com.doku.core.finance.process;

import com.doku.core.finance.data.Status;
import com.doku.core.finance.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TraceNumberValidation {

	@Autowired
	private BaseRepository baseRepository;

	public boolean validateTraceNumber(Integer wsID, String traceNumber) throws TransactionException {
		if (!baseRepository.getTransfersRepository().validateTransfersByTraceNumber(wsID, traceNumber)) {
			return true;
		}
		throw new TransactionException(String.valueOf(Status.DUPLICATE_TRANSACTION));

	}
}
