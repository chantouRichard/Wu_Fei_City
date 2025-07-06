package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.DTO.ActivityListResponse;
import com.whu.wufeibackend.DTO.ApiResponse;
import com.whu.wufeibackend.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户活动控制器
 * 为普通用户提供活动查询功能
 * 
 * 接口说明：
 * - joined: 用户已报名的活动（attended=0 且 status≠finished）
 * - available: 用户可报名的活动（signup_end_time_and_activity_start_time > NOW() 且未满员且用户未报名）
 * - history: 用户历史参与过的活动（attended=1）
 * 
 * @author 无废技术组
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户活动管理", description = "普通用户活动相关接口")
@CrossOrigin(origins = "*")
public class UserActivityController {

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(UserActivityController.class);

    @Autowired
    private UserActivityService userActivityService;

    /**
     * 获取用户已报名的活动列表
     * GET /api/user/{userId}/activities/joined
     * 
     * 业务逻辑：
     * - attended = 0（表示报名未参与）
     * - 活动状态 ≠ finished（表示活动未结束）
     * - 活动状态 = published（已发布）
     * 
     * @param userId 用户ID
     * @return 用户已报名的活动列表
     */
    @GetMapping("/{userId}/activities/joined")
    @Operation(summary = "获取用户已报名的活动", description = "获取用户已报名但尚未参与的活动列表")
    public ApiResponse<List<ActivityListResponse>> getJoinedActivities(@PathVariable Integer userId) {
        // logger.info("=== 开始处理获取用户{}已报名活动列表请求 ===", userId);
        long startTime = System.currentTimeMillis();
        
        try {
            // // logger.debug("调用UserActivityService.getJoinedActivities()方法，用户ID: {}", userId);
            List<ActivityListResponse> activities = userActivityService.getJoinedActivities(userId);
            
            long endTime = System.currentTimeMillis();
            // logger.info("成功获取用户{}已报名活动列表，数量: {}, 耗时: {}ms", 
//                       userId,
//                       activities != null ? activities.size() : 0,
//                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                // // logger.debug("用户{}已报名活动详情: {}", userId, activities);
            } else {
                // logger.warn("用户{}当前没有已报名的活动", userId);
            }
            
            return ApiResponse.success("获取已报名活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("获取用户{}已报名活动列表失败，耗时: {}ms，错误信息: {}", 
//                        userId, endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取已报名活动列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户可报名的活动列表
     * GET /api/user/{userId}/activities/available
     * 
     * 业务逻辑：
     * - signup_end_time_and_activity_start_time > NOW()（还可以报名）
     * - 活动状态 = published（已发布）
     * - 活动未满员（participantCount < maxParticipants 或 maxParticipants为NULL）
     * - 用户未报名该活动
     * 
     * @param userId 用户ID
     * @return 用户可报名的活动列表
     */
    @GetMapping("/{userId}/activities/available")
    @Operation(summary = "获取用户可报名的活动", description = "获取用户当前可以报名参与的活动列表")
    public ApiResponse<List<ActivityListResponse>> getAvailableActivities(@PathVariable Integer userId) {
        // logger.info("=== 开始处理获取用户{}可报名活动列表请求 ===", userId);
        long startTime = System.currentTimeMillis();
        
        try {
            // // logger.debug("调用UserActivityService.getAvailableActivities()方法，用户ID: {}", userId);
            List<ActivityListResponse> activities = userActivityService.getAvailableActivities(userId);
            
            long endTime = System.currentTimeMillis();
            // logger.info("成功获取用户{}可报名活动列表，数量: {}, 耗时: {}ms", 
//                       userId,
//                       activities != null ? activities.size() : 0,
//                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                // // logger.debug("用户{}可报名活动详情: {}", userId, activities);
            } else {
                // logger.warn("用户{}当前没有可报名的活动", userId);
            }
            
            return ApiResponse.success("获取可报名活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("获取用户{}可报名活动列表失败，耗时: {}ms，错误信息: {}", 
//                        userId, endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取可报名活动列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户历史参与过的活动列表
     * GET /api/user/{userId}/activities/history
     * 
     * 业务逻辑：
     * - attended = 1（表示已经实际参与完成）
     * 
     * @param userId 用户ID
     * @return 用户历史参与过的活动列表
     */
    @GetMapping("/{userId}/activities/history")
    @Operation(summary = "获取用户历史参与的活动", description = "获取用户已实际参与完成的活动列表")
    public ApiResponse<List<ActivityListResponse>> getHistoryActivities(@PathVariable Integer userId) {
        // logger.info("=== 开始处理获取用户{}历史参与活动列表请求 ===", userId);
        long startTime = System.currentTimeMillis();
        
        try {
            // // logger.debug("调用UserActivityService.getHistoryActivities()方法，用户ID: {}", userId);
            List<ActivityListResponse> activities = userActivityService.getHistoryActivities(userId);
            
            long endTime = System.currentTimeMillis();
            // logger.info("成功获取用户{}历史参与活动列表，数量: {}, 耗时: {}ms", 
//                       userId,
//                       activities != null ? activities.size() : 0,
//                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                // // logger.debug("用户{}历史参与活动详情: {}", userId, activities);
            } else {
                // logger.warn("用户{}当前没有历史参与的活动", userId);
            }
            
            return ApiResponse.success("获取历史参与活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("获取用户{}历史参与活动列表失败，耗时: {}ms，错误信息: {}", 
//                        userId, endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取历史参与活动列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/activities")
    @Operation(summary = "用户报名参加活动", description = "用户报名")
    public ApiResponse<?> joinActivity(@PathVariable Integer userId, @RequestBody Integer activityId){
        return ApiResponse.success("参加成功" ,userActivityService.joinActivity(activityId, userId));
    }

    @DeleteMapping("/{userId}/activities")
    public ApiResponse<?> cancelActivity(@PathVariable Integer userId, @RequestBody Integer activityId){
        return ApiResponse.success("取消成功" ,userActivityService.cancelActivity(activityId, userId));
    }
} 