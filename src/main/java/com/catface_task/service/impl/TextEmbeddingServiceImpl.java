package com.catface_task.service.impl;


import com.catface_task.common.vo.response.EmbeddingResponse;
import com.catface_task.service.TextEmbeddingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: TextEmbeddingServiceImpl
 * @author: Havoc.
 * @date: 2024/12/2 01:13
 * @version: 0.1
 * @description:
 */
@Service
public class TextEmbeddingServiceImpl implements TextEmbeddingService {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${micro-python.url}")
    private String apiRootUrl;

    /**
     * // TODO 目前写的比较繁琐，但只有这一出使用。后续再优化。
     * @param text
     * @return
     */
    @Override
    public float[] getEmbedding(String text) {
        String endpoint = "/rag/bge_embedding";
        String url = apiRootUrl + endpoint;

        try {
            // STAGE 1. 创建一个包含文本的JSON对象
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", text);
            // 将Map转换为JSON字符串
            String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

            // STAGE 2. 请求体v
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 创建 HTTP 请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);

            // 发送请求并获取响应
            String response = restTemplate.postForObject(url, requestEntity, String.class);

            // 解析响应体为 EmbeddingResponse 对象
            EmbeddingResponse embeddingResponse = objectMapper.readValue(response, EmbeddingResponse.class);

            if (embeddingResponse.getStatus() == 200) {
                return embeddingResponse.getEmbedding();
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
