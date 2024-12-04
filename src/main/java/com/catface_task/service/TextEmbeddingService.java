package com.catface_task.service;


/**
 * @className: TextEmbeddingService
 * @author: Havoc.
 * @date: 2024/12/2 01:13
 * @version: 0.1
 * @description:
 */
public interface TextEmbeddingService {
    float[] getEmbedding(String text);
}
