package com.dip.unifiedviewer.domain.model.requests;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.dip.unifiedviewer.domain.model.responses.UnifiedResponseNotificationMsg;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseDataSourceRequestModel {

	@JsonProperty("totalCount")
	private Integer totalCount;

	@JsonProperty("id")
	private String id;

//	@JsonProperty("parentId")
//	private String parentId;

	@JsonProperty("channelToPublish")
	private String channelToPublish;

	private Map<String, Object> requestBody = new HashMap<>();

	// for collecting unmapped field
	@JsonAnySetter
	public void unmappedFields(String key, Object value) {
		this.requestBody.put(key, value);
	}

	public String getRequestType() {
		return requestBody.get("type").toString();
	}

	public String getSearchIdentifier() {
		Object object = requestBody.get("searchIdentifier");
		return Objects.equals(object, null) ? "0" : object.toString();
	}

	public String getSearchCriteria() {
		Object object = requestBody.get("searchCriteria");
		return Objects.equals(object, null) ? "0" : object.toString();
	}
	
}
