package com.catface_task;


import com.catface_task.common.mapper.TaskMapper;
import com.catface_task.common.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @className: MyBatisTest
 * @author: Havoc.
 * @date: 2024/11/30 10:54
 * @version: 0.1
 * @description:
 */
@SpringBootTest
public class MyBatisTest {

    @Autowired
    private TaskMapper taskMapper;

//    @Test
//    public void testSelect() {
//        System.out.println(("----- selectAll method test ------"));
//        List<Task> task_list = taskMapper.selectList(null);
//        Assert.isTrue(5 == task_list.size(), "");
//        task_list.forEach(System.out::println);
//    }

}
