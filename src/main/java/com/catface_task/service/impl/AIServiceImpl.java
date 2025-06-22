package com.catface_task.service.impl;

import com.catface_task.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;

@Service
public class AIServiceImpl implements AIService {

    // 阿里云百炼API配置
    @Value("${aliyun.dashscope.api-key}")
    private String aliyunApiKey;

    @Value("${aliyun.dashscope.base-url}")
    private String aliyunBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 调用阿里云通义千问VL视觉API，传入图片URL和prompt，返回模型结果
     */
    @Override
    public String getQwenVLResponse(String imageUrl, String prompt) {
        try {
            MultiModalConversation conv = new MultiModalConversation();
            MultiModalMessage userMessage = MultiModalMessage.builder()
                    .role(Role.USER.getValue())
                    .content(Arrays.asList(
                            Collections.singletonMap("image", imageUrl),
                            Collections.singletonMap("text", prompt)
                    )).build();
            MultiModalConversationParam param = MultiModalConversationParam.builder()
                    .apiKey(aliyunApiKey)
                    .model("qwen-vl-max-latest")
                    .messages(Collections.singletonList(userMessage))
                    .build();
            MultiModalConversationResult result = conv.call(param);
            // 取第一个回复内容
            if (result.getOutput() != null &&
                result.getOutput().getChoices() != null &&
                !result.getOutput().getChoices().isEmpty() &&
                result.getOutput().getChoices().get(0).getMessage() != null &&
                result.getOutput().getChoices().get(0).getMessage().getContent() != null &&
                !result.getOutput().getChoices().get(0).getMessage().getContent().isEmpty()) {
                Object text = result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text");
                return text != null ? text.toString() : "";
            }
            return "无返回内容";
        } catch (ApiException | NoApiKeyException | UploadFileException e) {
            throw new RuntimeException("阿里云通义千问VL API调用失败: " + e.getMessage(), e);
        }
    }
}