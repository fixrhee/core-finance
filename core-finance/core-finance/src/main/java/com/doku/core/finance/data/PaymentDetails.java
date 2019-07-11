package com.doku.core.finance.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.doku.core.finance.data.Accounts;
import com.doku.core.finance.data.FeeResult;
import com.doku.core.finance.data.Members;
import com.doku.core.finance.data.Notifications;
import com.doku.core.finance.data.TransferTypes;
import com.doku.core.finance.data.WebServices;
import com.doku.core.finance.services.PaymentRequest;

public class PaymentDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8208040919455727883L;
	private Integer transferID;
	private Members fromMember;
	private Members toMember;
	private Accounts fromAccount;
	private Accounts toAccount;
	private TransferTypes transferType;
	private FeeResult fees;
	private WebServices webService;
	private PaymentRequest request;
	private String transactionNumber;
	private Date transactionDate;
	private String otp;
	private boolean twoFactorAuthentication;
	private List<Notifications> notification;
	private HashMap<String, Object> privateField;

	public Members getFromMember() {
		return fromMember;
	}

	public void setFromMember(Members fromMember) {
		this.fromMember = fromMember;
	}

	public Members getToMember() {
		return toMember;
	}

	public void setToMember(Members toMember) {
		this.toMember = toMember;
	}

	public Accounts getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(Accounts fromAccount) {
		this.fromAccount = fromAccount;
	}

	public Accounts getToAccount() {
		return toAccount;
	}

	public void setToAccount(Accounts toAccount) {
		this.toAccount = toAccount;
	}

	public FeeResult getFees() {
		return fees;
	}

	public void setFees(FeeResult fees) {
		this.fees = fees;
	}

	public WebServices getWebService() {
		return webService;
	}

	public void setWebService(WebServices webService) {
		this.webService = webService;
	}

	public PaymentRequest getRequest() {
		return request;
	}

	public void setRequest(PaymentRequest request) {
		this.request = request;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public TransferTypes getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferTypes transferType) {
		this.transferType = transferType;
	}

	public Integer getTransferID() {
		return transferID;
	}

	public void setTransferID(Integer transferID) {
		this.transferID = transferID;
	}

	public List<Notifications> getNotification() {
		return notification;
	}

	public void setNotification(List<Notifications> notification) {
		this.notification = notification;
	}

	public boolean isTwoFactorAuthentication() {
		return twoFactorAuthentication;
	}

	public void setTwoFactorAuthentication(boolean twoFactorAuthentication) {
		this.twoFactorAuthentication = twoFactorAuthentication;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public HashMap<String, Object> getPrivateField() {
		return privateField;
	}

	public void setPrivateField(HashMap<String, Object> privateField) {
		this.privateField = privateField;
	}

}
