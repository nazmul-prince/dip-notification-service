package com.dip.unifiedviewer.domain.model.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class PstnMsisdnRequestBodyModel extends BaseRequestBodyModel {
    @NotBlank(message = "requestKey must not be null")
    private String requestKey;
}
