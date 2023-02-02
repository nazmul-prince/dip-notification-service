package com.dip.unifiedviewer.domain.model.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class PassportRequestBodyModel extends BaseRequestBodyModel {

    @Pattern(regexp = "([A-Z]{2}[0-9]{7})|([0-9]{10}|[0-9]{17})", message = "requestKey must be a valid passport or NID number")
    @NotBlank(message = "requestKey must not be null")
    private String requestKey;
}
