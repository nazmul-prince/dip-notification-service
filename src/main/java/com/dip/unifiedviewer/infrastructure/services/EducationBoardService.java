package com.dip.unifiedviewer.infrastructure.services;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.EducationBoardRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.EducationBoardRequestRepository;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_EDUCATION_BOARD)
public class EducationBoardService implements DomainPersistenceServicePort {

    private final EducationBoardRequestRepository educationBoardRequestRepository;

    public EducationBoardService(EducationBoardRequestRepository educationBoardRequestRepository) {
        this.educationBoardRequestRepository = educationBoardRequestRepository;
    }

    @Override
    public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
        educationBoardRequestRepository.executeRequest((EducationBoardRequestBodyModel) bodyModel, queryJson);
    }
}
