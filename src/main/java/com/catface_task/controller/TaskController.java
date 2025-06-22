package com.catface_task.controller;

import com.catface_task.common.vo.request.TaskAcceptRequest;
import com.catface_task.common.vo.request.TaskSelectRecentVo;
import com.catface_task.common.vo.request.TaskTopKRequest;
import com.catface_task.common.vo.response.ResponseBean;
import com.catface_task.common.vo.response.TaskTopKResponse;
import com.catface_task.service.TasksService;
import com.catface_task.common.model.Task;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @className: TaskController
 * @author: Havoc.
 * @date: 2024/12/1 19:23
 * @version: 0.1
 * @description:
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TasksService tasksService;

    /**
     * 发布任务
     * @brief 添加任务到数据库
     * @param task
     * @return
     */
    @PostMapping
    public ResponseBean addTask(@NotNull @RequestBody Task task) {
        boolean result = tasksService.addTask(task);

        return result ? ResponseBean.success() : ResponseBean.fail("添加任务失败");
    }

    @GetMapping
    public ResponseBean getRecentTasks(TaskSelectRecentVo params) {
        // TIP 这样就是默认绑定 Query 类型的数据。
        Object result;
        if (Objects.equals(params.getMode(), "keywords")) {
            result = tasksService.searchByKeywords(params);
        } else {
            result = switch (params.getMode()) {
                case "poi" -> {
                    if (params.getRadius() < 500)  // UPDATE 最好不要硬编码。
                        params.setRadius(500);
                    yield tasksService.selectByPOI(params);
                }
                case "department" -> tasksService.selectByDepartment(params);
                default -> tasksService.getRecentTasks(params);
            };
        }
        return ResponseBean.success(result);
    }

    @GetMapping("/topk")
    public ResponseBean getTaskTopK(TaskTopKRequest params) {
        List<TaskTopKResponse> result = tasksService.TopK(params);
        return ResponseBean.success(result);
    }

    // Simple 取消 && 恢复 && 接受任务
    @DeleteMapping
    public ResponseBean cancelTask(@RequestParam(value = "task_id") int taskId) {
        return tasksService.deleteTask(taskId) > 0 ? ResponseBean.success() : ResponseBean.fail("删除任务失败");
    }

    @PutMapping
    public ResponseBean recoverTask(@RequestParam(value = "task_id") int taskId) {
        return tasksService.recoverTask(taskId) > 0 ? ResponseBean.success() : ResponseBean.fail("恢复任务失败");
    }

    @PostMapping("/accept")
    public ResponseBean acceptTask(@RequestBody TaskAcceptRequest params) {
        return tasksService.acceptTask(params) > 0 ? ResponseBean.success() : ResponseBean.fail("接受任务失败");
    }
    
}
