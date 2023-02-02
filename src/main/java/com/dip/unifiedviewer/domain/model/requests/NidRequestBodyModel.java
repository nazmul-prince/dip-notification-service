package com.dip.unifiedviewer.domain.model.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;
import com.dip.unifiedviewer.domain.validators.ValidDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class NidRequestBodyModel extends BaseRequestBodyModel {

	@Pattern(regexp = "([0-9]{10}|[0-9]{17})", message = "Nid Number must be 10 or 17 digits long.")
	@NotBlank(message = "requestKey must not be null")
	private String requestKey;

	@ValidDate(message = "dateOfBirth must be of format " + ApiExposureConstants.DATE_FORMAT)
	@NotBlank(message = "dateOfBirth must not be null")
	private String dateOfBirth;
}
