package com.dip.unifiedviewer.gateway.services.impls;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dip.unifiedviewer.gateway.services.QueryServiceProGatewayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuerySErviceProGatewayServiceImpl implements QueryServiceProGatewayService {

	@Value("${dip.query-service-pro.baseUrl}")
	private String baseUrl;

	@Value("${dip.query-service-pro.port}")
	private String port;

	@Value("${dip.query-service-pro.search-data-source-path}")
	private String searchPath;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void postRequest(String requestBody, String type) {
		String url = baseUrl + ":"  + port + searchPath + type.toLowerCase();

		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(url, request, String.class);
		
		log.info("status code: " + responseEntityStr.getHeaders());
	}

}
