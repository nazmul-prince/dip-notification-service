package com.dip.unifiedviewer.domain.factory.impls;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
import com.dip.unifiedviewer.domain.factory.PersistencePortFactory;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;

@Component
public class PersistencePortFactoryImpl implements PersistencePortFactory {

  Map<String, DomainPersistenceServicePort> implementationMap;

  public PersistencePortFactoryImpl(
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_NID) DomainPersistenceServicePort nidPort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_PASSPORT)
          DomainPersistenceServicePort passportPort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_DRIVING_LICENSE)
          DomainPersistenceServicePort drivingLicensePort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_VEHICLE_REGISTRATION)
          DomainPersistenceServicePort vehicleRegistrationPort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_MSISDN) DomainPersistenceServicePort msisdnPort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_BIRTH_REGISTRATION)
          DomainPersistenceServicePort birthRegistrationPort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_PSTN_MSISDN) DomainPersistenceServicePort pstnMsisdnPort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_NBR) DomainPersistenceServicePort nbrPort,
      @Qualifier(ApiExposureConstants.REQUEST_TYPE_EDUCATION_BOARD) DomainPersistenceServicePort educationBoardPort) {
    implementationMap = new HashMap<>();
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_NID, nidPort);
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_PASSPORT, passportPort);
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_DRIVING_LICENSE, drivingLicensePort);
    implementationMap.put(
        ApiExposureConstants.REQUEST_TYPE_VEHICLE_REGISTRATION, vehicleRegistrationPort);
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_MSISDN, msisdnPort);
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_BIRTH_REGISTRATION, birthRegistrationPort);
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_PSTN_MSISDN, pstnMsisdnPort);
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_NBR, nbrPort);
    implementationMap.put(ApiExposureConstants.REQUEST_TYPE_EDUCATION_BOARD, educationBoardPort);
  }

  @Override
  public DomainPersistenceServicePort getPortImpl(PublicApiWorkerTaskType taskType) {
    if (!implementationMap.containsKey(taskType.getValue())) {
      throw new RuntimeException("No port implementation for: " + taskType.getValue());
    }
    return implementationMap.get(taskType.getValue());
  }
}
