package com.catface_task.common.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @className: GroupUserLink
 * @author: Havoc.
 * @date: 2024/12/6 11:30
 * @version: 0.1
 * @description:
 */
@Data
@TableName("group_user_links")
public class GroupUserLink implements Serializable {
    private Integer groupId;
    private Integer userId;
}
