package com.doku.core.finance.process;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.ws.Holder;
import com.doku.core.finance.data.MemberCustomFields;
import com.doku.core.finance.data.ExternalMemberFields;
import com.doku.core.finance.data.Groups;
import com.doku.core.finance.data.MemberFields;
import com.doku.core.finance.data.MemberKYC;
import com.doku.core.finance.data.Members;
import com.doku.core.finance.data.Status;
import com.doku.core.finance.data.StatusBuilder;
import com.doku.core.finance.data.TransactionException;
import com.doku.core.finance.services.ConfirmKYCRequest;
import com.doku.core.finance.services.ConfirmKYCResponse;
import com.doku.core.finance.services.Header;
import com.doku.core.finance.services.LoadKYCRequest;
import com.doku.core.finance.services.LoadKYCResponse;
import com.doku.core.finance.services.LoadMembersByExternalIDRequest;
import com.doku.core.finance.services.LoadMembersByGroupIDRequest;
import com.doku.core.finance.services.LoadMembersByIDRequest;
import com.doku.core.finance.services.LoadMembersByUsernameRequest;
import com.doku.core.finance.services.LoadMembersRequest;
import com.doku.core.finance.services.LoadMembersResponse;
import com.doku.core.finance.services.Member;
import com.doku.core.finance.services.MemberKYCRequest;
import com.doku.core.finance.services.RegisterMemberRequest;
import com.doku.core.finance.services.SubscribeMemberRequest;
import com.doku.core.finance.services.UpdateMemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements Member {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;

	@Override
	public LoadMembersResponse loadMembersByID(Holder<Header> headerParam, LoadMembersByIDRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		List<Members> memberList = new LinkedList<Members>();

		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = memberValidation.validateMemberID(req.getId(), true);
			if (members == null) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}

			List<ExternalMemberFields> extID = memberValidation.loadExternalMemberFields(members.getId());
			List<MemberFields> memberfields = memberValidation.loadMemberCustomFieldValuesByMemberID(members.getId());
			Boolean kycStatus = memberValidation.isKYC(members.getId());

			members.setKycStatus(kycStatus);
			memberList.add(members);
			members.setCustomFields(memberfields);
			members.setExternalMembers(extID);
			loadMembers.setMembers(memberList);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public LoadMembersResponse loadMembersByGroupID(Holder<Header> headerParam, LoadMembersByGroupIDRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Members> members = baseRepository.getMembersRepository().loadMembersByGroupID(req.getGroupID(),
					req.getCurrentPage(), req.getPageSize());
			if (members.size() == 0) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}

			List<Integer> memberIDs = new LinkedList<Integer>();
			for (Members m : members) {
				memberIDs.add(m.getId());
			}

			List<MemberFields> mfield = memberValidation.loadMemberCustomFieldValuesByMemberID(memberIDs);
			Map<Integer, List<MemberFields>> result = mfield.stream()
					.collect(Collectors.groupingBy(MemberFields::getMemberCustomFieldID, Collectors.toList()));

			for (int i = 0; i < members.size(); i++) {
				members.get(i).setCustomFields(result.get(i));
			}

			Integer totalRecords = baseRepository.getMembersRepository().countTotalMembers();

			loadMembers.setMembers(members);
			loadMembers.setTotalRecords(totalRecords);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}

	}

	@Override
	public void registerMembers(Holder<Header> headerParam, RegisterMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Groups groups = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
			if (groups == null) {
				throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
			}

			if (req.getExternalMemberFields() != null) {
				Members parent = null;
				if (req.getExternalMemberFields().getParentID() != null) {
					parent = baseRepository.getMembersRepository().findOneMembers("id",
							req.getExternalMemberFields().getParentID());
					if (parent == null) {
						throw new TransactionException(String.valueOf(Status.PARENT_ID_NOT_FOUND));
					}
					req.getExternalMemberFields().setUsername(parent.getUsername());
				} else if (req.getExternalMemberFields().getUsername() != null) {
					parent = baseRepository.getMembersRepository().findOneMembers("username",
							req.getExternalMemberFields().getUsername());
					if (parent == null) {
						throw new TransactionException(String.valueOf(Status.PARENT_ID_NOT_FOUND));
					}
					req.getExternalMemberFields().setParentID(parent.getId());
				} else {
					parent = null;
				}

				if (parent == null) {
					throw new TransactionException(String.valueOf(Status.PARENT_ID_NOT_FOUND));
				}
			}

			if (req.getCustomFields() != null) {
				List<MemberCustomFields> fields = memberValidation.loadCustomFieldsByGroup(req.getGroupID());

				List<Integer> cfRefID = new LinkedList<Integer>();
				for (int i = 0; i < fields.size(); i++) {
					cfRefID.add(fields.get(i).getId());
				}

				List<Integer> cfRegID = new LinkedList<Integer>();
				for (int i = 0; i < req.getCustomFields().size(); i++) {
					cfRegID.add(req.getCustomFields().get(i).getMemberCustomFieldID());
				}

				cfRegID.removeAll(cfRefID);
				if (cfRegID.size() != 0) {
					throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
				}
			}
			baseRepository.getMembersRepository().createMembers(req);
		} catch (DuplicateKeyException ex) {
			throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
		}
	}

	@Override
	public LoadMembersResponse loadMembersByExternalID(Holder<Header> headerParam, LoadMembersByExternalIDRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Members> members = new LinkedList<Members>();

			if (req.getUsername() != null) {
				members = baseRepository.getMembersRepository().findMembersByExternalPartner(req);
			} else if (req.getExternalID() != null) {
				members = baseRepository.getMembersRepository().findMembersByExternalID(req);
			} else {
				if (req.getCurrentPage() == null || req.getPageSize() == null) {
					req.setCurrentPage(0);
					req.setPageSize(0);
				}
				members = baseRepository.getMembersRepository().loadMembersByExternalID(req);
			}

			if (members.size() == 0) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}

			List<Integer> memberIDs = new LinkedList<Integer>();
			for (Members m : members) {
				memberIDs.add(m.getId());
			}

			List<MemberFields> mfield = memberValidation.loadMemberCustomFieldValuesByMemberID(memberIDs);

			Map<Integer, List<MemberFields>> result = mfield.stream()
					.collect(Collectors.groupingBy(MemberFields::getMemberCustomFieldID, Collectors.toList()));

			for (int i = 0; i < members.size(); i++) {
				members.get(i).setCustomFields(result.get(i));
			}

			Integer totalRecords = baseRepository.getMembersRepository().countTotalExternalMembers(req.getPartnerID());
			loadMembers.setMembers(members);
			loadMembers.setTotalRecords(totalRecords);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public LoadMembersResponse loadMembersByUsername(Holder<Header> headerParam, LoadMembersByUsernameRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		List<Members> memberList = new LinkedList<Members>();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = baseRepository.getMembersRepository().findOneMembers("username", req.getUsername());
			if (members == null) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}
			List<ExternalMemberFields> extID = baseRepository.getMembersRepository()
					.loadExternalMemberFields(members.getId());
			List<MemberFields> memberfields = memberValidation.loadMemberCustomFieldValuesByMemberID(members.getId());

			Boolean kycStatus = baseRepository.getMembersRepository().memberKYCStatus(members.getId());
			if (kycStatus == null) {
				members.setKycStatus(false);
			} else {
				members.setKycStatus(kycStatus);
			}

			members.setCustomFields(memberfields);
			members.setExternalMembers(extID);
			memberList.add(members);
			loadMembers.setMembers(memberList);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public void updateMembers(Holder<Header> headerParam, UpdateMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);
			req.setId(fromMember.getId());
			if (req.getGroupID() != null && fromMember.getGroupID() != req.getGroupID()) {
				Groups group = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
				if (group == null) {
					throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
				}
			} else {
				req.setGroupID(fromMember.getGroupID());
			}

			if (req.getEmail() == null) {
				req.setEmail(fromMember.getEmail());
			}

			if (req.getName() == null) {
				req.setName(fromMember.getName());
			}

			if (req.getMsisdn() == null) {
				req.setMsisdn(fromMember.getMsisdn());
			}

			if (req.getIdCardNo() == null) {
				req.setIdCardNo(fromMember.getIdCardNo());
			}

			if (req.getMotherMaidenName() == null) {
				req.setMotherMaidenName(fromMember.getMotherMaidenName());
			}

			if (req.getAddress() == null) {
				req.setAddress(fromMember.getAddress());
			}

			if (req.getPlaceOfBirth() == null) {
				req.setPlaceOfBirth(fromMember.getPlaceOfBirth());
			}

			if (req.getDateOfBirth() == null) {
				req.setDateOfBirth(fromMember.getDateOfBirth());
			}

			baseRepository.getMembersRepository().updateMembers(req);
		} catch (DataIntegrityViolationException e) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}
	}

	@Override
	public void registerExternalMembers(Holder<Header> headerParam, SubscribeMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members parentMember = memberValidation.validateMemberID(req.getExternalMemberFields().getParentID(), true);
			Members fromMember = memberValidation.validateMember(req.getExternalMemberFields().getUsername(), true);
			Integer id = baseRepository.getMembersRepository().validateExternalMember(fromMember.getId(),
					parentMember.getId());
			if (id != null) {
				baseRepository.getMembersRepository().resubscribeMembers(id,
						req.getExternalMemberFields().getExternalID(), req.getExternalMemberFields().getDescription());
			} else {
				baseRepository.getMembersRepository().subscribeMembers(parentMember, fromMember,
						req.getExternalMemberFields().getExternalID(), req.getExternalMemberFields().getDescription());
			}
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void unregisterExternalMembers(Holder<Header> headerParam, SubscribeMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members parentMember = memberValidation.validateMemberID(req.getExternalMemberFields().getParentID(), true);
			Members fromMember = memberValidation.validateMember(req.getExternalMemberFields().getUsername(), true);
			Integer id = baseRepository.getMembersRepository().validateExternalMember(fromMember.getId(),
					parentMember.getId(), req.getExternalMemberFields().getExternalID());
			if (id == null) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			}
			baseRepository.getMembersRepository().unsubscribeMembers(parentMember,
					req.getExternalMemberFields().getExternalID());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}

	}

	@Override
	public void membersKYCRequest(Holder<Header> headerParam, MemberKYCRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);
			Groups group = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
			if (group == null) {
				throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
			}
			Boolean statusKYC = baseRepository.getMembersRepository().memberKYCStatus(fromMember.getId());
			if (statusKYC == true) {
				throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
			}

			Boolean isRequested = baseRepository.getMembersRepository().memberKYCIsRequested(fromMember.getId());
			if (isRequested) {
				throw new TransactionException(String.valueOf(Status.REQUEST_RECEIVED));
			}

			if (statusKYC == null || statusKYC == false) {
				baseRepository.getMembersRepository().memberKYCRequest(fromMember.getId(), req);
			}
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public ConfirmKYCResponse confirmKYCRequest(Holder<Header> headerParam, ConfirmKYCRequest req) {
		ConfirmKYCResponse confirmKYCResponse = new ConfirmKYCResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);

			//Integer id = baseRepository.getMembersRepository().loadKYCMemberByID(req.getId());
			//String email = baseRepository.getMembersRepository().findOneMembers("id", id).getEmail();

			if (req.isAccepted()) {
				if (baseRepository.getMembersRepository().memberKYCApproval(member.getId(), req.getId())) {
					confirmKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));

					// MuleClient client;
					// client = new MuleClient(configurator.getMuleContext());
					// Map<String, Object> header = new HashMap<String, Object>();
					// client.dispatch("KYCNotificationVM", email, header);

				} else {
					confirmKYCResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				}
			} else {
				baseRepository.getMembersRepository().memberKYCRejectApproval(member.getId(), req.getId(),
						req.getDescription());

				// MuleClient client;
				// client = new MuleClient(configurator.getMuleContext());
				// Map<String, Object> header = new HashMap<String, Object>();
				// client.dispatch("KYCNotificationRejectVM", email, header);

				confirmKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			}
			return confirmKYCResponse;
		} catch (TransactionException e) {
			confirmKYCResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return confirmKYCResponse;
		}
	}

	@Override
	public LoadMembersResponse loadAllMembers(Holder<Header> headerParam, LoadMembersRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Members> members = baseRepository.getMembersRepository().findAllMembers(req.getCurrentPage(),
					req.getPageSize());
			Integer totalRecords = baseRepository.getMembersRepository().countTotalMembers();
			loadMembers.setTotalRecords(totalRecords);
			loadMembers.setMembers(members);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public LoadKYCResponse loadKYCRequest(Holder<Header> headerParam, LoadKYCRequest req) {
		LoadKYCResponse loadKYCResponse = new LoadKYCResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<MemberKYC> kyc = new LinkedList<MemberKYC>();

			if (req.getId() == null) {
				kyc = baseRepository.getMembersRepository().loadMemberKYC(req.getCurrentPage(), req.getPageSize());

				if (kyc.size() == 0) {
					loadKYCResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
					return loadKYCResponse;
				}

				List<Integer> fromIDs = new LinkedList<Integer>();
				for (MemberKYC m : kyc) {
					fromIDs.add(m.getFromMember().getId());
				}

				List<Integer> approvedIDs = new LinkedList<Integer>();
				for (MemberKYC m : kyc) {
					approvedIDs.add(m.getApprovedMember().getId());
				}

				List<Integer> groupIDs = new LinkedList<Integer>();
				for (MemberKYC m : kyc) {
					groupIDs.add(m.getGroup().getId());
				}

				List<Members> fromMember = baseRepository.getMembersRepository().loadMembersByIds(fromIDs);
				List<Members> approvedMember = baseRepository.getMembersRepository().loadMembersByIds(approvedIDs);
				List<Groups> groupDestination = baseRepository.getGroupsRepository().loadGroupByIds(groupIDs);

				Map<Integer, List<Members>> fromMemberMap = fromMember.stream()
						.collect(Collectors.groupingBy(Members::getId, Collectors.toList()));
				Map<Integer, List<Members>> approvedMemberMap = approvedMember.stream()
						.collect(Collectors.groupingBy(Members::getId, Collectors.toList()));
				Map<Integer, List<Groups>> groupMap = groupDestination.stream()
						.collect(Collectors.groupingBy(Groups::getId, Collectors.toList()));

				for (int i = 0; i < kyc.size(); i++) {
					kyc.get(i).setFromMember(fromMemberMap.get(kyc.get(i).getFromMember().getId()).get(0));
					if (kyc.get(i).getApprovedMember().getId() != 0) {
						kyc.get(i).setApprovedMember(
								approvedMemberMap.get(kyc.get(i).getApprovedMember().getId()).get(0));
						kyc.get(i).setFormattedApprovalDate(Utils.formatDate(kyc.get(i).getApprovalDate()));
					} else {
						kyc.get(i).setApprovedMember(null);
					}
					kyc.get(i).setGroup(groupMap.get(kyc.get(i).getGroup().getId()).get(0));
				}

				Integer kycCount = baseRepository.getMembersRepository().countTotalKYCMembers();
				loadKYCResponse.setTotalRecords(kycCount);

			} else {
				kyc = baseRepository.getMembersRepository().loadMemberKYCByID(req.getId());

				if (kyc.size() == 0) {
					loadKYCResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
					return loadKYCResponse;
				}

				Members fromMember = baseRepository.getMembersRepository().findOneMembers("id",
						kyc.get(0).getFromMember().getId());
				kyc.get(0).setFromMember(fromMember);

				if (kyc.get(0).getApprovedMember().getId() != 0) {
					Members approvedMember = baseRepository.getMembersRepository().findOneMembers("id",
							kyc.get(0).getApprovedMember().getId());
					kyc.get(0).setApprovedMember(approvedMember);
					kyc.get(0).setFormattedApprovalDate(Utils.formatDate(kyc.get(0).getApprovalDate()));
				} else {
					kyc.get(0).setApprovedMember(null);
				}
				Groups group = baseRepository.getGroupsRepository().loadGroupsByID(kyc.get(0).getGroup().getId());
				kyc.get(0).setGroup(group);
			}

			loadKYCResponse.setMemberKYC(kyc);
			loadKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadKYCResponse;
		} catch (TransactionException e) {
			loadKYCResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadKYCResponse;
		}

	}

}
