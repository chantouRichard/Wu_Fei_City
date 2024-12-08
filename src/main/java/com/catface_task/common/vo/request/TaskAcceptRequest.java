package com.catface_task.common.vo.request;

import lombok.Data;

/**
 * @className: TaskAcceptRequest
 * @author: Havoc.
 * @date: 2024/12/8 09:40
 * @version: 0.1
 * @description:
 */
@Data
public class TaskAcceptRequest {
    private Integer taskId;
    private Integer userId;

    private String message;
}
