package com.catface_task.common.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.catface_task.common.model.taskEnums.DepartmentWHU;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @className: Group
 * @author: Havoc.
 * @date: 2024/12/6 11:13
 * @version: 0.1
 * @description:
 */
@Data
@TableName("groups")
public class Group implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer groupId;

    // 组群基本描述
    private String name;
    private String description;

    // 粗、细 地点。
    private DepartmentWHU department;
    private String position;
}
