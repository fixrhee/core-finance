package com.doku.core.finance.admin.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import core.finance.host.services.ws.transfers.ConfirmPaymentRequest;
import core.finance.host.services.ws.transfers.InquiryRequest;
import core.finance.host.services.ws.transfers.InquiryResponse;
import core.finance.host.services.ws.transfers.PaymentInquiryRequest;
import core.finance.host.services.ws.transfers.PaymentRequest;
import core.finance.host.services.ws.transfers.PaymentResponse;
import core.finance.host.services.ws.transfers.RequestPaymentConfirmationResponse;
import core.finance.host.services.ws.transfers.ReversalRequest;
import core.finance.host.services.ws.transfers.ReversalResponse;
import core.finance.host.services.ws.transfers.Transfer;
import core.finance.host.services.ws.transfers.TransferServiceImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doku.core.finance.admin.model.Ticket;
import com.doku.core.finance.admin.model.TopupMember;
import com.doku.core.finance.admin.model.Transaction;

@Component
public class TransactionProcessor {

	@Autowired
	private ContextLoader contextLoader;

	public InquiryResponse ticketInquiry(Ticket ticket, String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "transfers?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "TransferServiceImplService");
		TransferServiceImplService service = new TransferServiceImplService(url, qName);
		Transfer client = service.getTransferServiceImplPort();

		core.finance.host.services.ws.transfers.Header headerPayment = new core.finance.host.services.ws.transfers.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.transfers.Header> paymentHeaderAuth = new Holder<core.finance.host.services.ws.transfers.Header>();
		paymentHeaderAuth.value = headerPayment;

		PaymentInquiryRequest inquiryRequest = new PaymentInquiryRequest();
		inquiryRequest.setRequestID(ticket.getTicketID());

		InquiryResponse inquiryResponse = client.requestPaymentInquiry(paymentHeaderAuth, inquiryRequest);
		return inquiryResponse;
	}

	public PaymentResponse ticketPayment(String ticketID) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "transfers?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "TransferServiceImplService");
		TransferServiceImplService service = new TransferServiceImplService(url, qName);
		Transfer client = service.getTransferServiceImplPort();

		core.finance.host.services.ws.transfers.Header headerPayment = new core.finance.host.services.ws.transfers.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.transfers.Header> paymentHeaderAuth = new Holder<core.finance.host.services.ws.transfers.Header>();
		paymentHeaderAuth.value = headerPayment;

