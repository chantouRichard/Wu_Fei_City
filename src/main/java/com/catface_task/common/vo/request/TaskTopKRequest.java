package com.catface_task.common.vo.request;


import lombok.Data;

/**
 * @className: TaskTopKRequest
 * @author: Havoc.
 * @date: 2024/12/4 20:33
 * @version: 0.1
 * @description:
 */
@Data
public class TaskTopKRequest {
    private Integer num;
    private String query;
}
