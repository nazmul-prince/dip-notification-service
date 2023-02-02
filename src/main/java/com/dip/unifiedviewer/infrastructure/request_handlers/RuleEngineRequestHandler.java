package com.dip.unifiedviewer.infrastructure.request_handlers;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RuleEngineRequestHandler implements RequestHandler {

  private final Logger logger = LoggerFactory.getLogger(RuleEngineRequestHandler.class);

  @Override
  public void executeRequest(String url, String jsonPayload) {
    logger.info("request body model: " + jsonPayload);
    CloseableHttpClient cHttpClient = HttpClients.custom().build();
    doPOST(url, jsonPayload, cHttpClient);
  }

  private String doPOST(String url, String jsonPayload, CloseableHttpClient cHttpClient) {
    String response = null;
    CloseableHttpResponse httpResponse = null;
    try {
      HttpPost httpPost = new HttpPost(url);
      httpPost.addHeader("Content-type", "application/json");
      httpPost.setHeader("cache-control", "no-cache,no-cache,no-cache");
      httpPost.setEntity(new StringEntity(jsonPayload));
      httpResponse = cHttpClient.execute(httpPost);
      response = EntityUtils.toString(httpResponse.getEntity());
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
