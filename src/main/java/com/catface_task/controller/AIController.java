package com.catface_task.controller;

import com.catface_task.common.vo.request.AIRequest;
import com.catface_task.common.vo.response.AIResponse;
import com.catface_task.common.vo.response.ErrorResponse;
import com.catface_task.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    @Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> handleChat(@RequestBody AIRequest request) {
        try {
            // 处理语音识别（如果存在语音数据）
            if (request.getVoiceBase64() != null) {
                String recognizedText = aiService.speechToText(request.getVoiceBase64());
                request.setMessage(recognizedText);
            }

            // 调用文心一言API
            String aiReply = aiService.getWenxinResponse(
                    request.getMessage(),
                    request.getImageBase64()
            );

            // 构建响应
            AIResponse response = new AIResponse();
            response.setReplyText(aiReply);
            response.setVoiceBase64(aiService.textToSpeech(aiReply)); // 文本转语音
            response.setTimestamp(java.time.LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ErrorResponse(500, "AI服务处理失败: " + e.getMessage()));
        }
    }

    @PostMapping("/speech-recognition")
    public ResponseEntity<?> speechRecognition(@RequestBody AIRequest request) {
        try {
            if (request.getVoiceBase64() == null) {
                throw new IllegalArgumentException("缺少语音数据");
            }

            String recognizedText = aiService.speechToText(request.getVoiceBase64());
            return ResponseEntity.ok(Map.of("text", recognizedText));

        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, "语音识别失败: " + e.getMessage()));
        }
    }

    @PostMapping("/image-analysis")
    public ResponseEntity<?> imageAnalysis(@RequestBody AIRequest request) {
        try {
            if (request.getImageBase64() == null) {
                throw new IllegalArgumentException("缺少图像数据");
            }

            String analysisResult = aiService.analyzeImage(request.getImageBase64());
            return ResponseEntity.ok(Map.of("result", analysisResult));

        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, "图像分析失败: " + e.getMessage()));
        }
    }
}