		ConfirmPaymentRequest cpr = new ConfirmPaymentRequest();
		cpr.setRequestID(ticketID);
		PaymentResponse paymentResponse = client.confirmPayment(paymentHeaderAuth, cpr);
		return paymentResponse;
	}

	public InquiryResponse transactionInquiry(Transaction transfer, String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "transfers?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "TransferServiceImplService");
		TransferServiceImplService service = new TransferServiceImplService(url, qName);
		Transfer client = service.getTransferServiceImplPort();

		core.finance.host.services.ws.transfers.Header headerPayment = new core.finance.host.services.ws.transfers.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.transfers.Header> paymentHeaderAuth = new Holder<core.finance.host.services.ws.transfers.Header>();
		paymentHeaderAuth.value = headerPayment;

		String transferType = transfer.getTransactionType().split("-")[0].trim();

		InquiryRequest inquiryRequest = new InquiryRequest();
		inquiryRequest.setFromMember(username);
		inquiryRequest.setToMember(transfer.getToMember());
		inquiryRequest.setAmount(BigDecimal.valueOf(transfer.getAmount()));
		inquiryRequest.setTransferTypeID(Integer.valueOf(transferType));

		InquiryResponse inquiryResponse = client.doInquiry(paymentHeaderAuth, inquiryRequest);

		return inquiryResponse;
	}

	public PaymentResponse transactionPayment(String username, Integer amount, String toMember, String description,
			String credential, Integer transferTypeID) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "transfers?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "TransferServiceImplService");
		TransferServiceImplService service = new TransferServiceImplService(url, qName);
		Transfer client = service.getTransferServiceImplPort();

		core.finance.host.services.ws.transfers.Header headerPayment = new core.finance.host.services.ws.transfers.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.transfers.Header> paymentHeaderAuth = new Holder<core.finance.host.services.ws.transfers.Header>();
		paymentHeaderAuth.value = headerPayment;

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setFromMember(username);
		paymentRequest.setToMember(toMember);
		paymentRequest.setAmount(BigDecimal.valueOf(amount));
		paymentRequest.setTransferTypeID(transferTypeID);
		paymentRequest.setDescription(description);
		paymentRequest.setTraceNumber(Utils.generateTraceNum());
		paymentRequest.setCredential(credential);
		paymentRequest.setAccessTypeID(contextLoader.getMemberTrxCredentialTypeID());

		PaymentResponse paymentResponse = client.doPayment(paymentHeaderAuth, paymentRequest);

		return paymentResponse;
	}

	public RequestPaymentConfirmationResponse topupInquiry(TopupMember topup, String username)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "transfers?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "TransferServiceImplService");
		TransferServiceImplService service = new TransferServiceImplService(url, qName);
		Transfer client = service.getTransferServiceImplPort();

		core.finance.host.services.ws.transfers.Header headerPayment = new core.finance.host.services.ws.transfers.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.transfers.Header> paymentHeaderAuth = new Holder<core.finance.host.services.ws.transfers.Header>();
		paymentHeaderAuth.value = headerPayment;

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(BigDecimal.valueOf(topup.getAmount()));
		paymentRequest.setDescription(topup.getDescription());
		paymentRequest.setFromMember(username);
		paymentRequest.setToMember(topup.getToAccount());
		paymentRequest.setOriginator(username);
		paymentRequest.setTransferTypeID(topup.getTransferTypeID());
		paymentRequest.setTraceNumber(Utils.generateTraceNum());
		paymentRequest.setAccessTypeID(contextLoader.getMemberTrxCredentialTypeID());
		paymentRequest.setCredential(topup.getCredential());

		RequestPaymentConfirmationResponse paymentResponse = client.requestPaymentConfirmation(paymentHeaderAuth,
				paymentRequest);

		return paymentResponse;
	}

	public PaymentResponse topupPayment(String otp, String requestID) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "transfers?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "TransferServiceImplService");
		TransferServiceImplService service = new TransferServiceImplService(url, qName);
		Transfer client = service.getTransferServiceImplPort();

		core.finance.host.services.ws.transfers.Header headerPayment = new core.finance.host.services.ws.transfers.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.transfers.Header> paymentHeaderAuth = new Holder<core.finance.host.services.ws.transfers.Header>();
		paymentHeaderAuth.value = headerPayment;

		ConfirmPaymentRequest paymentRequest = new ConfirmPaymentRequest();
		paymentRequest.setOtp(otp);
		paymentRequest.setRequestID(requestID);

		PaymentResponse paymentResponse = client.confirmPayment(paymentHeaderAuth, paymentRequest);

		return paymentResponse;
	}

	public ReversalResponse reversePayment(String trxNumber) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "transfers?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "TransferServiceImplService");
		TransferServiceImplService service = new TransferServiceImplService(url, qName);
		Transfer client = service.getTransferServiceImplPort();
		core.finance.host.services.ws.transfers.Header headerPayment = new core.finance.host.services.ws.transfers.Header();
		headerPayment.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.transfers.Header> paymentHeaderAuth = new Holder<core.finance.host.services.ws.transfers.Header>();
		paymentHeaderAuth.value = headerPayment;

		ReversalRequest reversalRes = new ReversalRequest();
		reversalRes.setTransactionNumber(trxNumber);

		ReversalResponse reversalRep = client.reversePayment(paymentHeaderAuth, reversalRes);

		return reversalRep;
	}
}
