package com.dip.unifiedviewer.infrastructure.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KeycloakRequestHandler {

  private static final Logger logger = LoggerFactory.getLogger(KeycloakRequestHandler.class);

  private final SSLConnectionSocketFactory connectionSocketFactory;

  public KeycloakRequestHandler()
      throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    connectionSocketFactory =
        new SSLConnectionSocketFactory(
            SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
            NoopHostnameVerifier.INSTANCE);
  }

  public String postRequest(String url, List<NameValuePair> payload) {
    HttpPost httpPost = new HttpPost(url);
    httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
    try {
      httpPost.setEntity(new UrlEncodedFormEntity(payload, "UTF-8"));
      return submitRequest(httpPost);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getRequest(String url, String accessToken) {
    HttpGet httpGet = new HttpGet(url);
    String bearerToken = "Bearer " + accessToken;
    httpGet.addHeader("Authorization", bearerToken);
    httpGet.addHeader("Content-Type", "application/json");
    return submitRequest(httpGet);
  }

  public String putRequest(String url, String accessToken, String payload) {
    HttpPut httpPut = new HttpPut(url);
    String bearerToken = "Bearer " + accessToken;
    httpPut.addHeader("Authorization", bearerToken);
    httpPut.addHeader("Content-type", "application/json");
    try {
      httpPut.setEntity(new StringEntity(payload));
      return submitRequest(httpPut);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String submitRequest(HttpRequestBase httpPost) {
    CloseableHttpClient cHttpClient =
        HttpClients.custom().setSSLSocketFactory(connectionSocketFactory).build();
    String response = null;
    CloseableHttpResponse httpResponse = null;
    try {
      logger.info(httpPost.getURI().toString());
      httpResponse = cHttpClient.execute(httpPost);
      HttpEntity entity = httpResponse.getEntity();
      if (entity != null) {
        response = EntityUtils.toString(httpResponse.getEntity());
        logger.info(response);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Server Error", e);
    } finally {
      if (response != null) {
        try {
          httpResponse.close();
        } catch (IOException e) {
          logger.error("Couldn't close response properly", e);
        }
      }
    }
    return response;
  }
}
