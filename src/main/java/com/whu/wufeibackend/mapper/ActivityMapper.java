package com.whu.wufeibackend.mapper;

import com.whu.wufeibackend.entity.Activity;
import com.whu.wufeibackend.entity.ActivityParticipant;
import com.whu.wufeibackend.dto.ActivityListResponse.HistoryActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 活动Mapper接口
 * 处理活动相关的数据库操作
 */
@Mapper
public interface ActivityMapper {
    
    /**
     * 获取可参与的活动列表
     * 查询条件：status='published'且未结束的活动
     * 
     * @return 可参与的活动列表
     */
    List<Activity> findAvailableActivities();
    
    /**
     * 获取用户已报名的活动ID列表
     * 
     * @param userId 用户ID
     * @return 已报名的活动ID列表
     */
    List<Integer> findJoinedActivityIds(@Param("userId") Integer userId);
    
    /**
     * 获取用户的历史活动列表（包含参与状态）
     * 
     * @param userId 用户ID
     * @return 历史活动列表，包含attended状态
     */
    List<HistoryActivity> findHistoryActivities(@Param("userId") Integer userId);
    
    /**
     * 根据ID查询活动
     * 
     * @param id 活动ID
     * @return 活动信息
     */
    Activity findById(@Param("id") Integer id);
    
    /**
     * 插入新活动
     * 
     * @param activity 活动信息
     * @return 影响的行数
     */
    int insertActivity(Activity activity);
    
    /**
     * 更新活动信息
     * 
     * @param activity 活动信息
     * @return 影响的行数
     */
    int updateActivity(Activity activity);
    
    /**
     * 删除活动
     * 
     * @param id 活动ID
     * @return 影响的行数
     */
    int deleteActivity(@Param("id") Integer id);
    
    /**
     * 用户报名参与活动
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 影响的行数
     */
    int insertParticipant(@Param("activityId") Integer activityId, @Param("userId") Integer userId);
    
    /**
     * 取消用户参与活动
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 影响的行数
     */
    int deleteParticipant(@Param("activityId") Integer activityId, @Param("userId") Integer userId);
    
    /**
     * 更新用户参与状态（居委会确认参与）
     * 
     * @param activityId 活动ID
     * @param userId 用户ID
     * @param attended 是否参与
     * @return 影响的行数
     */
    int updateParticipantAttended(@Param("activityId") Integer activityId, 
                                 @Param("userId") Integer userId, 
                                 @Param("attended") Boolean attended);
    
    /**
     * 查询活动的参与人数
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    int countParticipants(@Param("activityId") Integer activityId);
} 