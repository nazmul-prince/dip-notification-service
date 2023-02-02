package com.dip.unifiedviewer.infrastructure.services;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.VehicleRegistrationRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.VehicleRegistrationRequestRepository;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_VEHICLE_REGISTRATION)
public class VehicleRegistrationService implements DomainPersistenceServicePort {

  private final VehicleRegistrationRequestRepository vehicleRegistrationRequestRepository;

  public VehicleRegistrationService(
      VehicleRegistrationRequestRepository vehicleRegistrationRequestRepository) {
    this.vehicleRegistrationRequestRepository = vehicleRegistrationRequestRepository;
  }

  @Override
  public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
    vehicleRegistrationRequestRepository.executeRequest(
        (VehicleRegistrationRequestBodyModel) bodyModel, queryJson);
  }
}
