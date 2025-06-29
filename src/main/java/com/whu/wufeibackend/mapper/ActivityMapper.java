package com.whu.wufeibackend.mapper;

import com.whu.wufeibackend.entity.Activity;
import com.whu.wufeibackend.DTO.ActivityListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动数据访问层接口
 * 
 * @author 无废技术组
 * @since 2024-06-27
 */
@Mapper
public interface ActivityMapper {

    /**
     * 获取开放报名的活动列表
     * 逻辑：start_time < NOW() AND end_time > NOW() AND status != 'finished'
     * 
     * @return 开放报名的活动列表
     */
    List<ActivityListResponse> getOpenActivities(@Param("userId") Integer userId);

    /**
     * 获取进行中的活动列表
     * 逻辑：end_time < NOW() AND status != 'finished'
     * 
     * @return 进行中的活动列表
     */
    List<ActivityListResponse> getInProgressActivities(@Param("userId") Integer userId);

    /**
     * 获取已结束的活动列表
     * 逻辑：status == 'finished'
     * 
     * @return 已结束的活动列表
     */
    List<ActivityListResponse> getFinishedActivities(@Param("userId") Integer userId);

    /**
     * 根据活动ID获取最近报名的三位用户头像
     * 
     * @param activityId 活动ID
     * @return 用户头像URL列表（最多3个）
     */
    List<String> getRecentParticipantAvatars(@Param("activityId") Integer activityId);

    /**
     * 根据活动ID获取参与人数
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    Integer getParticipantCount(@Param("activityId") Integer activityId);

    /**
     * 根据ID查询活动详情
     * 
     * @param id 活动ID
     * @return 活动详情
     */
    Activity findById(@Param("id") Integer id);

    /**
     * 查询所有活动
     * 
     * @return 活动列表
     */
    List<Activity> findAll();

    /**
     * 插入新活动
     * 
     * @param activity 活动信息
     * @return 影响行数
     */
    int insert(Activity activity);

    /**
     * 更新活动信息
     * 
     * @param activity 活动信息
     * @return 影响行数
     */
    int update(Activity activity);

    /**
     * 删除活动
     * 
     * @param id 活动ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
} 