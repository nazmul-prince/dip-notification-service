package com.dip.unifiedviewer.domain.model.responses;


import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UnifiedResponseNotificationMsg {

	@JsonProperty("type")
	private String type;
	
	@JsonProperty("totalCount")
	private Integer totalCount;

	@JsonProperty("recievedCount")
	private Integer recievedCount = 0;
	
	@JsonProperty("currentFailureCount")
	private Integer currentFailureCount = 0;
	
	@JsonProperty("currentSuccessCount")
	private Integer currentSuccessCount = 0;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("parentId")
	private String parentId;
	
	@JsonProperty("channelToPublish")
	private String channleToPublish;

	@JsonProperty("searchIdentifier")
	private String searchIdentifier;

	@JsonProperty("searchCriteria")
	private Integer searchCriteria;

	public boolean isFinished() {
		// TODO Auto-generated method stub
		return (totalCount == (currentFailureCount + currentSuccessCount));
	}
}