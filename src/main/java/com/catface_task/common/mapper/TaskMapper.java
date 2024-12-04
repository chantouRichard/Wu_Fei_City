package com.catface_task.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catface_task.common.model.Poi;
import com.catface_task.common.model.Task;
import com.catface_task.common.vo.request.TaskSelectRecentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @className: Task
 * @author: Havoc.
 * @date: 2024/11/30 10:50
 * @version: 0.1
 * @description:
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 插入任务
     * @param task 任务
     * @return 影响行数
     */
    int insertTask(Task task);

    List<Task> selectByPOI(TaskSelectRecentVo params); // TODO 之后传值优化。

    /**
     * 获取最近的任务，同时显示其状态，起到一个 “促进” 领取任务的效果。
     * @param params {  // TODO 如何方便的加入条件过滤？
     *               skip: 0
     *               num: 10
     * }
     * @return
     */
    List<Task> selectRecentTasks(TaskSelectRecentVo params);

    List<Task> selectByDepartment(TaskSelectRecentVo params);

    List<Task> selectByIds(@Param("list") List<Integer> ids);
}
