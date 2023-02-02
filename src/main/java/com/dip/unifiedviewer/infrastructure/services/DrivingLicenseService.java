package com.dip.unifiedviewer.infrastructure.services;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.DrivingLicenseRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.DrivingLicenseRequestRepository;

/* Service class responsible for handling driving license requests */
@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_DRIVING_LICENSE)
public class DrivingLicenseService implements DomainPersistenceServicePort {

  private final DrivingLicenseRequestRepository drivingLicenseRequestRepository;

  public DrivingLicenseService(DrivingLicenseRequestRepository drivingLicenseRequestRepository) {
    this.drivingLicenseRequestRepository = drivingLicenseRequestRepository;
  }

  @Override
  public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
    drivingLicenseRequestRepository.executeRequest((DrivingLicenseRequestBodyModel) bodyModel, queryJson);
  }
}
