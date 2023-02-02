package com.dip.unifiedviewer.domain.services;

import org.json.JSONObject;

public interface ErrorResponseGenerator {
    JSONObject generateErrorResponse(JSONObject jsonResponse);
}
