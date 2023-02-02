package com.dip.unifiedviewer.infrastructure.services;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.NidRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.NidRequestRepository;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_NID)
public class NidService implements DomainPersistenceServicePort {

	private final NidRequestRepository nidRequestRepository;

	public NidService(NidRequestRepository nidRequestRepository) {
		this.nidRequestRepository = nidRequestRepository;
	}

	@Override
	public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
		nidRequestRepository.executeRequest((NidRequestBodyModel) bodyModel, queryJson);
	}
}
