package com.catface_task.service;

public interface AIService {
    // 文心一言文本对话
    String getWenxinResponse(String message, String imageBase64);

    // 语音转文本（语音识别）
    String speechToText(String voiceBase64);

    // 文本转语音（语音合成）
    String textToSpeech(String text);

    // 图像分析（物品识别/垃圾分类）
    String analyzeImage(String imageBase64);
}
