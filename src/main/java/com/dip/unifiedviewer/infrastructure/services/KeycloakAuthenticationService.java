package com.dip.unifiedviewer.infrastructure.services;

import static com.dip.unifiedviewer.constansts.KeycloakConstants.KEYCLOAK_GET_ALL_USERS_URL_FORMAT;
import static com.dip.unifiedviewer.constansts.KeycloakConstants.KEYCLOAK_GET_USER_INFO_URL_FORMAT;
import static com.dip.unifiedviewer.constansts.KeycloakConstants.KEYCLOAK_TOKEN_GENERATOR_URL_FORMAT;
import static com.dip.unifiedviewer.constansts.KeycloakConstants.KEYCLOAK_UPDATE_USER_INFO_URL_FORMAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.constansts.KeycloakConstants;
import com.dip.unifiedviewer.domain.persistent.services.AuthenticationPort;

@Service
public class KeycloakAuthenticationService implements AuthenticationPort {

  @Value("${dip.publicapi.keycloak.baseUrl}")
  private String baseUrl;

  @Value("${dip.publicapi.keycloak.clientId}")
  private String clientId;

  @Value("${dip.publicapi.keycloak.clientSecret}")
  private String clientSecret;

  @Value("${dip.publicapi.keycloak.realm}")
  private String realm;

  @Value("${dip.publicapi.keycloak.admin.realm}")
  private String adminRealm;

  @Value("${dip.publicapi.keycloak.admin.clientId}")
  private String adminClientId;

  @Value("${dip.publicapi.keycloak.admin.username}")
  private String adminUsername;

  @Value("${dip.publicapi.keycloak.admin.password}")
  private String adminPassword;

  private final KeycloakRequestHandler keycloakRequestHandler;

  public KeycloakAuthenticationService(KeycloakRequestHandler keycloakRequestHandler) {
    this.keycloakRequestHandler = keycloakRequestHandler;
  }

  @Override
  public String getAdminAccessToken() {
    String url = String.format(KEYCLOAK_TOKEN_GENERATOR_URL_FORMAT, baseUrl, adminRealm);
    List<NameValuePair> payload = new ArrayList<>();
    payload.add(new BasicNameValuePair("client_id", adminClientId));
    payload.add(new BasicNameValuePair("username", adminUsername));
    payload.add(new BasicNameValuePair("password", adminPassword));
    payload.add(new BasicNameValuePair("grant_type", "password"));
    String response = keycloakRequestHandler.postRequest(url, payload);
    return getAccessTokenFromJsonString(response);
  }

  @Override
  public Map<String, Map<String, String>> getAllUserAttributes(String accessToken) {
    String url = String.format(KEYCLOAK_GET_ALL_USERS_URL_FORMAT, baseUrl, realm);
    String response = keycloakRequestHandler.getRequest(url, accessToken);
    JSONArray allUsersJson = new JSONArray(response);
    Map<String, Map<String, String>> allUserAttributeMap = new HashMap<>();
    for (int i=0; i<allUsersJson.length(); i++) {
      JSONObject userInfoJson = allUsersJson.getJSONObject(i);
      if (userInfoJson.has("attributes")) {
        String userId = userInfoJson.getString("id");
        allUserAttributeMap.put(userId, getAttributesMap(userInfoJson));
      }
    }
    return allUserAttributeMap;
  }

  @Override
  public String getUserAccessToken(String username, String password) {
    String url = String.format(KEYCLOAK_TOKEN_GENERATOR_URL_FORMAT, baseUrl, realm);
    List<NameValuePair> payload = new ArrayList<>();
    payload.add(new BasicNameValuePair("client_id", clientId));
    payload.add(new BasicNameValuePair("username", username));
    payload.add(new BasicNameValuePair("password", password));
    payload.add(new BasicNameValuePair("grant_type", "password"));
    payload.add(new BasicNameValuePair("client_secret", clientSecret));
    String response = keycloakRequestHandler.postRequest(url, payload);
    return getAccessTokenFromJsonString(response);
  }

  private String getAccessTokenFromJsonString(String jsonString) {
    JSONObject jsonObject = new JSONObject(jsonString);
    try {
      return jsonObject.getString("access_token");
    } catch (JSONException jsonException) {
      return null;
    }
  }

  @Override
  public Map<String, String> getAttributeMap(String userId, String accessToken) {
    String url = String.format(KEYCLOAK_GET_USER_INFO_URL_FORMAT, baseUrl, realm, userId);
    String response = keycloakRequestHandler.getRequest(url, accessToken);
    return getAttributesMap(new JSONObject(response));
  }

  @Override
  public void updateAttributes(
      String accessToken, String userId, Map<String, String> attributeMap) {
    JSONObject json = new JSONObject();
    json.put(KeycloakConstants.KEYCLOAK_JSON_KEY_ATTRIBUTES, getAttributeJson(attributeMap));
    String url = String.format(KEYCLOAK_UPDATE_USER_INFO_URL_FORMAT, baseUrl, realm, userId);
    keycloakRequestHandler.putRequest(url, accessToken, json.toString());
  }

  private JSONObject getAttributeJson(Map<String, String> attributeMap) {
    JSONObject attributeJson = new JSONObject();
    for (String key : attributeMap.keySet()) {
      JSONArray jsonArray = new JSONArray();
      jsonArray.put(attributeMap.get(key));
      attributeJson.put(key, jsonArray);
    }
    return attributeJson;
  }

  private Map<String, String> getAttributesMap(JSONObject userInfoJson) {
    Map<String, String> attributeMap = new HashMap<>();
    if (!userInfoJson.has(KeycloakConstants.KEYCLOAK_JSON_KEY_ATTRIBUTES)) return attributeMap;
    JSONObject attributesJson = userInfoJson.getJSONObject(KeycloakConstants.KEYCLOAK_JSON_KEY_ATTRIBUTES);

    for (String key : attributesJson.keySet()) {
      JSONArray list = attributesJson.getJSONArray(key);
      if (list.length() == 0) continue;
      attributeMap.put(key, list.getString(0));
    }
    return attributeMap;
  }
}
