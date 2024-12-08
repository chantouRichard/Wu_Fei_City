package com.catface_task.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.catface_task.common.model.Task;
import com.catface_task.common.model_es.TaskES;
import com.catface_task.common.vo.request.TaskAcceptRequest;
import com.catface_task.common.vo.request.TaskSelectRecentVo;
import com.catface_task.common.vo.request.TaskTopKRequest;
import com.catface_task.common.vo.response.TaskTopKResponse;

import java.util.List;

/**
 * @className: TasksService
 * @author: Havoc.
 * @date: 2024/12/1 19:18
 * @version: 0.1
 * @description:
 */
public interface TasksService extends IService<Task> {

    boolean addTask(Task task);

    List<Task> getRecentTasks(TaskSelectRecentVo params);

    List<Task> selectByPOI(TaskSelectRecentVo params);

    List<Task> selectByDepartment(TaskSelectRecentVo params);

    List<Task> searchByKeywords(TaskSelectRecentVo params);

    List<TaskTopKResponse> TopK(TaskTopKRequest params);

    List<TaskES> test(TaskSelectRecentVo params);

    // 横向业务
    int deleteTask(int taskId);

    int recoverTask(int taskId);

    int acceptTask(TaskAcceptRequest params);
}
