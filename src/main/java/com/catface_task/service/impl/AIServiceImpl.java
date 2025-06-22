package com.catface_task.service.impl;

import com.catface_task.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.*;

@Service
public class AIServiceImpl implements AIService {

    // 文心一言API配置
    @Value("${wenxin.api.key}")
    private String wenxinApiKey;

    @Value("${wenxin.api.secret}")
    private String wenxinApiSecret;

    @Value("${wenxin.api.chat-url}")
    private String wenxinChatUrl;

    @Value("${wenxin.api.image-url}")
    private String wenxinImageUrl;

    // 语音API配置
    @Value("${baidu.speech.api-key}")
    private String speechApiKey;

    @Value("${baidu.speech.secret-key}")
    private String speechSecretKey;

    @Value("${baidu.speech.asr-url}")
    private String speechAsrUrl;

    @Value("${baidu.speech.tts-url}")
    private String speechTtsUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private String wenxinAccessToken;
    private long tokenExpireTime;

    @Override
    public String getWenxinResponse(String message, String imageBase64) {
        // 获取access token（自动刷新）
        ensureAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + wenxinAccessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", Collections.singletonList(
                Map.of("role", "user", "content", message)
        ));

        // 添加图像信息（如果存在）
        if (imageBase64 != null) {
            requestBody.put("image", "data:image/jpeg;base64," + imageBase64);
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                wenxinChatUrl, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("result");
        }

        throw new RuntimeException("文心一言API调用失败: " + response.getBody());
    }

    @Override
    public String speechToText(String voiceBase64) {
        // 百度语音识别API调用
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("format", "wav"); // 根据实际音频格式调整
        requestBody.put("rate", 16000);   // 采样率
        requestBody.put("channel", 1);    // 声道数
        requestBody.put("cuid", "green-app");
        requestBody.put("token", getBaiduSpeechToken());
        requestBody.put("speech", voiceBase64);
        requestBody.put("len", voiceBase64.length());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                speechAsrUrl, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<?> results = (List<?>) response.getBody().get("result");
            if (results != null && !results.isEmpty()) {
                return (String) results.get(0);
            }
        }

        throw new RuntimeException("语音识别失败: " + response.getBody());
    }

    @Override
    public String textToSpeech(String text) {
        // 百度语音合成API调用
        String token = getBaiduSpeechToken();
        String url = String.format("%s?tex=%s&tok=%s&cuid=%s&ctp=1&lan=zh&per=0",
                speechTtsUrl,
                encodeUrl(text),
                token,
                "green-app");

        byte[] audioData = restTemplate.getForObject(url, byte[].class);
        if (audioData != null) {
            return Base64.getEncoder().encodeToString(audioData);
        }
        return null;
    }

    @Override
    public String analyzeImage(String imageBase64) {
        // 文心一言图像理解API
        ensureAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + wenxinAccessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("image", "data:image/jpeg;base64," + imageBase64);
        requestBody.put("prompt", "请识别图中的物品并说明垃圾分类类型");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                wenxinImageUrl, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("result");
        }

        throw new RuntimeException("图像分析失败: " + response.getBody());
    }

    // 获取文心一言Access Token（自动刷新）
    private synchronized void ensureAccessToken() {
        if (wenxinAccessToken == null || System.currentTimeMillis() > tokenExpireTime) {
            String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials" +
                    "&client_id=" + wenxinApiKey +
                    "&client_secret=" + wenxinApiSecret;

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                wenxinAccessToken = (String) response.getBody().get("access_token");
                long expiresIn = Long.parseLong(response.getBody().get("expires_in").toString());
                tokenExpireTime = System.currentTimeMillis() + (expiresIn - 300) * 1000; // 提前5分钟刷新
            } else {
                throw new RuntimeException("获取文心一言Token失败");
            }
        }
    }

    // 获取百度语音Token
    private String getBaiduSpeechToken() {
        String url = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" +
                "&client_id=" + speechApiKey +
                "&client_secret=" + speechSecretKey;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }
        throw new RuntimeException("获取语音Token失败");
    }

    private String encodeUrl(String text) {
        try {
            return java.net.URLEncoder.encode(text, "UTF-8");
        } catch (Exception e) {
            return text;
        }
    }
}