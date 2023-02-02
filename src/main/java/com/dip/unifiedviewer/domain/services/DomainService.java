package com.dip.unifiedviewer.domain.services;

import java.util.Optional;

import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
import com.dip.unifiedviewer.domain.model.responses.CheckStatusResponseBodyModel;
import com.dip.unifiedviewer.domain.model.responses.InitiateRequestResponseBodyModel;

public interface DomainService {
	InitiateRequestResponseBodyModel executeRequest(BaseRequestBodyModel bodyModel);
	CheckStatusResponseBodyModel executeStatusCheckRequest(BaseRequestBodyModel bodyModel);
	Optional<String> executeDataFetchRequest(BaseRequestBodyModel bodyModel);
}