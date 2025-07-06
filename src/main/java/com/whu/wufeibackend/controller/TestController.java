package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.DTO.ApiResponse;
import com.whu.wufeibackend.service.TimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 测试控制器
 * 仅在测试模式下生效，用于动态控制系统时间
 * 
 * @author Wu Fei City Team
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "测试工具", description = "测试模式下的辅助工具接口")
@CrossOrigin(origins = "*")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TimeService timeService;

    /**
     * 获取当前时间信息
     * GET /api/test/time
     * 
     * @return 时间信息
     */
    @GetMapping("/time")
    @Operation(summary = "获取时间信息", description = "获取当前的时间服务状态和时间信息")
    public ApiResponse<String> getTimeInfo() {
        // logger.info("获取时间信息请求");
        
        String timeInfo = timeService.getTimeInfo();
        // logger.info("时间信息: {}", timeInfo);
        
        return ApiResponse.success("获取时间信息成功", timeInfo);
    }

    /**
     * 设置模拟时间
     * POST /api/test/time/mock
     * 
     * @param mockTime 模拟时间 (yyyy-MM-dd'T'HH:mm:ss格式)
     * @return 设置结果
     */
    @PostMapping("/time/mock")
    @Operation(summary = "设置模拟时间", description = "在测试模式下设置模拟的当前时间")
    public ApiResponse<String> setMockTime(@RequestParam String mockTime) {
        // logger.info("设置模拟时间请求: {}", mockTime);
        
        if (!timeService.isTestMode()) {
            // logger.warn("非测试模式，无法设置模拟时间");
            return ApiResponse.error("非测试模式，无法设置模拟时间");
        }
        
        try {
            timeService.setMockTime(mockTime);
            String newTimeInfo = timeService.getTimeInfo();
            // logger.info("模拟时间设置成功: {}", newTimeInfo);
            
            return ApiResponse.success("模拟时间设置成功", newTimeInfo);
        } catch (Exception e) {
            // logger.error("设置模拟时间失败: {}", e.getMessage());
            return ApiResponse.error("设置模拟时间失败: " + e.getMessage());
        }
    }

    /**
     * 重置为真实时间
     * DELETE /api/test/time/mock
     * 
     * @return 重置结果
     */
    @DeleteMapping("/time/mock")
    @Operation(summary = "重置为真实时间", description = "重置时间服务，使用真实的系统时间")
    public ApiResponse<String> resetToRealTime() {
        // logger.info("重置为真实时间请求");
        
        if (!timeService.isTestMode()) {
            // logger.warn("非测试模式，无需重置");
            return ApiResponse.error("非测试模式，无需重置");
        }
        
        timeService.resetToRealTime();
        String timeInfo = timeService.getTimeInfo();
        // logger.info("已重置为真实时间: {}", timeInfo);
        
        return ApiResponse.success("已重置为真实时间", timeInfo);
    }

    /**
     * 快速设置测试时间（预设时间点）
     * POST /api/test/time/preset
     * 
     * @param preset 预设时间类型 (past/current/future)
     * @return 设置结果
     */
    @PostMapping("/time/preset")
    @Operation(summary = "快速设置预设时间", description = "使用预定义的时间点进行测试")
    public ApiResponse<String> setPresetTime(@RequestParam String preset) {
        // logger.info("设置预设时间请求: {}", preset);
        
        if (!timeService.isTestMode()) {
            return ApiResponse.error("非测试模式，无法设置模拟时间");
        }
        
        String mockTime;
        switch (preset.toLowerCase()) {
            case "past":
                mockTime = "2024-01-01T10:00:00";
                break;
            case "current":
                mockTime = "2024-12-01T10:00:00";
                break;
            case "future":
                mockTime = "2026-01-01T10:00:00";
                break;
            default:
                return ApiResponse.error("无效的预设时间类型。支持: past, current, future");
        }
        
        try {
            timeService.setMockTime(mockTime);
            String timeInfo = timeService.getTimeInfo();
            // logger.info("预设时间设置成功: {} -> {}", preset, timeInfo);
            
            return ApiResponse.success("预设时间设置成功", timeInfo);
        } catch (Exception e) {
            // logger.error("设置预设时间失败: {}", e.getMessage());
            return ApiResponse.error("设置预设时间失败: " + e.getMessage());
        }
    }
} 