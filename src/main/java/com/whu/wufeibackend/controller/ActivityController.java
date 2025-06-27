package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.dto.ApiResponse;
import com.whu.wufeibackend.dto.ActivityListResponse;
import com.whu.wufeibackend.entity.Activity;
import com.whu.wufeibackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 活动控制器
 * 处理活动相关的API请求
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获取活动列表
     * GET /api/activities?user_id=123
     * 
     * @param userId 用户ID
     * @return 活动列表响应
     */
    @GetMapping("/activities")
    public ApiResponse<ActivityListResponse> getActivities(@RequestParam("user_id") Integer userId) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return ApiResponse.error(400, "用户ID不能为空且必须为正整数");
            }

            // 获取活动列表
            ActivityListResponse activityList = activityService.getActivityList(userId);
            
            return ApiResponse.success("获取活动列表成功", activityList);

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取活动详情
     * GET /api/activities/{id}
     * 
     * @param activityId 活动ID
     * @return 活动详情
     */
    @GetMapping("/activities/{id}")
    public ApiResponse<Activity> getActivityById(@PathVariable("id") Integer activityId) {
        try {
            // 参数验证
            if (activityId == null || activityId <= 0) {
                return ApiResponse.error(400, "活动ID不能为空且必须为正整数");
            }

            // 获取活动详情
            Activity activity = activityService.getActivityById(activityId);
            
            return ApiResponse.success("获取活动详情成功", activity);

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(500, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 创建新活动（居委会用户）
     * POST /api/activities
     * 
     * @param activity 活动信息
     * @return 创建结果
     */
    @PostMapping("/activities")
    public ApiResponse<Activity> createActivity(@RequestBody Activity activity) {
        try {
            // 创建活动
            Activity createdActivity = activityService.createActivity(activity);
            
            return ApiResponse.success("创建活动成功", createdActivity);

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 用户报名参与活动
     * POST /api/activities/{id}/join
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 报名结果
     */
    @PostMapping("/activities/{id}/join")
    public ApiResponse<String> joinActivity(@PathVariable("id") Integer activityId, 
                                          @RequestParam("user_id") Integer userId) {
        try {
            // 参数验证
            if (activityId == null || activityId <= 0) {
                return ApiResponse.error(400, "活动ID不能为空且必须为正整数");
            }
            if (userId == null || userId <= 0) {
                return ApiResponse.error(400, "用户ID不能为空且必须为正整数");
            }

            // 用户报名参与活动
            boolean success = activityService.joinActivity(activityId, userId);
            
            if (success) {
                return ApiResponse.success("报名成功", "已成功报名参与活动");
            } else {
                return ApiResponse.error(500, "报名失败");
            }

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            } else if (e.getMessage().contains("未开放") || e.getMessage().contains("已满")) {
                return ApiResponse.error(403, e.getMessage());
            }
            return ApiResponse.error(500, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 用户取消参与活动
     * DELETE /api/activities/{id}/join
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 取消结果
     */
    @DeleteMapping("/activities/{id}/join")
    public ApiResponse<String> cancelJoinActivity(@PathVariable("id") Integer activityId, 
                                                @RequestParam("user_id") Integer userId) {
        try {
            // 参数验证
            if (activityId == null || activityId <= 0) {
                return ApiResponse.error(400, "活动ID不能为空且必须为正整数");
            }
            if (userId == null || userId <= 0) {
                return ApiResponse.error(400, "用户ID不能为空且必须为正整数");
            }

            // 取消报名
            boolean success = activityService.cancelJoinActivity(activityId, userId);
            
            if (success) {
                return ApiResponse.success("取消报名成功", "已成功取消活动报名");
            } else {
                return ApiResponse.error(500, "取消报名失败");
            }

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 更新活动信息（居委会用户）
     * PUT /api/activities/{id}
     * 
     * @param activityId 活动ID
     * @param activity 活动信息
     * @return 更新结果
     */
    @PutMapping("/activities/{id}")
    public ApiResponse<String> updateActivity(@PathVariable("id") Integer activityId, 
                                            @RequestBody Activity activity) {
        try {
            // 设置活动ID
            activity.setId(activityId);

            // 更新活动信息
            boolean success = activityService.updateActivity(activity);
            
            if (success) {
                return ApiResponse.success("更新活动成功", "活动信息已更新");
            } else {
                return ApiResponse.error(500, "更新活动失败");
            }

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(500, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 确认用户参与活动（居委会操作）
     * PUT /api/activities/{activityId}/participants/{userId}/attendance
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @param attended 是否参与
     * @return 确认结果
     */
    @PutMapping("/activities/{activityId}/participants/{userId}/attendance")
    public ApiResponse<String> confirmParticipation(@PathVariable("activityId") Integer activityId,
                                                   @PathVariable("userId") Integer userId,
                                                   @RequestParam("attended") Boolean attended) {
        try {
            // 参数验证
            if (activityId == null || activityId <= 0) {
                return ApiResponse.error(400, "活动ID不能为空且必须为正整数");
            }
            if (userId == null || userId <= 0) {
                return ApiResponse.error(400, "用户ID不能为空且必须为正整数");
            }
            if (attended == null) {
                return ApiResponse.error(400, "参与状态不能为空");
            }

            // 确认参与状态
            boolean success = activityService.confirmParticipation(activityId, userId, attended);
            
            if (success) {
                String message = attended ? "确认用户已参与活动" : "确认用户未参与活动";
                return ApiResponse.success("操作成功", message);
            } else {
                return ApiResponse.error(500, "操作失败");
            }

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 获取活动参与人数
     * GET /api/activities/{id}/participants/count
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    @GetMapping("/activities/{id}/participants/count")
    public ApiResponse<Integer> getParticipantCount(@PathVariable("id") Integer activityId) {
        try {
            // 参数验证
            if (activityId == null || activityId <= 0) {
                return ApiResponse.error(400, "活动ID不能为空且必须为正整数");
            }

            // 获取参与人数
            int count = activityService.getParticipantCount(activityId);
            
            return ApiResponse.success("获取参与人数成功", count);

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }
} 