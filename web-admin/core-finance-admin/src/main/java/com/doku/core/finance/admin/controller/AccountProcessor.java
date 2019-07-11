package com.doku.core.finance.admin.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import core.finance.host.services.ws.accounts.Account;
import core.finance.host.services.ws.accounts.AccountServiceImplService;
import core.finance.host.services.ws.accounts.AccountsPermissionRequest;
import core.finance.host.services.ws.accounts.AccountsRequest;
import core.finance.host.services.ws.accounts.BalanceInquiryRequest;
import core.finance.host.services.ws.accounts.BalanceInquiryResponse;
import core.finance.host.services.ws.accounts.CurrencyRequest;
import core.finance.host.services.ws.accounts.CurrencyResponse;
import core.finance.host.services.ws.accounts.LoadAccountPermissionsRequest;
import core.finance.host.services.ws.accounts.LoadAccountPermissionsResponse;
import core.finance.host.services.ws.accounts.LoadAccountsByGroupsRequest;
import core.finance.host.services.ws.accounts.LoadAccountsByGroupsResponse;
import core.finance.host.services.ws.accounts.LoadAccountsByIDRequest;
import core.finance.host.services.ws.accounts.LoadAccountsByIDResponse;
import core.finance.host.services.ws.accounts.LoadAccountsRequest;
import core.finance.host.services.ws.accounts.LoadAccountsResponse;
import core.finance.host.services.ws.accounts.TransactionException_Exception;
import core.finance.host.services.ws.accounts.TransactionHistoryRequest;
import core.finance.host.services.ws.accounts.TransactionHistoryResponse;
import core.finance.host.services.ws.groups.Group;
import core.finance.host.services.ws.groups.GroupServiceImplService;
import core.finance.host.services.ws.groups.LoadGroupsRequest;
import core.finance.host.services.ws.groups.LoadGroupsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.doku.core.finance.admin.model.AccountPermission;
import com.doku.core.finance.admin.model.Currency;

@Component
public class AccountProcessor {
	@Autowired
	private ContextLoader contextLoader;
	@Autowired
	private HazelcastInstance instance;

	public List<String> getAccountFromGroupID(Integer groupID, String username) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsByGroupsRequest lbg = new LoadAccountsByGroupsRequest();
		lbg.setGroupID(groupID);

