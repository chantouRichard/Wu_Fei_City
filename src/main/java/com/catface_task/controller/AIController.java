package com.catface_task.controller;

import com.catface_task.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    @Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> handleChat(@RequestBody Map<String, String> request) {
        try {
            String imageUrl = request.get("imageUrl");
            String prompt = request.get("prompt");
            if (imageUrl == null || imageUrl.isEmpty() || prompt == null || prompt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "缺少imageUrl或prompt字段"));
            }
            String aiReply = aiService.getQwenVLResponse(imageUrl, prompt);
            return ResponseEntity.ok(Map.of("reply", aiReply));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "AI服务处理失败: " + e.getMessage()));
        }
    }
}