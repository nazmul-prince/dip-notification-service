package com.dip.unifiedviewer.infrastructure.services;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.NbrRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.NbrRequestRepository;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_NBR)
public class NbrService implements DomainPersistenceServicePort {
    private final NbrRequestRepository nbrRequestRepository;

    public NbrService(NbrRequestRepository nbrRequestRepository) {
        this.nbrRequestRepository = nbrRequestRepository;
    }

    @Override
    public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
        NbrRequestBodyModel nbrRequestBodyModel = (NbrRequestBodyModel) bodyModel;
        nbrRequestRepository.executeRequest(nbrRequestBodyModel, queryJson);
    }
}
