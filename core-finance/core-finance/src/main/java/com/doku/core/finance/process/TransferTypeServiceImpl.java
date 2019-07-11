package com.doku.core.finance.process;

import java.util.LinkedList;
import java.util.List;
import javax.xml.ws.Holder;

import com.doku.core.finance.data.Accounts;
import com.doku.core.finance.data.Brokers;
import com.doku.core.finance.data.FeeByMember;
import com.doku.core.finance.data.Fees;
import com.doku.core.finance.data.Members;
import com.doku.core.finance.data.Status;
import com.doku.core.finance.data.StatusBuilder;
import com.doku.core.finance.data.TransactionException;
import com.doku.core.finance.data.TransferNotifications;
import com.doku.core.finance.data.TransferTypePermission;
import com.doku.core.finance.data.TransferTypes;
import com.doku.core.finance.services.LoadBrokerRequest;
import com.doku.core.finance.services.LoadBrokerResponse;
import com.doku.core.finance.services.LoadFeesByIDRequest;
import com.doku.core.finance.services.LoadFeesByIDResponse;
import com.doku.core.finance.services.BrokerRequest;
import com.doku.core.finance.services.FeeByMemberRequest;
import com.doku.core.finance.services.FeeByMemberResponse;
import com.doku.core.finance.services.FeeRequest;
import com.doku.core.finance.services.Header;
import com.doku.core.finance.services.LoadFeesByTransferTypeRequest;
import com.doku.core.finance.services.LoadFeesByTransferTypeResponse;
import com.doku.core.finance.services.LoadTransferTypesByAccountIDRequest;
import com.doku.core.finance.services.LoadTransferTypesByAccountIDResponse;
import com.doku.core.finance.services.LoadTransferTypesByIDRequest;
import com.doku.core.finance.services.LoadTransferTypesByIDResponse;
import com.doku.core.finance.services.LoadTransferTypesByUsernameRequest;
import com.doku.core.finance.services.LoadTransferTypesByUsernameResponse;
import com.doku.core.finance.services.LoadTransferTypesRequest;
import com.doku.core.finance.services.LoadTransferTypesResponse;
import com.doku.core.finance.services.TransferType;
import com.doku.core.finance.services.TransferTypeNotificationRequest;
import com.doku.core.finance.services.TransferTypeNotificationResponse;
import com.doku.core.finance.services.TransferTypePermissionRequest;
import com.doku.core.finance.services.TransferTypePermissionResponse;
import com.doku.core.finance.services.TransferTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferTypeServiceImpl implements TransferType {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private AccountValidation accountValidation;

	@Override
	public LoadTransferTypesResponse loadTransferTypes(Holder<Header> headerParam, LoadTransferTypesRequest req) {
		LoadTransferTypesResponse loadTransferTypesResponse = new LoadTransferTypesResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<TransferTypes> lTransferType = baseRepository.getTransferTypesRepository()
					.loadAllTransferType(req.getCurrentPage(), req.getPageSize());
			Integer total = baseRepository.getTransferTypesRepository().countTotalTransferTypes();
			loadTransferTypesResponse.setTotalRecords(total);
			loadTransferTypesResponse.setTransferType(lTransferType);
			loadTransferTypesResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadTransferTypesResponse;
		} catch (Exception e) {
			loadTransferTypesResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadTransferTypesResponse;
		}
	}

	@Override
	public LoadTransferTypesByIDResponse loadTransferTypesByID(Holder<Header> headerParam,
			LoadTransferTypesByIDRequest req) {
		LoadTransferTypesByIDResponse tid = new LoadTransferTypesByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			TransferTypes t = transferTypeValidation.validateTransferType(req.getId());
			tid.setTransferTypes(t);
			tid.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return tid;
		} catch (Exception e) {
			tid.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tid;
		}
	}

	@Override
	public LoadTransferTypesByAccountIDResponse loadTransferTypesByAccountID(Holder<Header> headerParam,
			LoadTransferTypesByAccountIDRequest req) {
		LoadTransferTypesByAccountIDResponse taid = new LoadTransferTypesByAccountIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<TransferTypes> lt = baseRepository.getTransferTypesRepository()
					.listTransferTypeByAccountID(req.getAccountID());
			taid.setTransferTypes(lt);
			taid.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return taid;
		} catch (Exception e) {
			taid.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return taid;
		}
	}

	@Override
	public LoadTransferTypesByUsernameResponse loadTransferTypesByUsername(Holder<Header> headerParam,
			LoadTransferTypesByUsernameRequest req) {
		LoadTransferTypesByUsernameResponse tur = new LoadTransferTypesByUsernameResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			List<TransferTypes> ltv = baseRepository.getTransferTypesRepository()
					.listTransferTypeByGroupID(member.getGroupID());
			tur.setTransferTypes(ltv);
			tur.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			tur.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tur;
		}
		return tur;
	}

	@Override
	public void createTransferTypes(Holder<Header> headerParam, TransferTypeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Accounts fromAcc = accountValidation.validateAccount(req.getFromAccountID());
			Accounts toAcc = accountValidation.validateAccount(req.getToAccountID());
			baseRepository.getTransferTypesRepository().createTransferType(fromAcc.getId(), toAcc.getId(),
					req.getName(), req.getDescription(), req.getMinAmount(), req.getMaxAmount(), req.getMaxCount(),
					req.getOtpThreshold());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateTransferTypes(Holder<Header> headerParam, TransferTypeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getId());
			Accounts fromAcc = accountValidation.validateAccount(req.getFromAccountID());
			Accounts toAcc = accountValidation.validateAccount(req.getToAccountID());
			baseRepository.getTransferTypesRepository().updateTransferType(req.getId(), fromAcc.getId(), toAcc.getId(),
					req.getName(), req.getDescription(), req.getMinAmount(), req.getMaxAmount(), req.getMaxCount(),
					req.getOtpThreshold());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void createTransferTypePermissions(Holder<Header> headerParam, TransferTypePermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			List<TransferTypePermission> lt = baseRepository.getTransferTypesRepository()
					.loadTransferTypePermissionsByGroup(req.getGroupID(), req.getTransferTypeID());
			if (lt.size() > 0) {
				throw new TransactionException("PERMISSION_ALREADY_GRANTED");
			}
			baseRepository.getTransferTypesRepository().createTransferTypePermission(req.getTransferTypeID(),
					req.getGroupID());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void deleteTransferTypePermissions(Holder<Header> headerParam, TransferTypePermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getId() != null) {
				baseRepository.getTransferTypesRepository().deleteTransferTypePermission(req.getId());
			} else {
				transferTypeValidation.validateTransferType(req.getTransferTypeID());
				baseRepository.getTransferTypesRepository().deleteTransferTypePermission(req.getTransferTypeID(),
						req.getGroupID());
			}
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public LoadFeesByIDResponse loadFeesByID(Holder<Header> headerParam, LoadFeesByIDRequest req) {
		LoadFeesByIDResponse lfbid = new LoadFeesByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Fees fee = baseRepository.getTransferTypesRepository().getFeeFromID(req.getId());

			if (fee.getFromAccountID() == 0) {
				fee.setFromAccountName("SOURCE_ACCOUNT");
			} else if (fee.getFromAccountID() == -1) {
				fee.setFromAccountName("DESTINATION_ACCOUNT");
			} else if (fee.getFromAccountID() == -2) {
				fee.setFromAccountName("SOURCE_PARENT_ACCOUNT");
			} else if (fee.getFromAccountID() == -3) {
				fee.setFromAccountName("DESTINATION_PARENT_ACCOUNT");
			} else {
				Accounts account = baseRepository.getAccountsRepository().loadAccountsByID(fee.getFromAccountID());
				fee.setFromAccountName(account.getName());
			}

			if (fee.getToAccountID() == 0) {
				fee.setToAccountName("SOURCE_ACCOUNT");
			} else if (fee.getToAccountID() == -1) {
				fee.setToAccountName("DESTINATION_ACCOUNT");
			} else if (fee.getToAccountID() == -2) {
				fee.setToAccountName("SOURCE_PARENT_ACCOUNT");
			} else if (fee.getToAccountID() == -3) {
				fee.setToAccountName("DESTINATION_PARENT_ACCOUNT");
			} else {
				Accounts account = baseRepository.getAccountsRepository().loadAccountsByID(fee.getToAccountID());
				fee.setToAccountName(account.getName());
			}

			if (fee.getFromMemberID() == 0) {
				fee.setFromMemberName("SOURCE_MEMBER");
			} else if (fee.getFromMemberID() == -1) {
				fee.setFromMemberName("DESTINATION_MEMBER");
			} else if (fee.getFromMemberID() == -2) {
				fee.setFromMemberName("SOURCE_PARENT_MEMBER");
			} else if (fee.getFromMemberID() == -3) {
				fee.setFromMemberName("DESTINATION_PARENT_MEMBER");
			} else {
				Members member = baseRepository.getMembersRepository().findOneMembers("id", fee.getFromMemberID());
				fee.setFromMemberName(member.getName());
			}

			if (fee.getToMemberID() == 0) {
				fee.setToMemberName("SOURCE_MEMBER");
			} else if (fee.getToMemberID() == -1) {
				fee.setToMemberName("DESTINATION_MEMBER");
			} else if (fee.getToMemberID() == -2) {
				fee.setToMemberName("SOURCE_PARENT_MEMBER");
			} else if (fee.getToMemberID() == -3) {
				fee.setToMemberName("DESTINATION_PARENT_MEMBER");
			} else {
				Members member = baseRepository.getMembersRepository().findOneMembers("id", fee.getToMemberID());
				fee.setToMemberName(member.getName());
			}

			lfbid.setFee(fee);
			lfbid.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			lfbid.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lfbid;
		}
		return lfbid;
	}

	@Override
	public LoadFeesByTransferTypeResponse loadFeesByTransferType(Holder<Header> headerParam,
			LoadFeesByTransferTypeRequest req) {
		LoadFeesByTransferTypeResponse lfbt = new LoadFeesByTransferTypeResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getId());
			List<Fees> fees = null;
			if (req.isShowAllStatus()) {
				fees = baseRepository.getTransferTypesRepository().getAllFeeFromTransferTypeID(req.getId());
			} else {
				fees = baseRepository.getTransferTypesRepository().getFeeFromTransferTypeID(req.getId());
			}
			lfbt.setFees(fees);
			lfbt.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			lfbt.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lfbt;
		}
		return lfbt;
	}

	@Override
	public void createFees(Holder<Header> headerParam, FeeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypesRepository().createFee(req);
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateFees(Holder<Header> headerParam, FeeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypesRepository().updateFee(req);
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void deleteFees(Holder<Header> headerParam, FeeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypesRepository().deleteFee(req.getTransferTypeID());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public LoadBrokerResponse loadBrokers(Holder<Header> headerParam, LoadBrokerRequest req) {
		LoadBrokerResponse br = new LoadBrokerResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getId() != null) {
				List<Brokers> lb = baseRepository.getTransferTypesRepository().getBrokersByID(req.getId());
				br.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
				br.setBrokers(lb);
			} else if (req.getFeeID() != null && req.getFromMember() != null) {
				Members member = memberValidation.validateMember(req.getFromMember(), true);
				List<Brokers> lb = baseRepository.getTransferTypesRepository().getBrokersFromMember(req.getFeeID(),
						member.getId());
				br.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
				br.setBrokers(lb);
			} else if (req.getFeeID() != null && req.getToMember() != null) {
				Members member = memberValidation.validateMember(req.getToMember(), false);
				List<Brokers> lb = baseRepository.getTransferTypesRepository().getBrokersToMember(req.getFeeID(),
						member.getId());
				br.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
				br.setBrokers(lb);
			} else if (req.getFeeID() != null) {
				List<Brokers> lb = baseRepository.getTransferTypesRepository().getBrokersFromFee(req.getFeeID());
				br.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
				br.setBrokers(lb);
			} else {
				List<Brokers> lb = baseRepository.getTransferTypesRepository().loadAllBrokers();
				br.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
				br.setBrokers(lb);
			}
			return br;
		} catch (TransactionException ex) {
			br.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return br;
		}
	}

	@Override
	public void createBrokers(Holder<Header> headerParam, BrokerRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		Members toMember = memberValidation.validateMember(req.getToMember(), false);
		accountValidation.validateAccount(req.getToAccountID());

		/*
		 * BROKERING MEMBER & ACCOUNT : If value = 0 -> Source memberID/accountID, If
		 * value = -1 -> destination
		 */

		if (req.getFromMember().equalsIgnoreCase("0") || req.getFromMember().equalsIgnoreCase("-1")) {
			baseRepository.getTransferTypesRepository().createBrokers(req, Integer.valueOf(req.getFromMember()),
					toMember.getId());
		} else {
			Members fromMember = memberValidation.validateMember(req.getFromMember(), true);
			accountValidation.validateAccount(req.getFromAccountID());
			baseRepository.getTransferTypesRepository().createBrokers(req, fromMember.getId(), toMember.getId());
		}
	}

	@Override
	public void editBrokers(Holder<Header> headerParam, BrokerRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		Members toMember = memberValidation.validateMember(req.getToMember(), false);
		accountValidation.validateAccount(req.getToAccountID());
		if (req.getFromMember().equalsIgnoreCase("0") || req.getFromMember().equalsIgnoreCase("-1")) {
			baseRepository.getTransferTypesRepository().updateBrokers(req, Integer.valueOf(req.getFromMember()),
					toMember.getId());
		} else {
			Members fromMember = memberValidation.validateMember(req.getFromMember(), true);
			accountValidation.validateAccount(req.getFromAccountID());
			baseRepository.getTransferTypesRepository().updateBrokers(req, fromMember.getId(), toMember.getId());
		}
	}

	@Override
	public void deleteBrokers(Holder<Header> headerParam, BrokerRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getTransferTypesRepository().deleteBrokers(req);
	}

	@Override
	public TransferTypePermissionResponse loadTransferTypePermissions(Holder<Header> headerParam,
			TransferTypePermissionRequest req) throws TransactionException {
		TransferTypePermissionResponse transferTypePermissionResponse = new TransferTypePermissionResponse();

		webserviceValidation.validateWebservice(headerParam.value.getToken());
		List<TransferTypePermission> lGroup = baseRepository.getTransferTypesRepository()
				.loadTransferTypePermissionsByID(req.getTransferTypeID());
		transferTypePermissionResponse.setPermission(lGroup);
		transferTypePermissionResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		return transferTypePermissionResponse;
	}

	@Override
	public TransferTypeNotificationResponse loadTransferTypeNotification(Holder<Header> headerParam,
			TransferTypeNotificationRequest req) {
		TransferTypeNotificationResponse transferTypeNotificationResponse = new TransferTypeNotificationResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getId() != null) {
				List<TransferNotifications> listTransferNotifications = new LinkedList<TransferNotifications>();
				TransferNotifications transferNotifications = baseRepository.getTransferTypesRepository()
						.loadTransferNotificationByID(req.getId());
				listTransferNotifications.add(transferNotifications);
				transferTypeNotificationResponse.setNotification(listTransferNotifications);
			} else {
				List<TransferNotifications> listTransferNotifications = baseRepository.getTransferTypesRepository()
						.loadTransferNotificationByTransferType(req.getTransferTypeID());
				transferTypeNotificationResponse.setNotification(listTransferNotifications);
			}

			transferTypeNotificationResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));

		} catch (TransactionException e) {
			e.printStackTrace();
			transferTypeNotificationResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
		}
		return transferTypeNotificationResponse;
	}

	@Override
	public void createTransferTypeNotification(Holder<Header> headerParam, TransferTypeNotificationRequest req)
			throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getTransferTypesRepository().createTransferNotification(req);
	}

	@Override
	public void updateTransferTypeNotification(Holder<Header> headerParam, TransferTypeNotificationRequest req)
			throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getTransferTypesRepository().updateTransferNotification(req);
	}

	@Override
	public void deleteTransferTypeNotification(Holder<Header> headerParam, TransferTypeNotificationRequest req)
			throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getTransferTypesRepository().deleteTransferNotification(req.getId());
	}

	@Override
	public void createFeeByMember(Holder<Header> headerParam, FeeByMemberRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		Members member = memberValidation.validateMember(req.getUsername(), !req.isDestination());
		baseRepository.getTransferTypesRepository().createFeeByMember(req.getFeeID(), member.getId(),
				req.isDestination());
	}

	@Override
	public FeeByMemberResponse loadFeeByMember(Holder<Header> headerParam, FeeByMemberRequest req)
			throws TransactionException {
		FeeByMemberResponse response = new FeeByMemberResponse();
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		List<FeeByMember> feeMember = null;
		if (req.getId() != null) {
			feeMember = baseRepository.getTransferTypesRepository().loadFeeByMemberByID(req.getId());
		} else {
			feeMember = baseRepository.getTransferTypesRepository().loadFeeByMember(req.getFeeID());
		}
		response.setFeeByMember(feeMember);
		response.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		return response;
	}

	@Override
	public void deleteFeeByMember(Holder<Header> headerParam, FeeByMemberRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getTransferTypesRepository().deleteFeeByMember(req.getId());
	}
}
