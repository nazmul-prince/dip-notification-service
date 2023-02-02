package com.dip.unifiedviewer.util;

import static com.dip.unifiedviewer.constansts.JsonConstants.*;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

public class JSONUtil {

	private JSONUtil() {}

	public static JSONObject getJSONbyPath(JSONObject jsonObject, List<String> path) {
		JSONObject rootJsonObject = jsonObject;
		for (String prefix : path) {
			rootJsonObject = rootJsonObject.getJSONObject(prefix);
		}
		return rootJsonObject;
	}

	public static JSONObject getErrorJson(String message, HttpStatus status) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_KEY_TIMESTAMP, LocalDateTime.now().toString());
		jsonObject.put(JSON_KEY_STATUS_CODE, status.value());
		jsonObject.put(JSON_KEY_STATUS_MESSAGE, message);
		return jsonObject;
	}

	public static JSONObject getInitialDataJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_KEY_RESPONSE_RECORDS, new JSONArray());
		jsonObject.put(JSON_KEY_STATUS_CODE, HttpStatus.OK.value());
		jsonObject.put(JSON_KEY_STATUS_MESSAGE, JSON_KEY_SUCCESS);
		return jsonObject;
	}
}
