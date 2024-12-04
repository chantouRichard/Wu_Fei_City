package com.catface_task.common.model_es;

import com.catface_task.annotation.EmbeddingExplain;
import com.catface_task.common.model.Task;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @className: Task
 * @author: Havoc.
 * @date: 2024/12/2 00:12
 * @version: 0.1
 * @description:
 */
@Data
@Document(indexName = "catface_tasks")
public class TaskES {

    @Id
    private Integer id;

    @EmbeddingExplain("任务标题")
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    @EmbeddingExplain("任务内容细节描述")
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String description;

    @EmbeddingExplain("任务位置")
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String position;

    @EmbeddingExplain("任务特点标签")
    @Field(type = FieldType.Keyword)
    private String[] tags;

    @Field(type = FieldType.Dense_Vector, dims = 768, index = true, similarity = "cosine")
    private float[] embedding;

    @EmbeddingExplain("任务开始时间")  // TODO
    @Field(type = FieldType.Date)
    private Long startTime;
//
    @EmbeddingExplain("任务截止时间")
    @Field(type = FieldType.Date)
    private Long deadline;

    public TaskES() {};
    /**
     * 构造函数，直接从 Task 对象创建 TaskES 对象
     */
    public TaskES(Task task) {
        this.id = task.getTaskId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.position = task.getPosition();
        this.tags = task.getTags();

//        this.startTime = task.getTime().getStartTime();
//        this.deadline = task.getTime().getDeadline();
        // next embeddings
    }
}
