package com.dip.unifiedviewer.infrastructure.services;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.requests.MsisdnRequestBodyModel;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;
import com.dip.unifiedviewer.infrastructure.repositories.MsisdnRequestRepository;
import com.dip.unifiedviewer.util.TimeUtil;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Qualifier(ApiExposureConstants.REQUEST_TYPE_MSISDN)
public class MsisdnService implements DomainPersistenceServicePort {

  private final MsisdnRequestRepository msisdnRequestRepository;

  public MsisdnService(MsisdnRequestRepository msisdnRequestRepository) {
    this.msisdnRequestRepository = msisdnRequestRepository;
  }

  @Override
  public void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson) {
    MsisdnRequestBodyModel msisdnRequestBodyModel = (MsisdnRequestBodyModel) bodyModel;

    if (msisdnRequestBodyModel.getStartDate() == null) {
      msisdnRequestBodyModel.setStartDate(
          TimeUtil.getEpochTimeFromLocalDateTime(LocalDateTime.now()));
    }

    if (msisdnRequestBodyModel.getEndDate() == null) {
      LocalDateTime startDate =
          TimeUtil.getLocalDateTimeFromEpochSecond(
              Long.parseLong(msisdnRequestBodyModel.getStartDate()));
      msisdnRequestBodyModel.setEndDate(
          TimeUtil.getEpochTimeFromLocalDateTime(
              startDate.minusDays(ApiExposureConstants.DEFAULT_TIME_RANGE_IN_DAYS)));
    }
    msisdnRequestRepository.executeRequest(msisdnRequestBodyModel, queryJson);
  }
}
