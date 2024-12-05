package com.catface_task.common.model.taskEnums;

import lombok.Getter;

@Getter
public enum TaskLevel implements Describable {
    LOW("一般"),
    MEDIUM("中等"),
    HIGH("紧急");

    private final String description;
    TaskLevel(String description) {
        this.description = description;
    }
}
