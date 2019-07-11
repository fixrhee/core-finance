package com.doku.core.finance.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;
import com.doku.core.finance.data.TransactionException;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Account {

	@WebMethod(action = "loadAccounts")
	public LoadAccountsResponse loadAccounts(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccountsRequest req);

	@WebMethod(action = "loadAccountsByGroups")
	public LoadAccountsByGroupsResponse loadAccountsByGroups(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccountsByGroupsRequest req);

	@WebMethod(action = "loadAccountPermissionsByAccount")
	public LoadAccountPermissionsResponse loadAccountPermissionsByAccount(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccountPermissionsRequest req);

	@WebMethod(action = "loadAccountPermissionsByID")
	public LoadAccountPermissionsResponse loadAccountPermissionsByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccountPermissionsRequest req);

	@WebMethod(action = "loadAccountsByID")
	public LoadAccountsByIDResponse loadAccountsByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccountsByIDRequest req);

	@WebMethod(action = "loadBalanceInquiry")
	public BalanceInquiryResponse loadBalanceInquiry(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BalanceInquiryRequest req);

	@WebMethod(action = "loadTransactionHistory")
	public TransactionHistoryResponse loadTransactionHistory(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TransactionHistoryRequest req);

	@WebMethod(action = "createAccount")
	public void createAccount(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AccountsRequest req) throws TransactionException;

	@WebMethod(action = "updateAccount")
	public void updateAccount(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AccountsRequest req) throws TransactionException;

	@WebMethod(action = "createAccountPermission")
	public void createAccountPermission(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AccountsPermissionRequest req) throws TransactionException;

	@WebMethod(action = "updateAccountPermission")
	public void updateAccountPermission(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AccountsPermissionRequest req) throws TransactionException;

	@WebMethod(action = "deleteAccountPermission")
	public void deleteAccountPermission(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AccountsPermissionRequest req) throws TransactionException;

	@WebMethod(action = "loadCurrency")
	public CurrencyResponse loadCurrency(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CurrencyRequest req) throws TransactionException;

	@WebMethod(action = "createCurrency")
	public void createCurrency(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CurrencyRequest req) throws TransactionException;

	@WebMethod(action = "updateCurrency")
	public void updateCurrency(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CurrencyRequest req) throws TransactionException;

}