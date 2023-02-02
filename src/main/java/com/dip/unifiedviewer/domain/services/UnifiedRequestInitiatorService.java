package com.dip.unifiedviewer.domain.services;

import com.dip.unifiedviewer.domain.model.requests.BaseDataSourceRequestModel;

public interface UnifiedRequestInitiatorService {
	
	void initiateUnifiedRequest(BaseDataSourceRequestModel baseDataSourceRequestModel);

}
