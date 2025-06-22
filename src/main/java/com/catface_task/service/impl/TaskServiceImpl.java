package com.catface_task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catface_task.common.model.taskEnums.TaskStatus;
import com.catface_task.common.vo.request.TaskAcceptRequest;
import com.catface_task.common.vo.request.TaskSelectRecentVo;
import com.catface_task.common.vo.request.TaskTopKRequest;
import com.catface_task.common.vo.response.TaskTopKResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catface_task.common.mapper.TaskMapper;
import com.catface_task.common.model.Task;
import com.catface_task.service.TasksService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @className: TaskServiceImpl
 * @author: Havoc.
 * @date: 2024/12/1 19:20
 * @version: 0.1
 * @description:
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TasksService {

    @Autowired
    private TaskMapper taskMapper;

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    @Transactional
    public boolean addTask(Task task) {
        task.setStatus(TaskStatus.WAITING);

        boolean rc = true;
        // STAGE 1. MySQL
        rc = taskMapper.insertTask(task) > 0;
        if (!rc) {
            return false;
        }
        return rc;
    }

    @Override
    public List<Task> getRecentTasks(TaskSelectRecentVo params) {
        if (params.getNum() == 0)
            params.setNum(10);
        return taskMapper.selectRecentTasks(params);
    }

    @Override
    public List<Task> selectByPOI(TaskSelectRecentVo params) {
        if (params.getNum() == 0)
            params.setNum(10);
        return taskMapper.selectByPOI(params);
    }

    @Override
    public  List<Task> selectByDepartment(TaskSelectRecentVo params) {
        if (params.getNum() == 0)
            params.setNum(10);
        return taskMapper.selectByDepartment(params);
    }

    @Override
    public List<Task> searchByKeywords(TaskSelectRecentVo params) {
        // TODO: 实现基于 MySQL 的关键词搜索
        return null;
    }

    @Override
    public List<TaskTopKResponse> TopK(TaskTopKRequest params) {
        // TODO: 实现基于 MySQL 的 TopK 搜索
        return null;
    }

    @Override
    public int deleteTask(int taskId) {
        Task task = new Task();
        task.setTaskId(taskId);
        task.setIsDeleted(true);
        task.setStatus(TaskStatus.CANCELED);
        return taskMapper.updateById(task);
    }

    @Override
    public int recoverTask(int taskId) {
        Task task = new Task();
        task.setTaskId(taskId);
        task.setStatus(TaskStatus.WAITING);
        task.setIsDeleted(false);
        return taskMapper.updateById(task);
    }

    @Override
    public int acceptTask(TaskAcceptRequest params) {
        Task task = new Task();
        task.setTaskId(params.getTaskId());
        task.setUserAcceptedId(params.getUserId());
        task.setStatus(TaskStatus.ACCEPTED);
        return taskMapper.updateById(task);
    }
}
