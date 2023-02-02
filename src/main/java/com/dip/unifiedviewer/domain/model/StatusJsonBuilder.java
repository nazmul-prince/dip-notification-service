package com.dip.unifiedviewer.domain.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dip.unifiedviewer.constansts.JsonConstants;

public class StatusJsonBuilder implements JsonBuilder {

    private JSONArray jsonArray;

    public StatusJsonBuilder() {
        jsonArray = new JSONArray();
    }

    @Override
    public StatusJsonBuilder addJson(TreeNode node) {
        JSONObject json = new JSONObject();
        json.put("type", node.getType().getValue());
        json.put(JsonConstants.JSON_KEY_EXPECTED_COUNT, node.getExpectedCount());
        json.put(JsonConstants.JSON_KEY_CURRENT_COUNT, 0);
        jsonArray.put(json);
        return this;
    }

    @Override
    public JSONArray build() {
        return jsonArray;
    }
}
