package com.dip.unifiedviewer.infrastructure.services;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.PstnMsisdnRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.PstnMsisdnRequestRepository;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_PSTN_MSISDN)
public class PstnMsisdnService implements DomainPersistenceServicePort {
    private final PstnMsisdnRequestRepository pstnMsisdnRequestRepository;

    public PstnMsisdnService(PstnMsisdnRequestRepository pstnMsisdnRequestRepository) {
        this.pstnMsisdnRequestRepository = pstnMsisdnRequestRepository;
    }

    @Override
    public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
        PstnMsisdnRequestBodyModel pstnMsisdnRequestBodyModel = (PstnMsisdnRequestBodyModel) bodyModel;
        pstnMsisdnRequestRepository.executeRequest(pstnMsisdnRequestBodyModel, queryJson);
    }
}
