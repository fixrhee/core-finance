package com.doku.core.finance.process;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.ws.Holder;

import com.doku.core.finance.data.AccountPermissions;
import com.doku.core.finance.data.Accounts;
import com.doku.core.finance.data.ClosedAccountBalance;
import com.doku.core.finance.data.Currencies;
import com.doku.core.finance.data.MemberView;
import com.doku.core.finance.data.Members;
import com.doku.core.finance.data.PaymentFields;
import com.doku.core.finance.data.Status;
import com.doku.core.finance.data.StatusBuilder;
import com.doku.core.finance.data.TransactionException;
import com.doku.core.finance.data.TransferHistory;
import com.doku.core.finance.data.TransferTypeFields;
import com.doku.core.finance.data.Transfers;
import com.doku.core.finance.data.WebServices;
import com.doku.core.finance.services.Header;
import com.doku.core.finance.services.LoadAccountPermissionsRequest;
import com.doku.core.finance.services.LoadAccountPermissionsResponse;
import com.doku.core.finance.services.Account;
import com.doku.core.finance.services.AccountsPermissionRequest;
import com.doku.core.finance.services.BalanceInquiryRequest;
import com.doku.core.finance.services.BalanceInquiryResponse;
import com.doku.core.finance.services.CurrencyRequest;
import com.doku.core.finance.services.CurrencyResponse;
import com.doku.core.finance.services.AccountsRequest;
import com.doku.core.finance.services.LoadAccountsByGroupsRequest;
import com.doku.core.finance.services.LoadAccountsByGroupsResponse;
import com.doku.core.finance.services.LoadAccountsByIDRequest;
import com.doku.core.finance.services.LoadAccountsByIDResponse;
import com.doku.core.finance.services.LoadAccountsRequest;
import com.doku.core.finance.services.LoadAccountsResponse;
import com.doku.core.finance.services.TransactionHistoryRequest;
import com.doku.core.finance.services.TransactionHistoryResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceImpl implements Account {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private CredentialValidation credentialValidation;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private AccountValidation accountValidation;
	@Autowired
	private CurrencyValidation currenciesValidation;
	private Logger logger = LoggerFactory.getLogger(AccessServiceImpl.class);

	@Override
	public LoadAccountsByIDResponse loadAccountsByID(Holder<Header> headerParam, LoadAccountsByIDRequest req) {
		LoadAccountsByIDResponse accountResponse = new LoadAccountsByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getGroupID() != null) {
				Accounts accounts = accountValidation.validateAccount(req.getId(), req.getGroupID());
				if (accounts == null) {
					accountResponse.setStatus(StatusBuilder.getStatus(Status.ACCOUNT_NOT_FOUND));
					return accountResponse;
				}
				Currencies currency = baseRepository.getCurrenciesRepository()
						.loadCurrencyByID(accounts.getCurrency().getId());
				accounts.setFormattedCreditLimit(Utils.formatAmount(accounts.getCreditLimit(), currency.getGrouping(),
						currency.getDecimal(), currency.getFormat(), currency.getPrefix(), currency.getTrailer()));
				accounts.setFormattedLowerCreditLimit(Utils.formatAmount(accounts.getLowerCreditLimit(),
						currency.getGrouping(), currency.getDecimal(), currency.getFormat(), currency.getPrefix(),
						currency.getTrailer()));
				accounts.setFormattedUpperCreditLimit(Utils.formatAmount(accounts.getUpperCreditLimit(),
						currency.getGrouping(), currency.getDecimal(), currency.getFormat(), currency.getPrefix(),
						currency.getTrailer()));
				accountResponse.setAccount(accounts);
			} else {
				Accounts accounts = accountValidation.validateAccount(req.getId());
				if (accounts == null) {
					accountResponse.setStatus(StatusBuilder.getStatus(Status.ACCOUNT_NOT_FOUND));
					return accountResponse;
				}
				accountResponse.setAccount(accounts);
			}

			accountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return accountResponse;
		} catch (TransactionException e) {
			accountResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return accountResponse;
		}
	}

	@Override
	public LoadAccountsByGroupsResponse loadAccountsByGroups(Holder<Header> headerParam,
			LoadAccountsByGroupsRequest req) {
		LoadAccountsByGroupsResponse accountResponse = new LoadAccountsByGroupsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Accounts> accounts = accountValidation.validateAccountByGroup(req);
			accountResponse.setAccounts(accounts);
			accountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return accountResponse;
		} catch (TransactionException e) {
			accountResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return accountResponse;
		}
	}

	@Override
	public BalanceInquiryResponse loadBalanceInquiry(Holder<Header> headerParam, BalanceInquiryRequest req) {
		BalanceInquiryResponse balanceResponse = new BalanceInquiryResponse();

		/*
		 * Validate Webservice
		 */
		try {
			WebServices ws = webserviceValidation.validateWebservice(headerParam.value.getToken());

			/*
			 * Validate FromMember
			 */
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);

			/*
			 * Validate FromMember Credential
			 */
			credentialValidation.validateCredential(ws, req.getAccessTypeID(), req.getCredential(), fromMember);

			/*
			 * Validate AccountID
			 */
			Accounts account = accountValidation.validateAccount(req.getAccountID(), fromMember.getGroupID());
			Currencies currency = baseRepository.getCurrenciesRepository()
					.loadCurrencyByID(account.getCurrency().getId());
			account.setCurrency(currency);

			BigDecimal balance = BigDecimal.ZERO;

			ClosedAccountBalance caBalance = baseRepository.getAccountsRepository()
					.loadClosedAccountBalance(fromMember.getId(), account.getId());
			if (caBalance != null) {
				BigDecimal currentBalance = baseRepository.getAccountsRepository().loadBalanceInquiry(
						fromMember.getUsername(), account.getId(), caBalance.getLastTransferID());
				balance = caBalance.getBalance().add(currentBalance);
				logger.info("[Member = " + fromMember.getUsername() + ", Closed Account Balance = " + caBalance.getBalance()
						+ ", Current Balance = " + currentBalance + ", Final Balance = " + balance + "]");
			} else {
				balance = accountValidation.loadBalanceInquiry(req.getUsername(), req.getAccountID());
			}

			BigDecimal reservedAmount = accountValidation.loadReservedAmount(req.getUsername(), req.getAccountID());

			balanceResponse.setBalance(balance);
			balanceResponse.setFormattedBalance(Utils.formatAmount(balance, currency.getGrouping(),
					currency.getDecimal(), currency.getFormat(), currency.getPrefix(), currency.getTrailer()));
			balanceResponse.setReservedAmount(reservedAmount);
			balanceResponse.setFormattedReservedAmount(Utils.formatAmount(reservedAmount, currency.getGrouping(),
					currency.getDecimal(), currency.getFormat(), currency.getPrefix(), currency.getTrailer()));
			balanceResponse.setAccount(account);
			balanceResponse.setMember(fromMember);
			balanceResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return balanceResponse;
		} catch (TransactionException ce) {
			balanceResponse.setStatus(StatusBuilder.getStatus(ce.getMessage()));
			return balanceResponse;
		}

	}

	@Override
	public TransactionHistoryResponse loadTransactionHistory(Holder<Header> headerParam,
			TransactionHistoryRequest req) {
		TransactionHistoryResponse history = new TransactionHistoryResponse();
		try {
			WebServices ws = webserviceValidation.validateWebservice(headerParam.value.getToken());

			/*
			 * Validate FromMember
			 */
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);

			/*
			 * Validate FromMember Credential
			 */
			credentialValidation.validateCredential(ws, req.getAccessTypeID(), req.getCredential(), fromMember);

			String fromDate = req.getFromDate() != null ? req.getFromDate() : Utils.GetDate("yyyy-MM-dd");
			String toDate = req.getFromDate() != null ? req.getToDate() : Utils.GetDate("yyyy-MM-dd");
			String orderBy = req.getOrderBy() != null ? req.getOrderBy() : "id";
			String descendingOrder = req.getDescendingOrder() == null || req.getDescendingOrder() == true ? "desc"
					: "asc";
			List<Transfers> trf = baseRepository.getAccountsRepository().loadTransferFromUsername(req.getUsername(),
					req.getAccountID(), req.getCurrentPage(), req.getPageSize(), fromDate, toDate, orderBy,
					descendingOrder);

			if (trf.size() == 0) {
				history.setStatus(StatusBuilder.getStatus(Status.NO_TRANSACTION));
				return history;
			}

			Currencies currency = baseRepository.getCurrenciesRepository().loadCurrencyByAccountID(req.getAccountID());

			Integer totalDisplayRecords = baseRepository.getAccountsRepository()
					.countTotalTransaction(fromMember.getId(), req.getAccountID(), fromDate, toDate);
			Integer totalRecords = baseRepository.getAccountsRepository().countTotalRecords();

			List<Integer> transferIDs = new LinkedList<Integer>();
			for (Transfers t : trf) {
				transferIDs.add(t.getId());
			}

			List<PaymentFields> pfield = baseRepository.getTransfersRepository()
					.loadMultiPaymentFieldValuesByTransferID(transferIDs);

			Map<Integer, List<PaymentFields>> result = pfield.stream()
					.collect(Collectors.groupingBy(PaymentFields::getTransferID, Collectors.toList()));

			List<TransferHistory> lth = new LinkedList<TransferHistory>();
			for (int i = 0; i < trf.size(); i++) {
				TransferHistory trfHistory = new TransferHistory();
				if (trf.get(i).isCustomField()) {
					List<PaymentFields> paymentFields = result.get(trf.get(i).getId());
					trfHistory.setCustomFields(paymentFields);
				}
				MemberView fromMemberTrf = new MemberView();
				fromMemberTrf.setId(trf.get(i).getFromMemberID());
				fromMemberTrf.setName(trf.get(i).getFromName());
				fromMemberTrf.setUsername(trf.get(i).getFromUsername());

				MemberView toMemberTrf = new MemberView();
				toMemberTrf.setId(trf.get(i).getToMemberID());
				toMemberTrf.setName(trf.get(i).getToName());
				toMemberTrf.setUsername(trf.get(i).getToUsername());

				// if (trf.get(i).getParentID() == null) {
				TransferTypeFields typeField = new TransferTypeFields();
				typeField.setFromAccounts(trf.get(i).getFromAccountID());
				typeField.setToAccounts(trf.get(i).getToAccountID());
				typeField.setName(trf.get(i).getName());
				trfHistory.setTransferType(typeField);
				// }

				trfHistory.setAmount(trf.get(i).getAmount());
				trfHistory.setFormattedAmount(Utils.formatAmount(trf.get(i).getAmount(), currency.getGrouping(),
						currency.getDecimal(), currency.getFormat(), currency.getPrefix(), currency.getTrailer()));
				trfHistory.setChargedBack(trf.get(i).isChargedBack());
				trfHistory.setDescription(trf.get(i).getDescription());
				trfHistory.setFromMember(fromMemberTrf);
				trfHistory.setToMember(toMemberTrf);
				trfHistory.setId(trf.get(i).getId());
				trfHistory.setParentID(trf.get(i).getParentID());
				trfHistory.setTraceNumber(trf.get(i).getTraceNumber().substring(1));
				trfHistory.setTransactionDate(trf.get(i).getTransactionDate());
				trfHistory.setTransactionNumber(trf.get(i).getTransactionNumber());
				lth.add(trfHistory);
			}

			history.setTransfers(lth);
			history.setTotalRecords(totalRecords);
			history.setDisplayRecords(totalDisplayRecords);
			history.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return history;
		} catch (TransactionException ce) {
			history.setStatus(StatusBuilder.getStatus(ce.getMessage()));
			return history;
		}
	}

	@Override
	public LoadAccountsResponse loadAccounts(Holder<Header> headerParam, LoadAccountsRequest req) {
		LoadAccountsResponse accountResponse = new LoadAccountsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				req.setCurrentPage(0);
				req.setPageSize(0);
			}
			List<Accounts> accounts = baseRepository.getAccountsRepository().loadAccounts(req.getCurrentPage(),
					req.getPageSize());

			Integer totalRecords = accountValidation.countTotalAccounts();
			accountResponse.setAccount(accounts);
			accountResponse.setTotalRecords(totalRecords);
			accountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return accountResponse;
		} catch (TransactionException e) {
			accountResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return accountResponse;
		}
	}

	@Override
	public void createAccount(Holder<Header> headerParam, AccountsRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Currencies currency = currenciesValidation.validateCurrencyByID(req.getCurrencyID());
			if (currency == null) {
				throw new TransactionException(String.valueOf(Status.INVALID_CURRENCY));
			}
			baseRepository.getAccountsRepository().createAccount(req.getName(), req.getDescription(),
					req.isSystemAccount(), req.getCurrencyID());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateAccount(Holder<Header> headerParam, AccountsRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			accountValidation.validateAccount(req.getId());
			baseRepository.getAccountsRepository().updateAccount(req.getId(), req.getCurrencyID(), req.getName(),
					req.getDescription(), req.isSystemAccount());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void createAccountPermission(Holder<Header> headerParam, AccountsPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			accountValidation.validateAccount(req.getAccountID());
			baseRepository.getAccountsRepository().addAccountPermission(req.getAccountID(), req.getGroupID(),
					req.getCreditLimit(), req.getUpperCreditLimit(), req.getLowerCreditLimit());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateAccountPermission(Holder<Header> headerParam, AccountsPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getAccountsRepository().updateAccountPermission(req.getId(), req.getCreditLimit(),
					req.getUpperCreditLimit(), req.getLowerCreditLimit());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void deleteAccountPermission(Holder<Header> headerParam, AccountsPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getId() != null) {
				baseRepository.getAccountsRepository().deleteAccountPermission(req.getId());
			} else {
				accountValidation.validateAccount(req.getAccountID());
				baseRepository.getAccountsRepository().deleteAccountPermission(req.getAccountID(), req.getGroupID());
			}
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public CurrencyResponse loadCurrency(Holder<Header> headerParam, CurrencyRequest req) throws TransactionException {
		CurrencyResponse cr = new CurrencyResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getId() == null) {
				List<Currencies> lc = baseRepository.getCurrenciesRepository().loadAllCurrencies();
				cr.setCurrency(lc);
				if (lc.size() == 0) {
					cr.setStatus(StatusBuilder.getStatus(Status.INVALID_CURRENCY));
				}
			} else {
				List<Currencies> listCurrencies = new LinkedList<Currencies>();
				Currencies c = baseRepository.getCurrenciesRepository().loadCurrencyByID(req.getId());
				listCurrencies.add(c);
				cr.setCurrency(listCurrencies);
			}
			cr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return cr;
		} catch (TransactionException ex) {
			cr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return cr;
		}
	}

	@Override
	public void createCurrency(Holder<Header> headerParam, CurrencyRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		currenciesValidation.createCurrency(req);
	}

	@Override
	public void updateCurrency(Holder<Header> headerParam, CurrencyRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		currenciesValidation.updateCurrency(req);
	}

	@Override
	public LoadAccountPermissionsResponse loadAccountPermissionsByAccount(Holder<Header> headerParam,
			LoadAccountPermissionsRequest req) {
		LoadAccountPermissionsResponse loadAccountPermissionsResponse = new LoadAccountPermissionsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<AccountPermissions> accounts = accountValidation.validateAccountPermission(req.getAccountID());
			if (accounts == null) {
				loadAccountPermissionsResponse.setStatus(StatusBuilder.getStatus(Status.ACCOUNT_NOT_FOUND));
				return loadAccountPermissionsResponse;
			}
			loadAccountPermissionsResponse.setAccountPermission(accounts);
			loadAccountPermissionsResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadAccountPermissionsResponse;
		} catch (TransactionException e) {
			loadAccountPermissionsResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadAccountPermissionsResponse;
		}
	}

	@Override
	public LoadAccountPermissionsResponse loadAccountPermissionsByID(Holder<Header> headerParam,
			LoadAccountPermissionsRequest req) {
		LoadAccountPermissionsResponse loadAccountPermissionsResponse = new LoadAccountPermissionsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<AccountPermissions> accounts = accountValidation.validateAccountPermissionByID(req.getId());
			if (accounts == null) {
				loadAccountPermissionsResponse.setStatus(StatusBuilder.getStatus(Status.ACCOUNT_NOT_FOUND));
				return loadAccountPermissionsResponse;
			}
			loadAccountPermissionsResponse.setAccountPermission(accounts);
			loadAccountPermissionsResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadAccountPermissionsResponse;
		} catch (TransactionException e) {
			loadAccountPermissionsResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadAccountPermissionsResponse;
		}
	}

}
