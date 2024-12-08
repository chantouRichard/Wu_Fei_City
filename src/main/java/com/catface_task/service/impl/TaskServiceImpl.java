package com.catface_task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catface_task.common.dao_es.TaskES_DAO;
import com.catface_task.common.dao_es.TaskRepository;
import com.catface_task.common.model.taskEnums.TaskStatus;
import com.catface_task.common.model_es.TaskES;
import com.catface_task.common.vo.request.TaskAcceptRequest;
import com.catface_task.common.vo.request.TaskSelectRecentVo;
import com.catface_task.common.vo.request.TaskTopKRequest;
import com.catface_task.common.vo.response.TaskTopKResponse;
import com.catface_task.service.TextEmbeddingService;
import com.catface_task.utils.EmbeddingToStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catface_task.common.mapper.TaskMapper;
import com.catface_task.common.model.Task;
import com.catface_task.service.TasksService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private TaskES_DAO taskESDAO;

    @Autowired
    private TextEmbeddingService textEmbeddingService;

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
        // STAGE 2. ES
        TaskES taskES = new TaskES(task);
        // text embedding
        String embeddingExplain = EmbeddingToStringUtil.toEmbeddingString(taskES);
        float[] embedding = textEmbeddingService.getEmbedding(embeddingExplain);
        taskES.setEmbedding(embedding);
        rc = taskESDAO.addTask(taskES);
        if (!rc) { // ES 添加失败，MySQL 回滚。
            throw new IllegalArgumentException("ES添加任务失败");
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
        List<TaskES> res = taskESDAO.searchTasksByKeywords(params.getNum(), params.getSkip(), params.getKeywords());
        List<Integer> taskIds = res.stream().map(TaskES::getId).collect(Collectors.toList());

        if (taskIds.isEmpty()) {
            return null;
        }
        // 使用 taskIds 查询 Task 对象
        return taskMapper.selectByIds(taskIds);
    }

    @Override
    public List<TaskTopKResponse> TopK(TaskTopKRequest params) {
        // Stage 1. embedding
        float[] embedding = textEmbeddingService.getEmbedding(params.getQuery());

        // Stage 2. doc search & sort
        List<TaskES> res = taskESDAO.vectorSearch(embedding, params.getNum()); // TODO K ~ num
        List<Integer> taskIds = res.stream().map(TaskES::getId).collect(Collectors.toList());

        List<Task> tasks = taskMapper.selectByIds(taskIds);

        // 创建一个 Map 来存储 taskId 和其对应的索引
        Map<Integer, Integer> taskIdIndexMap = new HashMap<>();
        for (int i = 0; i < taskIds.size(); i++) {
            taskIdIndexMap.put(taskIds.get(i), i);
        }

        // 使用自定义比较器对 tasks 进行排序
        tasks.sort(Comparator.comparingInt(task -> taskIdIndexMap.get(task.getTaskId())));

        // Stage 3. explain for LLM
        List<TaskTopKResponse> taskTopKResponses = tasks.stream()
                .map(task -> {
                    String explain = EmbeddingToStringUtil.toEmbeddingString(task);
                    TaskTopKResponse response = new TaskTopKResponse();
                    response.setTaskRaw(task);
                    response.setExplain(explain);
                    return response;
                })
                .collect(Collectors.toList());

        return taskTopKResponses;
    }

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<TaskES> test(TaskSelectRecentVo params) {
        return taskRepository.findByTitleOrDescriptionOrPosition(params.getKeywords(),
                params.getKeywords(), params.getKeywords());
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
