package com.dip.unifiedviewer.domain.model.requests;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/* request body model class for driving license requests*/
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class DrivingLicenseRequestBodyModel extends BaseRequestBodyModel {
    @NotBlank(message = "requestKey must not be null")
    private String requestKey;
}
