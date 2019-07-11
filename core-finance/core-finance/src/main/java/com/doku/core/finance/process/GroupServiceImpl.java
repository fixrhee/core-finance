package com.doku.core.finance.process;

import java.util.List;

import javax.xml.ws.Holder;

import com.doku.core.finance.data.Groups;
import com.doku.core.finance.data.Status;
import com.doku.core.finance.data.StatusBuilder;
import com.doku.core.finance.data.TransactionException;
import com.doku.core.finance.services.CreateGroupsRequest;
import com.doku.core.finance.services.Group;
import com.doku.core.finance.services.Header;
import com.doku.core.finance.services.LoadGroupsByIDRequest;
import com.doku.core.finance.services.LoadGroupsByIDResponse;
import com.doku.core.finance.services.LoadGroupsRequest;
import com.doku.core.finance.services.LoadGroupsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupServiceImpl implements Group {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private GroupValidation groupValidation;

	@Override
	public LoadGroupsResponse loadAllGroups(Holder<Header> headerParam, LoadGroupsRequest req) {
		LoadGroupsResponse groupsResponse = new LoadGroupsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				req.setCurrentPage(0);
				req.setPageSize(0);
			}
			List<Groups> groups = groupValidation.loadAllGroups(req);
			Integer totalRecords = groupValidation.getTotalRecords();
			groupsResponse.setGroupsList(groups);
			groupsResponse.setTotalRecords(totalRecords);
			groupsResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return groupsResponse;
		} catch (TransactionException e) {
			groupsResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return groupsResponse;
		}
	}

	@Override
	public void createGroups(Holder<Header> headerParam, CreateGroupsRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		groupValidation.validateCreateGroup(req);
	}

	@Override
	public LoadGroupsByIDResponse loadGroupsByID(Holder<Header> headerParam, LoadGroupsByIDRequest req) {
		LoadGroupsByIDResponse groupsResponse = new LoadGroupsByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Groups groups = groupValidation.loadGroupsByID(req.getId());
			groupsResponse.setGroups(groups);
			groupsResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return groupsResponse;
		} catch (TransactionException e) {
			groupsResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return groupsResponse;
		}
	}

}
