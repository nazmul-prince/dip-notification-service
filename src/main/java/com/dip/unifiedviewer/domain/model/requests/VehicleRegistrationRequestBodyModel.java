package com.dip.unifiedviewer.domain.model.requests;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class VehicleRegistrationRequestBodyModel extends BaseRequestBodyModel {

	@NotBlank(message = "requestKey must not be null")
	private String requestKey;

	@NotBlank(message = "zone must not be null")
	private String zone;

	@NotBlank(message = "series must not be null")
	private String series;
}
