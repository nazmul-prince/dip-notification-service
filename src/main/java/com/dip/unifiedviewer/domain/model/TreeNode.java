package com.dip.unifiedviewer.domain.model;

import java.util.List;

import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class TreeNode {
    private PublicApiWorkerTaskType type;

    private int expectedCount;

    private int priority;

    private List<TreeNode> children;
}
