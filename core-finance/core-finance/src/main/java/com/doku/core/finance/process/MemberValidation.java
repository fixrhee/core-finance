package com.doku.core.finance.process;

import java.util.List;
import com.doku.core.finance.data.ExternalMemberFields;
import com.doku.core.finance.data.MemberCustomFields;
import com.doku.core.finance.data.MemberFields;
import com.doku.core.finance.data.Members;
import com.doku.core.finance.data.Status;
import com.doku.core.finance.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberValidation {

	@Autowired
	private BaseRepository baseRepository;

	public Members validateMember(String username, boolean source) throws TransactionException {
		Members member = baseRepository.getMembersRepository().findOneMembers("username", username);
		if (member == null) {
			if (source) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			} else {
				throw new TransactionException(String.valueOf(Status.DESTINATION_MEMBER_NOT_FOUND));
			}
		}
		List<ExternalMemberFields> externalMembers = baseRepository.getMembersRepository()
				.loadExternalMemberFields(member.getId());
		member.setExternalMembers(externalMembers);
		return member;
	}

	public Members validateMemberID(Integer memberID, boolean source) throws TransactionException {
		Members member = baseRepository.getMembersRepository().findOneMembers("id", memberID);
		if (member == null) {
			if (source) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			} else {
				throw new TransactionException(String.valueOf(Status.DESTINATION_MEMBER_NOT_FOUND));
			}
		}
		List<ExternalMemberFields> externalMembers = baseRepository.getMembersRepository()
				.loadExternalMemberFields(member.getId());
		member.setExternalMembers(externalMembers);
		return member;
	}

	public List<ExternalMemberFields> loadExternalMemberFields(Integer memberID) {
		return baseRepository.getMembersRepository().loadExternalMemberFields(memberID);
	}

	public Boolean isKYC(Integer memberID) {
		return baseRepository.getMembersRepository().memberKYCStatus(memberID);
	}

	public List<MemberFields> loadMemberCustomFieldValuesByMemberID(Integer memberID) {
		return baseRepository.getMembersRepository().loadFieldValuesByMemberID(memberID);
	}

	public List<MemberFields> loadMemberCustomFieldValuesByMemberID(List<Integer> memberID) {
		return baseRepository.getMembersRepository().loadFieldValuesByMemberID(memberID);
	}

	public List<MemberCustomFields> loadCustomFieldsByGroup(Integer groupID) {
		return baseRepository.getMembersRepository().loadFieldsByGroupID(groupID);
	}

}
