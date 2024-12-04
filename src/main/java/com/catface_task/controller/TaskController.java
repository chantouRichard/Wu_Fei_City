package com.catface_task.controller;

import com.catface_task.common.model_es.TaskES;
import com.catface_task.common.vo.request.TaskSelectRecentVo;
import com.catface_task.common.vo.request.TaskTopKRequest;
import com.catface_task.common.vo.response.ResponseBean;
import com.catface_task.common.vo.response.TaskTopKResponse;
import com.catface_task.service.TasksService;
import com.catface_task.common.model.Task;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
     * @brief 先调用 MySQL，然后处理到 ES 中。// INFO 利用事务传递概念。
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
//            SearchHits<TaskES>
            result = tasksService.searchByKeywords(params);
//            result = tasksService.test(params);
        } else {
//            List<Task>
            result = switch (params.getMode()) {
                case "poi" -> {
                    if (params.getRadius() < 500)
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
}
