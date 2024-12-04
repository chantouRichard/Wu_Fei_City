package com.catface_task.common.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.Duration;

/**
 * @className: TaskTime
 * @author: Havoc.
 * @date: 2024/12/1 19:41
 * @version: 0.1
 * @description:
 */
@Data
public class TaskTime {
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private Duration estimatedMinDuration;
    private Duration estimatedMaxDuration;
}
