package com.dip.unifiedviewer.domain.model.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbrRequestBodyModel extends BaseRequestBodyModel {
    @NotNull(message = "requestKey must not be null")
    private String requestKey;
    @NotNull(message = "requestValue must not be null")
    private String requestValue;
}
