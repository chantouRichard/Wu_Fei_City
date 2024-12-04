package com.catface_task.common.vo.response;


import lombok.Data;

/**
 * @className: Embedding
 * @author: Havoc.
 * @date: 2024/12/2 01:44
 * @version: 0.1
 * @description:
 */
@Data
public class EmbeddingResponse {
    private Integer status;
    private String message;
    private float[] embedding; // TODO 这里之后设计为泛型会更合适。
}
