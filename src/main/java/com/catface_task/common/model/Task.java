package com.catface_task.common.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.catface_task.annotation.EmbeddingExplain;
import lombok.Data;
import org.apache.ibatis.annotations.Result;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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

    @EmbeddingExplain("任务标题")
    private String title;

    @EmbeddingExplain("任务内容细节描述")
    private String description;

    @EmbeddingExplain("任务位置描述")
    private String position; // TODO 之后也换成 enum

    @EmbeddingExplain("任务状态")
    private TaskStatus status;

    @EmbeddingExplain("任务急迫程度")
    private TaskLevel level;

    @EmbeddingExplain("任务特点标签")
    private String[] tags;

    private TaskTime time;

    @EmbeddingExplain("任务所在学部")
    private DepartmentWHU department;
    private Poi poi;

    private LocalDateTime createdAt;  // MySQL 默认插入时间；

    private Integer userAcceptedId;
}
