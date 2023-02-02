package com.dip.unifiedviewer.domain.persistent.services;

import java.util.Map;

public interface AuthenticationPort {
    String getAdminAccessToken();

    Map<String, Map<String, String>> getAllUserAttributes(String accessToken);

    String getUserAccessToken(String username, String password);

    Map<String, String> getAttributeMap(String userId, String accessToken);

    void updateAttributes(String accessToken, String userId, Map<String, String> attributeMap);
}
