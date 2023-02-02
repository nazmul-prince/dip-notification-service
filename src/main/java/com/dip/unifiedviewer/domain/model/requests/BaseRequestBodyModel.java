package com.dip.unifiedviewer.domain.model.requests;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "rule",
    visible = true,
    defaultImpl = BaseRequestBodyModel.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NidRequestBodyModel.class, name = "NID"),
        @JsonSubTypes.Type(value = PassportRequestBodyModel.class, name = "PASSPORT"),
        @JsonSubTypes.Type(value = DrivingLicenseRequestBodyModel.class, name = "DRIVINGLICENSE"),
        @JsonSubTypes.Type(
                value = VehicleRegistrationRequestBodyModel.class,
                name = "VEHICLEREGISTRATION"),
        @JsonSubTypes.Type(value = MsisdnRequestBodyModel.class, name = "MSISDN"),
        @JsonSubTypes.Type(value = BirthRegistrationRequestBodyModel.class, name = "BIRTHREGISTRATION"),
        @JsonSubTypes.Type(value = PstnMsisdnRequestBodyModel.class, name = "PSTN_MSISDN"),
        @JsonSubTypes.Type(value = NbrRequestBodyModel.class, name = "NBR"),
        @JsonSubTypes.Type(value = EducationBoardRequestBodyModel.class, name = "EDUCATION_BOARD")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
//@ToString
public class BaseRequestBodyModel {

  @NotNull(message = "Must provide a valid rule")
  private PublicApiWorkerTaskType rule;

  @NotNull(message = "Must provide a username")
  private String username;

  @NotNull(message = "Must provide a password")
  private String password;

  @JsonIgnore
  private String userId;

  @JsonIgnore
  private String agencyId;

  @Deprecated private String caseId;

  private String requestId;

  @Size(min = 1, message = "RequestedFor must have 1 or more items")
  private Set<String> requestedFor;

  @JsonIgnore
  private Set<String> limitExceededList = new HashSet<>();

  private String searchMode = "DISCOVERY";

  private String apiType = "PUBLIC";

@Override
public String toString() {
	return " for agency Id: " + this.agencyId
			+ " for user id: " + this.userId
			+ " for searched: " + this.requestedFor
			+ " for limit count: " + this.limitExceededList;
}
  
}
