//package com.dip.unifiedviewer.domain.factory.impls;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
//import com.dip.unifiedviewer.domain.factory.MasterTreeFactory;
//import com.dip.unifiedviewer.domain.model.TreeNode;
//import com.dip.unifiedviewer.domain.persistent.services.JsonTreeLoaderPort;
//
//@Component
//public class MasterTreeFactoryImpl implements MasterTreeFactory {
//
//  private static final Logger logger = LoggerFactory.getLogger(MasterTreeFactoryImpl.class);
//
//  private Map<PublicApiWorkerTaskType, TreeNode> treeRootMap;
//  private final JsonTreeLoaderPort jsonTreeLoader;
//  private final String jsonPrefix;
//
//  public MasterTreeFactoryImpl(JsonTreeLoaderPort jsonTreeLoader, @Value("${app.json-prefix}") String jsonPrefix) {
//    this.jsonTreeLoader = jsonTreeLoader;
//    this.jsonPrefix = jsonPrefix;
//
//    loadTrees();
//  }
//
//  private void loadTrees() {
//    treeRootMap = new HashMap<>();
//    Arrays.stream(PublicApiWorkerTaskType.values())
//        .forEach(type -> treeRootMap.put(type, jsonTreeLoader.getTreeByJsonName(jsonPrefix + type.getKey())));
//    logger.info("All master trees loaded successfully");
//  }
//
//  @Override
//  public TreeNode getTreeByType(PublicApiWorkerTaskType type) {
//    return treeRootMap.get(type);
//  }
//}
