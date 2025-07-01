package com.whu.wufeibackend.mapper;

import com.whu.wufeibackend.DTO.ActivityListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户活动数据访问层接口
 * 
 * @author 无废技术组
 * @since 2025-01-27
 */
@Mapper
public interface UserActivityMapper {

    /**
     * 获取用户已报名的活动列表
     * 业务逻辑：attended = 0 且 status ≠ finished 且 status = published
     * 
     * @param userId 用户ID
     * @return 用户已报名的活动列表
     */
    List<ActivityListResponse> getJoinedActivities(@Param("userId") Integer userId);

    /**
     * 获取用户可报名的活动列表
     * 业务逻辑：signup_end_time_and_activity_start_time > NOW() 且未满员且用户未报名且status = published
     * 
     * @param userId 用户ID
     * @return 用户可报名的活动列表
     */
    List<ActivityListResponse> getAvailableActivities(@Param("userId") Integer userId);

    /**
     * 获取用户历史参与过的活动列表
     * 业务逻辑：attended = 1
     * 
     * @param userId 用户ID
     * @return 用户历史参与过的活动列表
     */
    List<ActivityListResponse> getHistoryActivities(@Param("userId") Integer userId);

    /**
     * 获取最近参与的用户头像列表（复用ActivityMapper的方法）
     * 
     * @param activityId 活动ID
     * @return 用户头像URL列表（最多3个）
     */
    List<String> getRecentParticipantAvatars(@Param("activityId") Integer activityId);
} 