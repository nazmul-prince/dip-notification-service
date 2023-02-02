package com.dip.unifiedviewer.domain.persistent.services;

import com.dip.unifiedviewer.domain.model.TreeNode;

public interface JsonTreeLoaderPort {

    TreeNode getTreeByJsonName(String jsonName);
}
