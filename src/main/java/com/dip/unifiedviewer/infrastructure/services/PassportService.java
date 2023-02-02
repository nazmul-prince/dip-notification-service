package com.dip.unifiedviewer.infrastructure.services;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.PassportRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.PassportRequestRepository;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_PASSPORT)
public class PassportService implements DomainPersistenceServicePort {

  private final PassportRequestRepository passportRequestRepository;

  public PassportService(PassportRequestRepository passportRequestRepository) {
    this.passportRequestRepository = passportRequestRepository;
  }

  @Override
  public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
    passportRequestRepository.executeRequest((PassportRequestBodyModel) bodyModel, queryJson);
  }
}
