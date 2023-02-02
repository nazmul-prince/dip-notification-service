package com.dip.unifiedviewer.domain.model;

import org.json.JSONArray;

public interface JsonBuilder {

    JsonBuilder addJson(TreeNode node);
    JSONArray build();
}
