package com.dip.unifiedviewer.domain.services;

import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;

public interface AuthenticationService {
    String authenticateUser(BaseRequestBodyModel requestBodyModel);
    void adjustParameters(BaseRequestBodyModel requestBodyModel);
}
