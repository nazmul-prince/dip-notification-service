package com.dip.unifiedviewer.infrastructure.services;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.BirthRegistrationRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.BirthRegistrationRequestRepository;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_BIRTH_REGISTRATION)
public class BirthRegistrationService implements DomainPersistenceServicePort {

	private final BirthRegistrationRequestRepository birthRegistrationRequestRepository;

	public BirthRegistrationService(BirthRegistrationRequestRepository birthRegistrationRequestRepository) {
		this.birthRegistrationRequestRepository = birthRegistrationRequestRepository;
	}

	@Override
	public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
		birthRegistrationRequestRepository.executeRequest((BirthRegistrationRequestBodyModel) bodyModel, queryJson);
	}
}