		LoadAccountsByGroupsResponse response = client.loadAccountsByGroups(accountHeaderAuth, lbg);
		List<String> accountList = new LinkedList<String>();
		if (response.getStatus().getMessage().equalsIgnoreCase("PROCESSED")) {
			for (int i = 0; i < response.getAccounts().size(); i++) {
				String composeAccount = "<div class=\"col-md-4\"><p><h2>"
						+ response.getAccounts().get(i).getName().trim() + "</h2></p></div>";
				composeAccount += "<div class=\"col-md-6\"><p>" + response.getAccounts().get(i).getDescription().trim()
						+ "</p></div>";
				composeAccount += "<div class=\"col-md-2\"><p><button type=\"submit\" class=\"btn btn-success\" onclick=\"window.location='/admin/transactionHistory?GroupID="
						+ groupID + "&username=" + username + "&AccountID=" + response.getAccounts().get(i).getId()
						+ "'\">View</button></p></div>";
				accountList.add(composeAccount);
			}
			return accountList;
		} else {
			return null;
		}
	}

	public String getTransactionHistory(String username, Integer accountID, String fromDate, String toDate,
			Integer currentPage, Integer pageSize) throws MalformedURLException {
		IMap<String, List<String>> trxHistoryMap = instance.getMap("TransactionHistory");
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderTokenNonCredential());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		TransactionHistoryRequest trxReq = new TransactionHistoryRequest();
		trxReq.setUsername(username);
		trxReq.setAccountID(accountID);
		trxReq.setCurrentPage(currentPage);
		trxReq.setPageSize(pageSize);
		trxReq.setFromDate(fromDate);
		trxReq.setToDate(toDate);

		TransactionHistoryResponse response = client.loadTransactionHistory(accountHeaderAuth, trxReq);

		List<Map<String, Object>> trxList = new LinkedList<Map<String, Object>>();
		for (int i = 0; i < response.getTransfers().size(); i++) {
			Map<String, Object> trxContent = new HashMap<String, Object>();
			trxContent.put("transactionDate", Utils
					.formatDate(response.getTransfers().get(i).getTransactionDate().toGregorianCalendar().getTime()));

			String fromSelf = response.getTransfers().get(i).getFromMember().getUsername().equalsIgnoreCase(username)
					? ""
					: response.getTransfers().get(i).getFromMember().getName();
			String toSelf = response.getTransfers().get(i).getToMember().getUsername().equalsIgnoreCase(username) ? ""
					: response.getTransfers().get(i).getToMember().getName();

			if ((fromSelf.equalsIgnoreCase("")) && (toSelf.equalsIgnoreCase(""))) {
				trxContent.put("toFromMember", response.getTransfers().get(i).getFromMember().getName());
			} else {
				trxContent.put("toFromMember", fromSelf + toSelf);
			}
			trxContent.put("amount", response.getTransfers().get(i).getFormattedAmount());

			// String transferTypeName = response.getTransfers().get(i).getParentID() !=
			// null
			// ? response.getTransfers().get(i).getDescription()
			// : response.getTransfers().get(i).getTransferType().getName();

			trxContent.put("transactionNumber", response.getTransfers().get(i).getTransactionNumber());
			trxContent.put("traceNumber", response.getTransfers().get(i).getTraceNumber());

			if (response.getTransfers().get(i).getParentID() != null) {
				trxContent.put("transferType", response.getTransfers().get(i).getDescription());
				trxContent.put("description", response.getTransfers().get(i).getParentID());
			} else {
				trxContent.put("transferType", response.getTransfers().get(i).getTransferType().getName());
				trxContent.put("description", response.getTransfers().get(i).getDescription());

			}
			String remark = response.getTransfers().get(i).getAmount().compareTo(BigDecimal.ZERO) > 0 ? "C" : "D";
			trxContent.put("remark", remark);
			trxList.add(trxContent);
		}

		List<String> jsonList = convertList(trxList, s -> Utils.toJSON(s));
		trxHistoryMap.put(username, jsonList);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", trxList);
		trxMap.put("recordsTotal", response.getTotalRecords() != null ? response.getTotalRecords() : 0);
		trxMap.put("recordsFiltered", response.getDisplayRecords() != null ? response.getDisplayRecords() : 0);

		String jsontrxHistory = Utils.toJSON(trxMap);
		return jsontrxHistory;

	}

	public String searchTransactionHistory(String username, Integer accountID, String fromDate, String toDate,
			Integer start, Integer length, String searchValue) throws MalformedURLException {
		IMap<String, List<String>> trxHistoryMap = instance.getMap("TransactionHistory");
		List<String> cacheList = trxHistoryMap.get(username);
		Pattern pattern = Pattern.compile(".(:\")*" + searchValue);

		StringBuilder bufferList = new StringBuilder();
		Integer matchList = 0;
		if (cacheList != null) {
			bufferList.append("{\"data\":[");

			List<String> matches = cacheList.stream().filter(pattern.asPredicate()).collect(Collectors.toList());
			if (matches != null) {
				bufferList.append(matches.stream().map(Object::toString).collect(Collectors.joining(",")));
				matchList = matches.size();
			}
			bufferList.append("]");
			bufferList.append(",\"recordsTotal\":\"" + matchList + "\"");
			bufferList.append(",\"recordsFiltered\":\"" + matchList + "\"");
			bufferList.append("}");
		} else {
			this.getTransactionHistory(username, accountID, fromDate, toDate, start, length);
			cacheList = trxHistoryMap.get(username);
			bufferList.append("{\"data\":[");

			List<String> matches = cacheList.stream().filter(pattern.asPredicate()).collect(Collectors.toList());
			if (matches != null) {
				bufferList.append(matches.stream().map(Object::toString).collect(Collectors.joining(",")));
				matchList = matches.size();
			}
			bufferList.append("]");
			bufferList.append(",\"recordsTotal\":\"" + matchList + "\"");
			bufferList.append(",\"recordsFiltered\":\"" + matchList + "\"");
			bufferList.append("}");
		}

		return bufferList.toString();
	}

	public Map<String, Object> getAccountDetails(String username, Integer accountID, Integer groupID)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();
		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderTokenNonCredential());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsByIDRequest loadAccountsByID = new LoadAccountsByIDRequest();
		loadAccountsByID.setId(accountID);
		loadAccountsByID.setGroupID(groupID);

		LoadAccountsByIDResponse loadAccountsByIDResponse = client.loadAccountsByID(accountHeaderAuth,
				loadAccountsByID);

		BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
		balanceInquiryRequest.setUsername(username);
		balanceInquiryRequest.setAccountID(accountID);

		BalanceInquiryResponse balanceInquiryResponse = client.loadBalanceInquiry(accountHeaderAuth,
				balanceInquiryRequest);

		Map<String, Object> accDetailsContentMap = new HashMap<String, Object>();
		accDetailsContentMap.put("accountName", loadAccountsByIDResponse.getAccount().getName());
		accDetailsContentMap.put("creditLimit", loadAccountsByIDResponse.getAccount().getFormattedCreditLimit());
		accDetailsContentMap.put("upperCreditLimit",
				loadAccountsByIDResponse.getAccount().getFormattedUpperCreditLimit());
		accDetailsContentMap.put("balance", balanceInquiryResponse.getFormattedBalance());
		accDetailsContentMap.put("reservedAmount", balanceInquiryResponse.getFormattedReservedAmount());

		return accDetailsContentMap;
	}

	public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
		return from.stream().map(func).collect(Collectors.toList());
	}

	public LoadAccountsByIDResponse loadAccountDetail(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsByIDRequest lbg = new LoadAccountsByIDRequest();
		lbg.setId(id);
		LoadAccountsByIDResponse response = client.loadAccountsByID(accountHeaderAuth, lbg);

		return response;
	}

	public String loadAccountMember(Integer groupID, Integer pageSize, Integer currentPage)
			throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsByGroupsRequest lbg = new LoadAccountsByGroupsRequest();
		lbg.setGroupID(groupID);
		LoadAccountsByGroupsResponse response = client.loadAccountsByGroups(accountHeaderAuth, lbg);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("groupID", groupID);
		trxMap.put("data", response.getAccounts());
		trxMap.put("recordsTotal", response.getAccounts().size());
		trxMap.put("recordsFiltered", response.getAccounts().size());
		return Utils.toJSON(trxMap);
	}

	public String loadAllAccount(Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsRequest la = new LoadAccountsRequest();
		la.setCurrentPage(currentPage);
		la.setPageSize(pageSize);
		LoadAccountsResponse response = client.loadAccounts(accountHeaderAuth, la);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", response.getAccount());
		trxMap.put("recordsTotal", response.getTotalRecords());
		trxMap.put("recordsFiltered", response.getTotalRecords());
		return Utils.toJSON(trxMap);
	}

	public List<String> listAllAccount(Integer currentPage, Integer pageSize) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountsRequest la = new LoadAccountsRequest();
		la.setCurrentPage(currentPage);
		la.setPageSize(pageSize);
		LoadAccountsResponse response = client.loadAccounts(accountHeaderAuth, la);

		List<String> accountList = new LinkedList<String>();
		if (response.getAccount().size() > 0) {
			int i;
			for (i = 0; i < response.getAccount().size(); i++) {
				accountList.add(response.getAccount().get(i).getId() + " - " + response.getAccount().get(i).getName());
			}
		}

		return accountList;
	}

	public String loadAccountPermission(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountPermissionsRequest loadAccountPermissionsRequest = new LoadAccountPermissionsRequest();
		loadAccountPermissionsRequest.setAccountID(id);
		LoadAccountPermissionsResponse loadAccountsResponse = client.loadAccountPermissionsByAccount(accountHeaderAuth,
				loadAccountPermissionsRequest);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", loadAccountsResponse.getAccountPermission());
		trxMap.put("recordsTotal", loadAccountsResponse.getAccountPermission().size());
		trxMap.put("recordsFiltered", loadAccountsResponse.getAccountPermission().size());
		return Utils.toJSON(trxMap);
	}

	public LoadAccountPermissionsResponse loadAccountPermissionByID(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		LoadAccountPermissionsRequest loadAccountPermissionsRequest = new LoadAccountPermissionsRequest();
		loadAccountPermissionsRequest.setId(id);
		LoadAccountPermissionsResponse loadAccountsResponse = client.loadAccountPermissionsByID(accountHeaderAuth,
				loadAccountPermissionsRequest);
		return loadAccountsResponse;

	}

	public void createAccountPermission(AccountPermission req)
			throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		AccountsPermissionRequest accountPermissionRequest = new AccountsPermissionRequest();
		String[] groupID = req.getGroup().split("-");
		Integer id = Integer.valueOf(groupID[0].trim());

		accountPermissionRequest.setGroupID(id);
		accountPermissionRequest.setAccountID(req.getAccountID());
		accountPermissionRequest.setCreditLimit(req.getCreditLimit());
		accountPermissionRequest.setUpperCreditLimit(req.getUpperCreditLimit());
		accountPermissionRequest.setLowerCreditLimit(req.getLowerCreditLimit());

		client.createAccountPermission(accountHeaderAuth, accountPermissionRequest);
	}

	public void editAccountPermission(AccountPermission req)
			throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		AccountsPermissionRequest accountPermissionRequest = new AccountsPermissionRequest();

		accountPermissionRequest.setId(req.getId());
		accountPermissionRequest.setCreditLimit(req.getCreditLimit());
		accountPermissionRequest.setUpperCreditLimit(req.getUpperCreditLimit());
		accountPermissionRequest.setLowerCreditLimit(req.getLowerCreditLimit());

		client.updateAccountPermission(accountHeaderAuth, accountPermissionRequest);
	}

	public void deleteAccountPermission(Integer id) throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		AccountsPermissionRequest accountsPermissionRequest = new AccountsPermissionRequest();
		accountsPermissionRequest.setId(id);
		client.deleteAccountPermission(accountHeaderAuth, accountsPermissionRequest);

	}

	public String loadCurrency() throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		CurrencyRequest currencyRequest = new CurrencyRequest();
		CurrencyResponse currencyResponse = client.loadCurrency(accountHeaderAuth, currencyRequest);

		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", currencyResponse.getCurrency());
		trxMap.put("recordsTotal", currencyResponse.getCurrency().size());
		trxMap.put("recordsFiltered", currencyResponse.getCurrency().size());
		return Utils.toJSON(trxMap);
	}

	public void createCurrency(Currency req) throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		CurrencyRequest currencyRequest = new CurrencyRequest();
		currencyRequest.setCode(req.getCode());
		currencyRequest.setDecimalSeparator(req.getDecimal());
		currencyRequest.setFormat(req.getFormat());
		currencyRequest.setGroupingSeparator(req.getGrouping());
		currencyRequest.setName(req.getName());
		currencyRequest.setPrefix(req.getPrefix());
		currencyRequest.setTrailer(req.getTrailer());

		client.createCurrency(accountHeaderAuth, currencyRequest);
	}

	public CurrencyResponse loadCurrencyByID(Integer id) throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		CurrencyRequest currencyRequest = new CurrencyRequest();
		currencyRequest.setId(id);

		return client.loadCurrency(accountHeaderAuth, currencyRequest);
	}

	public void editCurrency(Currency req) throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		CurrencyRequest currencyRequest = new CurrencyRequest();
		currencyRequest.setId(req.getId());
		currencyRequest.setCode(req.getCode());
		currencyRequest.setDecimalSeparator(req.getDecimal());
		currencyRequest.setFormat(req.getFormat());
		currencyRequest.setGroupingSeparator(req.getGrouping());
		currencyRequest.setName(req.getName());
		currencyRequest.setPrefix(req.getPrefix());
		currencyRequest.setTrailer(req.getTrailer());

		client.updateCurrency(accountHeaderAuth, currencyRequest);
	}

	public List<String> getListCurrency() throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		CurrencyRequest currencyRequest = new CurrencyRequest();
		CurrencyResponse currencyResponse = client.loadCurrency(accountHeaderAuth, currencyRequest);

		List<String> currencyList = new LinkedList<String>();
		if (currencyResponse.getCurrency().size() > 0) {
			int i;
			for (i = 0; i < currencyResponse.getCurrency().size(); i++) {
				currencyList.add(currencyResponse.getCurrency().get(i).getId() + " - "
						+ currencyResponse.getCurrency().get(i).getName());
			}
		}

		return currencyList;
	}

	public void createAccount(com.doku.core.finance.admin.model.Account req)
			throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		Integer currencyID = Integer.valueOf(req.getCurrencyName().split("-")[0].trim());
		AccountsRequest accountsRequest = new AccountsRequest();
		accountsRequest.setCurrencyID(currencyID);
		accountsRequest.setDescription(req.getDescription());
		accountsRequest.setName(req.getName());
		accountsRequest.setSystemAccount(req.isSystemAccount());

		client.createAccount(accountHeaderAuth, accountsRequest);
	}

	public void editAccount(com.doku.core.finance.admin.model.Account req)
			throws MalformedURLException, TransactionException_Exception {
		URL url = new URL(contextLoader.getHostWSUrl() + "accounts?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "AccountServiceImplService");
		AccountServiceImplService as = new AccountServiceImplService(url, qName);
		Account client = as.getAccountServiceImplPort();

		core.finance.host.services.ws.accounts.Header headerAccount = new core.finance.host.services.ws.accounts.Header();
		headerAccount.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.accounts.Header> accountHeaderAuth = new Holder<core.finance.host.services.ws.accounts.Header>();
		accountHeaderAuth.value = headerAccount;

		Integer currencyID = Integer.valueOf(req.getCurrencyName().split("-")[0].trim());
		AccountsRequest accountsRequest = new AccountsRequest();
		accountsRequest.setId(req.getAccountID());
		accountsRequest.setCurrencyID(currencyID);
		accountsRequest.setDescription(req.getDescription());
		accountsRequest.setName(req.getName());
		accountsRequest.setSystemAccount(req.isSystemAccount());

		client.updateAccount(accountHeaderAuth, accountsRequest);
	}

}
