package com.dip.unifiedviewer.constansts;

public interface KeycloakConstants {

    String KEYCLOAK_TOKEN_GENERATOR_URL_FORMAT =
            "%s/auth/realms/%s/protocol/openid-connect/token";
    String KEYCLOAK_GET_USER_INFO_URL_FORMAT =
            "%s/auth/admin/realms/%s/users/%s";
    String KEYCLOAK_GET_ALL_USERS_URL_FORMAT =
            "%s/auth/admin/realms/%s/users";
    String KEYCLOAK_UPDATE_USER_INFO_URL_FORMAT =
            "%s/auth/admin/realms/%s/users/%s";

    String ATTRIBUTE_KEY_SUBSTRING_DAILY = "DAILY";
    String ATTRIBUTE_KEY_SUBSTRING_PEAK = "PEAK";
    String ATTRIBUTE_KEY_SUBSTRING_OFFPEAK = "OFFPEAK";

    String ATTRIBUTE_KEY_SUBSTRING_START = "START";
    String ATTRIBUTE_KEY_SUBSTRING_END = "END";
    String ATTRIBUTE_KEY_SUBSTRING_QUOTA = "QUOTA";
    String ATTRIBUTE_KEY_SUBSTRING_REMAINING = "REMAINING";

    String KEYCLOAK_JSON_KEY_ATTRIBUTES = "attributes";
}
