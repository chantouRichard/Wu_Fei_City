package com.catface_task.service;

public interface AIService {
    /**
     * 调用阿里云通义千问VL视觉API，传入图片URL和prompt，返回模型结果
     */
    String getQwenVLResponse(String imageUrl, String prompt);
}
