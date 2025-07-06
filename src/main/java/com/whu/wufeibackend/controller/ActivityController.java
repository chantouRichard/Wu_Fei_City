package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.DTO.*;
import com.whu.wufeibackend.service.ActivityService;
import com.whu.wufeibackend.service.TimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 活动管理控制器
 * 
 * 时间逻辑说明：
 * - createdAtAndSignupStartTime: 既是活动创建时间也是报名开始时间
 * - signupEndTimeAndActivityStartTime: 既是报名结束时间也是活动开始时间
 * - activityEndTime: 活动结束时间
 * 
 * 活动状态判断逻辑：
 * - open: created_at_and_signup_start_time < NOW() AND signup_end_time_and_activity_start_time > NOW()
 * - inprogress: signup_end_time_and_activity_start_time < NOW() AND activity_end_time > NOW()
 * - finished: activity_end_time < NOW() OR status = 'finished'
 * 
 * @author 无废技术组
 * @since 2024-06-27
 * @updated 2025-01-27
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
    
    @Autowired
    private TimeService timeService;

    /**
     * 获取开放报名的活动列表
     * GET /api/activities/open
     * 逻辑：created_at_and_signup_start_time < NOW() AND signup_end_time_and_activity_start_time > NOW() AND status != 'finished'
     * 
     * 返回字段包含：
     * - createdAtAndSignupStartTime: 活动创建时间/报名开始时间
     * - signupEndTimeAndActivityStartTime: 报名结束时间/活动开始时间
     * - activityEndTime: 活动结束时间
     * 
     * @return 居委会开放报名的活动列表
     */
    @PostMapping("/open")
    @Operation(summary = "获取开放报名的活动列表", description = "获取当前可以报名参与的活动列表")
    public ApiResponse<List<ActivityListResponse>> getOpenActivities(@RequestBody Map<String, Object> requestBody) {
        Integer userId = (Integer) requestBody.get("userId");
        try {
            List<ActivityListResponse> activities = activityService.getOpenActivities(userId);

            if (activities != null && !activities.isEmpty()) {
                // // logger.debug("开放报名活动详情: {}", activities);
            } else {
                // logger.warn("当前没有开放报名的活动");
            }
            
            return ApiResponse.success("获取开放报名活动列表成功", activities);
            
        } catch (Exception e) {
            return ApiResponse.error("获取开放报名活动列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取进行中的活动列表
     * GET /api/activities/inprogress
     * 逻辑：signup_end_time_and_activity_start_time < NOW() AND activity_end_time > NOW() AND status != 'finished'
     * 
     * 返回字段包含：
     * - createdAtAndSignupStartTime: 活动创建时间/报名开始时间
     * - signupEndTimeAndActivityStartTime: 报名结束时间/活动开始时间
     * - activityEndTime: 活动结束时间
     * 
     * @return 居委会进行中的活动列表
     */
    @PostMapping("/inprogress")
    @Operation(summary = "获取进行中的活动列表", description = "获取报名已结束但活动尚未完成的活动列表")
    public ApiResponse<List<ActivityListResponse>> getInProgressActivities(@RequestBody Map<String, Object> requestBody) {
        // logger.info("=== 开始处理获取进行中活动列表请求 ===");
        long startTime = System.currentTimeMillis();
        Integer userId = (Integer) requestBody.get("userId");

        try {
            // // logger.debug("调用ActivityService.getInProgressActivities()方法");
            List<ActivityListResponse> activities = activityService.getInProgressActivities(userId);
            
            long endTime = System.currentTimeMillis();
            // logger.info("成功获取进行中活动列表，数量: {}, 耗时: {}ms", 
//                       activities != null ? activities.size() : 0,
//                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                // // logger.debug("进行中活动详情: {}", activities);
            } else {
                // logger.warn("当前没有进行中的活动");
            }
            
            return ApiResponse.success("获取进行中活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("获取进行中活动列表失败，耗时: {}ms，错误信息: {}", 
//                        endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取进行中活动列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取已结束的活动列表
     * GET /api/activities/finished
     * 逻辑：activity_end_time < NOW() OR status = 'finished'
     * 
     * 返回字段包含：
     * - createdAtAndSignupStartTime: 活动创建时间/报名开始时间
     * - signupEndTimeAndActivityStartTime: 报名结束时间/活动开始时间
     * - activityEndTime: 活动结束时间
     * 
     * @return 已结束的活动列表
     */
    @PostMapping("/finished")
    @Operation(summary = "获取已结束的活动列表", description = "获取已经完成的活动列表")
    public ApiResponse<List<ActivityListResponse>> getFinishedActivities(@RequestBody Map<String, Object> requestBody) {
        // logger.info("=== 开始处理获取已结束活动列表请求 ===");
        long startTime = System.currentTimeMillis();
        Integer userId = (Integer) requestBody.get("userId");

        try {
            // // logger.debug("调用ActivityService.getFinishedActivities()方法");
            List<ActivityListResponse> activities = activityService.getFinishedActivities(userId);
            
            long endTime = System.currentTimeMillis();
            // logger.info("成功获取已结束活动列表，数量: {}, 耗时: {}ms", 
//                       activities != null ? activities.size() : 0,
//                       endTime - startTime);
            
            if (activities != null && !activities.isEmpty()) {
                // // logger.debug("已结束活动详情: {}", activities);
            } else {
                // logger.warn("当前没有已结束的活动");
            }
            
            return ApiResponse.success("获取已结束活动列表成功", activities);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("获取已结束活动列表失败，耗时: {}ms，错误信息: {}", 
//                        endTime - startTime, e.getMessage(), e);
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
        // logger.info("=== 开始处理获取活动参与者头像请求，活动ID: {} ===", activityId);
        long startTime = System.currentTimeMillis();
        
        try {
            // // logger.debug("调用ActivityService.getRecentParticipantAvatars()方法，活动ID: {}", activityId);
            List<String> avatars = activityService.getRecentParticipantAvatars(activityId);
            
            long endTime = System.currentTimeMillis();
            // logger.info("成功获取活动{}的参与者头像，数量: {}, 耗时: {}ms", 
//                       activityId,
//                       avatars != null ? avatars.size() : 0,
//                       endTime - startTime);
            
            return ApiResponse.success("获取参与者头像成功", avatars);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("获取活动{}的参与者头像失败，耗时: {}ms，错误信息: {}", 
//                        activityId, endTime - startTime, e.getMessage(), e);
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
        // logger.info("=== 开始处理获取活动参与人数请求，活动ID: {} ===", activityId);
        long startTime = System.currentTimeMillis();
        
        try {
            // // logger.debug("调用ActivityService.getParticipantCount()方法，活动ID: {}", activityId);
            Integer count = activityService.getParticipantCount(activityId);
            
            long endTime = System.currentTimeMillis();
            // logger.info("成功获取活动{}的参与人数: {}, 耗时: {}ms", 
//                       activityId, count, endTime - startTime);
            
            return ApiResponse.success("获取参与人数成功", count);
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("获取活动{}的参与人数失败，耗时: {}ms，错误信息: {}", 
//                        activityId, endTime - startTime, e.getMessage(), e);
            return ApiResponse.error("获取参与人数失败: " + e.getMessage());
        }
    }

    /**
     * 创建新活动
     * POST /api/activities/create
     * 
     * 验证规则：
     * - title: 长度至少2个字符
     * - 时间逻辑: created_at_and_signup_start_time < signup_end_time_and_activity_start_time < activity_end_time
     * - location: 非空
     * 
     * 字段说明：
     * - created_at_and_signup_start_time: 活动创建时间（即报名开始时间），由服务器自动设置为当前时间
     * - signup_end_time_and_activity_start_time: 报名结束时间（即活动开始时间）
     * - activity_end_time: 活动结束时间
     * 
     * @param request 创建活动请求体
     * @return 创建的活动信息
     */
    @PostMapping("/create")
    @Operation(summary = "创建新活动", description = "居委会用户创建新的社区活动")
    public ResponseEntity<ApiResponse<CreateActivityResponse>> createActivity(
            @Valid @RequestBody CreateActivityRequest request) {
        
         logger.info("=== 开始处理创建活动请求，请求: {} ===", request);
        long startTime = System.currentTimeMillis();
        
        try {
            // 验证请求参数
            // // logger.debug("开始验证请求参数");
            
            // 验证标题长度
            if (request.getTitle() == null || request.getTitle().trim().length() < 2) {
                // logger.warn("活动标题验证失败：标题长度小于2个字符");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "活动标题长度至少为2个字符"));
            }
            
            // 验证地点非空
            if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
                // logger.warn("活动地点验证失败：地点不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "活动地点不能为空"));
            }
            
            // 验证时间逻辑（使用时间服务）
            LocalDateTime currentTime = timeService.now();
            if (!request.isTimeSequenceValid(currentTime)) {
                // logger.warn("时间逻辑验证失败：当前时间: {}, 活动开始时间: {}, 活动结束时间: {}, 时间服务信息: {}", 
//                           currentTime,
//                           request.getSignupEndTimeAndActivityStartTime(),
//                           request.getActivityEndTime(),
//                           timeService.getTimeInfo());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "时间设置不正确：活动开始时间必须晚于当前时间，活动结束时间必须晚于活动开始时间"));
            }
            
            // // logger.debug("请求参数验证通过");
            
            // 目前暂时使用默认的居委会用户ID，实际应用中需要从认证信息中获取
            Long organizerId = Long.valueOf(request.getOrganizerId());
            
            // // logger.debug("调用ActivityService.createActivity()方法，组织者ID: {}", organizerId);
            CreateActivityResponse response = activityService.createActivity(request, organizerId);
            
            long endTime = System.currentTimeMillis();
            
            if (response != null) {
                // logger.info("成功创建活动，ID: {}, 标题: {}, 耗时: {}ms", 
//                           response.getActivityId(), response.getTitle(), endTime - startTime);
                
                return ResponseEntity.ok(ApiResponse.success("活动创建成功", response));
            } else {
                // logger.error("创建活动失败：服务返回null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "活动创建失败"));
            }
            
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            // logger.error("创建活动时发生异常，耗时: {}ms，错误信息: {}", 
//                        endTime - startTime, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "服务器内部错误: " + e.getMessage()));
        }
    }

    @GetMapping("/{activityId}/participants")
    public ResponseEntity<?> getActivityParticipants(
            @PathVariable Integer activityId) {

        try {
            List<MakeSureUser> participants = activityService.makeSureActivity(activityId);
            if (participants == null || participants.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{activityId}/confirmAttendance")
    @Operation(summary = "确认活动", description = "居委会确认活动")
    public ResponseEntity<ApiResponse<Integer>> confirmUserAttendance(@PathVariable Integer activityId, @RequestBody List<Integer> userIds){
        return ResponseEntity.ok(ApiResponse.success(activityService.confirmAttendance(activityId, userIds)));
    }
}