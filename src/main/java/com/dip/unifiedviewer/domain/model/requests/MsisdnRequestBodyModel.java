package com.dip.unifiedviewer.domain.model.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class MsisdnRequestBodyModel extends BaseRequestBodyModel {

  @Pattern(
      regexp = "8801[356789][0-9]{8}",
      message = "requestKey must be a valid mobile number starting with 880")
  @NotBlank(message = "requestKey must not be null")
  private String requestKey;

  @Pattern(regexp = "[0-9]+", message = "startDate must be a valid epoch time")
//  @NotBlank(message = "startDate must not be null")
  private String startDate;

  @Pattern(regexp = "[0-9]+", message = "endDate must be a valid epoch time")
//  @NotBlank(message = "endDate must not be null")
  private String endDate;
}
