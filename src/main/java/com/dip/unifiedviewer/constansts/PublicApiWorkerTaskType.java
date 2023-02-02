package com.dip.unifiedviewer.constansts;

import lombok.Getter;

@Getter
public enum PublicApiWorkerTaskType {
  NID(ApiExposureConstants.REQUEST_TYPE_NID, "nid"),
  ESAF(ApiExposureConstants.REQUEST_TYPE_ESAF, "esaf"),
  PASSPORT(ApiExposureConstants.REQUEST_TYPE_PASSPORT, "passport"),
  DRIVINGLICENSE(ApiExposureConstants.REQUEST_TYPE_DRIVING_LICENSE, "drivingLicense"),
  VEHICLEREGISTRATION(
      ApiExposureConstants.REQUEST_TYPE_VEHICLE_REGISTRATION, "vehicleRegistration"),
  MSISDN(ApiExposureConstants.REQUEST_TYPE_MSISDN, "msisdn"),
  LRL(ApiExposureConstants.REQUEST_TYPE_LRL, "lrl"),
  SMS(ApiExposureConstants.REQUEST_TYPE_SMS, "sms"),
  CDR(ApiExposureConstants.REQUEST_TYPE_CDR, "cdr"),
  MOBILE_DEVICE_INFORMATION(
      ApiExposureConstants.REQUEST_TYPE_MOBILE_DEVICE_INFORMATION, "mobileDeviceInformation"),
  BIRTHREGISTRATION(ApiExposureConstants.REQUEST_TYPE_BIRTH_REGISTRATION, "birthRegistration"),
  LOCAL_ESAF(ApiExposureConstants.REQUEST_TYPE_LOCAL_ESAF, "localEsaf"),
  LOCAL_ESAF_THEN_ESAF(ApiExposureConstants.REQUEST_TYPE_LOCAL_ESAF_THEN_ESAF, "localEsafThenEsaf"),
  MNP(ApiExposureConstants.REQUEST_TYPE_MNP, "mnp"),
  PSTN_MSISDN(ApiExposureConstants.REQUEST_TYPE_PSTN_MSISDN, "pstnMsisdn"),
  PSTN_ESAF(ApiExposureConstants.REQUEST_TYPE_PSTN_ESAF, "pstn-esaf"),
  NBR(ApiExposureConstants.REQUEST_TYPE_NBR, "nbr"),
  EDUCATION_BOARD(ApiExposureConstants.REQUEST_TYPE_EDUCATION_BOARD, "educationBoard"),
  ROOT("ROOT", "root"),
  DEFAULT("DEFAULT", "default");

  private String value;
  private String key;

  PublicApiWorkerTaskType(String value, String key) {
    this.value = value;
    this.key = key;
  }

  public static PublicApiWorkerTaskType getWorkerType(String requestData) {
    return PublicApiWorkerTaskType.valueOf(requestData);
  }

  @Override
  public String toString() {
    return this.getValue();
  }
}
