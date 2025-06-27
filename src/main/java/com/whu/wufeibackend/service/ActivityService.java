package com.whu.wufeibackend.service;

import com.whu.wufeibackend.dto.ActivityListResponse;
import com.whu.wufeibackend.entity.Activity;
import com.whu.wufeibackend.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 活动服务类
 * 处理活动相关的业务逻辑
 */
@Service
public class ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 获取活动列表（根据用户ID）
     * 
     * @param userId 用户ID
     * @return 活动列表响应数据
     */
    public ActivityListResponse getActivityList(Integer userId) {
        try {
            // 1. 获取可参与的活动列表（status='published'且未结束）
            List<Activity> availableActivities = activityMapper.findAvailableActivities();

            // 2. 获取用户已报名的活动ID列表
            List<Integer> joinedActivityIds = activityMapper.findJoinedActivityIds(userId);

            // 3. 获取用户的历史活动列表（包含participated状态）
            List<ActivityListResponse.HistoryActivity> historyActivities = 
                activityMapper.findHistoryActivities(userId);

            // 4. 构建并返回响应数据
            return new ActivityListResponse(availableActivities, joinedActivityIds, historyActivities);

        } catch (Exception e) {
            throw new RuntimeException("获取活动列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据ID获取活动详情
     * 
     * @param activityId 活动ID
     * @return 活动信息
     */
    public Activity getActivityById(Integer activityId) {
        try {
            Activity activity = activityMapper.findById(activityId);
            if (activity == null) {
                throw new RuntimeException("活动不存在");
            }
            return activity;
        } catch (Exception e) {
            throw new RuntimeException("获取活动详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建新活动
     * 
     * @param activity 活动信息
     * @return 创建的活动信息
     */
    @Transactional
    public Activity createActivity(Activity activity) {
        try {
            // 参数验证
            if (activity.getTitle() == null || activity.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("活动标题不能为空");
            }
            if (activity.getOrganizerId() == null) {
                throw new IllegalArgumentException("组织者ID不能为空");
            }
            if (activity.getStartTime() == null || activity.getEndTime() == null) {
                throw new IllegalArgumentException("活动开始和结束时间不能为空");
            }
            if (activity.getLocation() == null || activity.getLocation().trim().isEmpty()) {
                throw new IllegalArgumentException("活动地点不能为空");
            }

            // 设置默认状态
            if (activity.getStatus() == null) {
                activity.setStatus("pending");
            }

            // 插入活动
            int result = activityMapper.insertActivity(activity);
            if (result <= 0) {
                throw new RuntimeException("创建活动失败");
            }

            return activity;
        } catch (Exception e) {
            throw new RuntimeException("创建活动失败: " + e.getMessage(), e);
        }
    }

    /**
     * 用户报名参与活动
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 是否报名成功
     */
    @Transactional
    public boolean joinActivity(Integer activityId, Integer userId) {
        try {
            // 1. 检查活动是否存在且可报名
            Activity activity = activityMapper.findById(activityId);
            if (activity == null) {
                throw new RuntimeException("活动不存在");
            }
            if (!"published".equals(activity.getStatus())) {
                throw new RuntimeException("活动未开放报名");
            }

            // 2. 检查是否达到最大参与人数
            if (activity.getMaxParticipants() != null) {
                int currentParticipants = activityMapper.countParticipants(activityId);
                if (currentParticipants >= activity.getMaxParticipants()) {
                    throw new RuntimeException("活动人数已满");
                }
            }

            // 3. 插入参与记录
            int result = activityMapper.insertParticipant(activityId, userId);
            return result > 0;

        } catch (Exception e) {
            throw new RuntimeException("报名活动失败: " + e.getMessage(), e);
        }
    }

    /**
     * 用户取消参与活动
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 是否取消成功
     */
    @Transactional
    public boolean cancelJoinActivity(Integer activityId, Integer userId) {
        try {
            int result = activityMapper.deleteParticipant(activityId, userId);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("取消报名失败: " + e.getMessage(), e);
        }
    }

    /**
     * 更新活动信息
     * 
     * @param activity 活动信息
     * @return 是否更新成功
     */
    @Transactional
    public boolean updateActivity(Activity activity) {
        try {
            // 参数验证
            if (activity.getId() == null) {
                throw new IllegalArgumentException("活动ID不能为空");
            }

            // 检查活动是否存在
            Activity existingActivity = activityMapper.findById(activity.getId());
            if (existingActivity == null) {
                throw new RuntimeException("活动不存在");
            }

            int result = activityMapper.updateActivity(activity);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("更新活动失败: " + e.getMessage(), e);
        }
    }

    /**
     * 确认用户参与活动（居委会操作）
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @param attended 是否参与
     * @return 是否操作成功
     */
    @Transactional
    public boolean confirmParticipation(Integer activityId, Integer userId, Boolean attended) {
        try {
            int result = activityMapper.updateParticipantAttended(activityId, userId, attended);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("确认参与状态失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取活动参与人数
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    public int getParticipantCount(Integer activityId) {
        try {
            return activityMapper.countParticipants(activityId);
        } catch (Exception e) {
            throw new RuntimeException("获取参与人数失败: " + e.getMessage(), e);
        }
    }
} 