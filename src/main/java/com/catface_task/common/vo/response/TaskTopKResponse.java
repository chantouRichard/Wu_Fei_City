package com.catface_task.common.vo.response;


import com.catface_task.common.model.Task;
import lombok.Data;

/**
 * @className: TaskTopKResponse
 * @author: Havoc.
 * @date: 2024/12/4 17:12
 * @version: 0.1
 * @description:
 */
@Data
public class TaskTopKResponse {
    private Task taskRaw;  // 原始基本信息
    private String explain; // 提供给 LLM 的中文综合描述

}
