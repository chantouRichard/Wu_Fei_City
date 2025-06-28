package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.dto.ActivityListResponse;
import com.whu.wufeibackend.dto.ApiResponse;
import com.whu.wufeibackend.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动管理控制器
 * 
 * @author 无废技术组
 * @since 2024-06-27
 */
@RestController
@RequestMapping("/api/activities")
@Tag(name = "活动管理", description = "社区活动相关接口")
@CrossOrigin(origins = "*")
public class ActivityController {

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityService activityService;

    /**
     * 获取开放报名的活动列表
     * GET /api/activities/open
     * 逻辑：start_time < NOW() AND end_time > NOW() AND status != 'finished'
     * 
     * @return 开放报名的活动列表
     */
    @GetMapping("/open")
    @Operation(summary = "获取开放报名的活动列表", description = "获取当前可以报名参与的活动列表")
    public ApiResponse<List<ActivityListResponse>> getOpenActivities() {
        logger.info("=== 开始处理获取开放报名活动列表请求 ===");
        long startTime = System.currentTimeMillis();
        
        try {
            logger.debug("调用ActivityService.getOpenActivities()方法");
            List<ActivityListResponse> activities = activityService.getOpenActivities();
            
            long endTime = System.currentTimeMillis();
            logger.info("成功获取开放报名活动列表，数量: {}, 耗时: {}ms", 
                       activities != null ? activities.size() : 0, 
                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                logger.debug("开放报名活动详情: {}", activities);
            } else {
                logger.warn("当前没有开放报名的活动");
            }
            
            return ApiResponse.success("获取开放报名活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("获取开放报名活动列表失败，耗时: {}ms，错误信息: {}", 
                        endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取开放报名活动列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取进行中的活动列表
     * GET /api/activities/inprogress
     * 逻辑：end_time < NOW() AND status != 'finished'
     * 
     * @return 进行中的活动列表
     */
    @GetMapping("/inprogress")
    @Operation(summary = "获取进行中的活动列表", description = "获取报名已结束但活动尚未完成的活动列表")
    public ApiResponse<List<ActivityListResponse>> getInProgressActivities() {
        logger.info("=== 开始处理获取进行中活动列表请求 ===");
        long startTime = System.currentTimeMillis();
        
        try {
            logger.debug("调用ActivityService.getInProgressActivities()方法");
            List<ActivityListResponse> activities = activityService.getInProgressActivities();
            
            long endTime = System.currentTimeMillis();
            logger.info("成功获取进行中活动列表，数量: {}, 耗时: {}ms", 
                       activities != null ? activities.size() : 0, 
                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                logger.debug("进行中活动详情: {}", activities);
            } else {
                logger.warn("当前没有进行中的活动");
            }
            
            return ApiResponse.success("获取进行中活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("获取进行中活动列表失败，耗时: {}ms，错误信息: {}", 
                        endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取进行中活动列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取已结束的活动列表
     * GET /api/activities/finished
     * 逻辑：status == 'finished'
     * 
     * @return 已结束的活动列表
     */
    @GetMapping("/finished")
    @Operation(summary = "获取已结束的活动列表", description = "获取已经完成的活动列表")
    public ApiResponse<List<ActivityListResponse>> getFinishedActivities() {
        logger.info("=== 开始处理获取已结束活动列表请求 ===");
        long startTime = System.currentTimeMillis();
        
        try {
            logger.debug("调用ActivityService.getFinishedActivities()方法");
            List<ActivityListResponse> activities = activityService.getFinishedActivities();
            
            long endTime = System.currentTimeMillis();
            logger.info("成功获取已结束活动列表，数量: {}, 耗时: {}ms", 
                       activities != null ? activities.size() : 0, 
                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                logger.debug("已结束活动详情: {}", activities);
            } else {
                logger.warn("当前没有已结束的活动");
            }
            
            return ApiResponse.success("获取已结束活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("获取已结束活动列表失败，耗时: {}ms，错误信息: {}", 
                        endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取已结束活动列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取活动参与者头像列表
     * GET /api/activities/{activityId}/avatars
     * 
     * @param activityId 活动ID
     * @return 最近报名的三位用户头像
     */
    @GetMapping("/{activityId}/avatars")
    @Operation(summary = "获取活动参与者头像", description = "获取指定活动最近报名的三位用户头像")
    public ApiResponse<List<String>> getParticipantAvatars(@PathVariable Integer activityId) {
        logger.info("=== 开始处理获取活动参与者头像请求，活动ID: {} ===", activityId);
        long startTime = System.currentTimeMillis();
        
        try {
            logger.debug("调用ActivityService.getRecentParticipantAvatars()方法，活动ID: {}", activityId);
            List<String> avatars = activityService.getRecentParticipantAvatars(activityId);
            
            long endTime = System.currentTimeMillis();
            logger.info("成功获取活动{}的参与者头像，数量: {}, 耗时: {}ms", 
                       activityId, 
                       avatars != null ? avatars.size() : 0, 
                       endTime - startTime);
            
            return ApiResponse.success("获取参与者头像成功", avatars);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("获取活动{}的参与者头像失败，耗时: {}ms，错误信息: {}", 
                        activityId, endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取参与者头像失败: " + e.getMessage());
        }
    }

    /**
     * 获取活动参与人数
     * GET /api/activities/{activityId}/count
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    @GetMapping("/{activityId}/count")
    @Operation(summary = "获取活动参与人数", description = "获取指定活动的参与人数")
    public ApiResponse<Integer> getParticipantCount(@PathVariable Integer activityId) {
        logger.info("=== 开始处理获取活动参与人数请求，活动ID: {} ===", activityId);
        long startTime = System.currentTimeMillis();
        
        try {
            logger.debug("调用ActivityService.getParticipantCount()方法，活动ID: {}", activityId);
            Integer count = activityService.getParticipantCount(activityId);
            
            long endTime = System.currentTimeMillis();
            logger.info("成功获取活动{}的参与人数: {}, 耗时: {}ms", 
                       activityId, count, endTime - startTime);
            
            return ApiResponse.success("获取参与人数成功", count);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("获取活动{}的参与人数失败，耗时: {}ms，错误信息: {}", 
                        activityId, endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取参与人数失败: " + e.getMessage());
        }
    }
} 