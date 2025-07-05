package com.whu.wufeibackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间服务类
 * 支持测试模式下的时间模拟
 * 
 * @author Wu Fei City Team
 * @since 2025-01-27
 */
@Service
public class TimeService {

    private static final Logger logger = LoggerFactory.getLogger(TimeService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * 是否启用测试模式
     */
    @Value("${app.test-mode.enabled:false}")
    private boolean testModeEnabled;

    /**
     * 模拟的当前时间（测试模式下使用）
     */
    @Value("${app.test-mode.mock-current-time:}")
    private String mockCurrentTimeStr;

    /**
     * 获取当前时间
     * 如果启用测试模式且配置了模拟时间，则返回模拟时间
     * 否则返回真实的当前时间
     * 
     * @return 当前时间
     */
    public LocalDateTime now() {
        if (testModeEnabled && mockCurrentTimeStr != null && !mockCurrentTimeStr.trim().isEmpty()) {
            try {
                LocalDateTime mockTime = LocalDateTime.parse(mockCurrentTimeStr.trim(), FORMATTER);
                // // logger.debug("使用模拟时间: {}", mockTime);
                return mockTime;
            } catch (Exception e) {
                // logger.warn("解析模拟时间失败: {}, 使用真实时间", mockCurrentTimeStr, e);
                return LocalDateTime.now();
            }
        } else {
            LocalDateTime realTime = LocalDateTime.now();
            if (testModeEnabled) {
                // // logger.debug("测试模式已启用但未配置模拟时间，使用真实时间: {}", realTime);
            }
            return realTime;
        }
    }

    /**
     * 检查是否为测试模式
     * 
     * @return 是否为测试模式
     */
    public boolean isTestMode() {
        return testModeEnabled;
    }

    /**
     * 获取模拟时间字符串
     * 
     * @return 模拟时间字符串
     */
    public String getMockCurrentTimeStr() {
        return mockCurrentTimeStr;
    }

    /**
     * 设置模拟时间（运行时动态设置）
     * 仅在测试模式下有效
     * 
     * @param mockTime 模拟时间
     */
    public void setMockTime(LocalDateTime mockTime) {
        if (testModeEnabled) {
            this.mockCurrentTimeStr = mockTime.format(FORMATTER);
            // logger.info("动态设置模拟时间: {}", mockTime);
        } else {
            // logger.warn("非测试模式下无法设置模拟时间");
        }
    }

    /**
     * 设置模拟时间（运行时动态设置）
     * 仅在测试模式下有效
     * 
     * @param mockTimeStr 模拟时间字符串 (yyyy-MM-dd'T'HH:mm:ss格式)
     */
    public void setMockTime(String mockTimeStr) {
        if (testModeEnabled) {
            try {
                LocalDateTime.parse(mockTimeStr, FORMATTER); // 验证格式
                this.mockCurrentTimeStr = mockTimeStr;
                // logger.info("动态设置模拟时间: {}", mockTimeStr);
            } catch (Exception e) {
                // logger.error("设置模拟时间失败，格式错误: {}", mockTimeStr, e);
                throw new IllegalArgumentException("模拟时间格式错误，应为: yyyy-MM-dd'T'HH:mm:ss");
            }
        } else {
            // logger.warn("非测试模式下无法设置模拟时间");
        }
    }

    /**
     * 重置为真实时间
     */
    public void resetToRealTime() {
        if (testModeEnabled) {
            this.mockCurrentTimeStr = "";
            // logger.info("已重置为真实时间模式");
        }
    }

    /**
     * 获取时间信息（用于调试）
     * 
     * @return 时间信息字符串
     */
    public String getTimeInfo() {
        if (testModeEnabled) {
            return String.format("测试模式: %s, 模拟时间: %s, 当前返回时间: %s", 
                               testModeEnabled, 
                               mockCurrentTimeStr.isEmpty() ? "未设置" : mockCurrentTimeStr, 
                               now());
        } else {
            return String.format("生产模式, 当前时间: %s", now());
        }
    }
} 