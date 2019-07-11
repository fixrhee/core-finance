package com.doku.core.finance.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Transfer {

	@WebMethod(action = "doPayment")
	public PaymentResponse doPayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PaymentRequest req);

	@WebMethod(action = "doInquiry")
	public InquiryResponse doInquiry(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam InquiryRequest req);

	@WebMethod(action = "requestPaymentConfirmation")
	public RequestPaymentConfirmationResponse requestPaymentConfirmation(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam PaymentRequest req);

	@WebMethod(action = "confirmPayment")
	public PaymentResponse confirmPayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ConfirmPaymentRequest req);

	@WebMethod(action = "loadPendingTransaction")
	public PendingPaymentResponse loadPendingTransaction(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PendingPaymentRequest req);

	@WebMethod(action = "generatePaymentTicket")
	public GeneratePaymentTicketResponse generatePaymentTicket(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam GeneratePaymentTicketRequest req);

	@WebMethod(action = "validatePaymentTicket")
	public ValidatePaymentTicketResponse validatePaymentTicket(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ValidatePaymentTicketRequest req);

	@WebMethod(action = "confirmPaymentTicket")
	public PaymentResponse confirmPaymentTicket(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ConfirmPaymentTicketRequest req);

	@WebMethod(action = "transactionStatus")
	public TransactionStatusResponse transactionStatus(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TransactionStatusRequest req);

	@WebMethod(action = "reversePayment")
	public ReversalResponse reversePayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ReversalRequest req);

	@WebMethod(action = "createPaymentCustomFields")
	public void createPaymentCustomFields(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CreatePaymentCustomFieldsRequest req) throws Exception;

	@WebMethod(action = "requestPaymentInquiry")
	public InquiryResponse requestPaymentInquiry(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PaymentInquiryRequest req);
}
