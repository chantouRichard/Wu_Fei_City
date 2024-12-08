package com.catface_task.common.model.taskEnums;


import lombok.Getter;

/**
 * @className: TaskStatus
 * @author: Havoc.
 * @date: 2024/11/30 10:17
 * @version: 0.1
 * @description:
 */
@Getter
public enum TaskStatus implements Describable {
    WAITING("等待中"),
    COMPLETED("已完成"),
    ACCEPTED("已接取"),
    CANCELED("已取消");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

}
