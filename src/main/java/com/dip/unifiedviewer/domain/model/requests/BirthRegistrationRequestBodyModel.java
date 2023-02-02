package com.dip.unifiedviewer.domain.model.requests;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.validators.ValidDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class BirthRegistrationRequestBodyModel extends BaseRequestBodyModel {

	@NotBlank(message = "requestKey must not be null")
	private String requestKey;

	@ValidDate(message = "dateOfBirth must be of format " + ApiExposureConstants.DATE_FORMAT)
	@NotBlank(message = "dateOfBirth must not be null")
	private String dateOfBirth;
}
