package com.dip.unifiedviewer.domain.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class QueryJsonBuilder implements JsonBuilder {

    private JSONArray jsonArray;

    public QueryJsonBuilder() {
        jsonArray = new JSONArray();
    }

    @Override
    public QueryJsonBuilder addJson(TreeNode node) {
        JSONObject json = new JSONObject();
        json.put("type", node.getType().getValue());
        json.put("priority", node.getPriority());
        jsonArray.put(json);
        return this;
    }

    @Override
    public JSONArray build() {
        return jsonArray;
    }
}
