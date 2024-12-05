package com.catface_task.common.model.taskEnums;

import lombok.Getter;

@Getter
public enum DepartmentWHU implements Describable {
    IT("信部"),
    MEDICAL("医学部"),
    ENGINE("工部"),
    ART("文理"),
    LAKE("湖滨"),
    MAPLE("枫园"),
    NET("网安"),
    OTHER("其他");

    private final String description;

    DepartmentWHU(String description) {
        this.description = description;
    }

}
