package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.dto.ActivityListResponse;
import com.whu.wufeibackend.dto.ApiResponse;
import com.whu.wufeibackend.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        try {
            List<ActivityListResponse> activities = activityService.getOpenActivities();
            return ApiResponse.success("获取开放报名活动列表成功", activities);
        } catch (Exception e) {
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
        try {
            List<ActivityListResponse> activities = activityService.getInProgressActivities();
            return ApiResponse.success("获取进行中活动列表成功", activities);
        } catch (Exception e) {
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
        try {
            List<ActivityListResponse> activities = activityService.getFinishedActivities();
            return ApiResponse.success("获取已结束活动列表成功", activities);
        } catch (Exception e) {
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
        try {
            List<String> avatars = activityService.getRecentParticipantAvatars(activityId);
            return ApiResponse.success("获取参与者头像成功", avatars);
        } catch (Exception e) {
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
        try {
            Integer count = activityService.getParticipantCount(activityId);
            return ApiResponse.success("获取参与人数成功", count);
        } catch (Exception e) {
            return ApiResponse.error("获取参与人数失败: " + e.getMessage());
        }
    }
} 