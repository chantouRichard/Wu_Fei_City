package com.catface_task.common.dao_es;


import com.catface_task.common.model_es.TaskES;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * @className: Taskresposity
 * @author: Havoc.
 * @date: 2024/12/4 15:28
 * @version: 0.1
 * @description:
 */
@org.springframework.stereotype.Repository
public interface TaskRepository extends Repository<TaskES, Integer> {
    List<TaskES> findByTitleOrDescriptionOrPosition(String title, String description, String position);
}
