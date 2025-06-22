package com.catface_task.common.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.catface_task.common.model.taskEnums.DepartmentWHU;
import com.catface_task.common.model.taskEnums.TaskLevel;
import com.catface_task.common.model.taskEnums.TaskStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @className: Task
 * @author: Havoc.
 * @date: 2024/11/30 09:50
 * @version: 0.1
 * @description: 发布的任务MODEL
 */
@Data
@TableName("tasks")
public class Task implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer taskId;
    private Integer userId;

    private String title;

    private String description;

    private String position; // TODO 之后也换成 enum

    private TaskStatus status;

    private TaskLevel level;

    private String[] tags;

    private TaskTime time;

    private DepartmentWHU department;
    private Poi poi;

    private LocalDateTime createdAt;  // MySQL 默认插入时间；

    //
    private Boolean isDeleted;  // 单独一个字段，方便 MyBatis 快速过滤。
    private Integer userAcceptedId;  // TODO 之后肯定需要更多的 "沟通" 处理。

}
