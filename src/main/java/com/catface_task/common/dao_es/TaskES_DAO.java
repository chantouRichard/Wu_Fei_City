package com.catface_task.common.dao_es;


import com.catface_task.common.model_es.TaskES;

import java.util.List;

/**
 * @className: TaskES_DAO
 * @author: Havoc.
 * @date: 2024/12/2 00:21
 * @version: 0.1
 * @description:
 */
public interface TaskES_DAO {

    boolean addTask(TaskES task);

    List<TaskES> searchTasksByKeywords(Integer num, Integer skip, String query);

    List<TaskES> vectorSearch(float[] queryVector, int size);
}
