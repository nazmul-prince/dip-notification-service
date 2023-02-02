package com.dip.unifiedviewer.domain.persistent.services;

import org.json.JSONArray;

import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;

public interface DomainPersistenceServicePort {
  void executeRequest(BaseRequestBodyModel bodyModel, JSONArray queryJson);
}
