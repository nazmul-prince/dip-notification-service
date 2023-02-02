package com.dip.unifiedviewer.infrastructure.services;

import com.dip.unifiedviewer.domain.model.TreeNode;
import com.dip.unifiedviewer.domain.persistent.services.JsonTreeLoaderPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonTreeLoaderImpl implements JsonTreeLoaderPort {

    private static final Logger logger = LoggerFactory.getLogger(JsonTreeLoaderImpl.class);

    @Value("${app.json-directory}")
    private String jsonDirectory;

    private final ObjectMapper objectMapper;

    public JsonTreeLoaderImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public TreeNode getTreeByJsonName(String jsonName) {
        try {
            String fileName = getFileName(jsonName);
            InputStream inputStream = new ClassPathResource(fileName).getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            return objectMapper.readValue(bytes, TreeNode.class);
        } catch (IOException e)  {
            logger.warn("Could not read json: " + jsonName);
            return new TreeNode();
        }
    }

    private String getFileName(String jsonName) {
        return jsonDirectory + "/" + jsonName + ".json";
    }
}
