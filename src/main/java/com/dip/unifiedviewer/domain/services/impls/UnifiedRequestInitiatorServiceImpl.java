package com.dip.unifiedviewer.domain.services.impls;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.domain.model.requests.BaseDataSourceRequestModel;
import com.dip.unifiedviewer.domain.services.RequestInitiatorTemplate;
import com.dip.unifiedviewer.domain.services.UnifiedRequestInitiatorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UnifiedRequestInitiatorServiceImpl implements UnifiedRequestInitiatorService{
	
	private RequestInitiatorTemplate requestInitiatorTemplate;
	
	

	@Async("threadPoolTaskExecutor")
	@Override
	public void initiateUnifiedRequest(BaseDataSourceRequestModel baseDataSourceRequestModel) {
		requestInitiatorTemplate.initiateUnifiedRequest(baseDataSourceRequestModel);
	}

}
