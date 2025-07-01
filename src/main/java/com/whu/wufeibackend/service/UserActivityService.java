package com.whu.wufeibackend.service;

import com.whu.wufeibackend.DTO.ActivityListResponse;
import com.whu.wufeibackend.mapper.UserActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户活动业务逻辑服务类
 * 
 * 业务逻辑说明：
 * - joined: attended = 0 且 status ≠ finished 且 status = published
 * - available: signup_end_time_and_activity_start_time > NOW() 且未满员且用户未报名且status = published
 * - history: attended = 1
 * 
 * 满员逻辑：
 * - maxParticipants为NULL时永远不满员
 * - 满员判断：participantCount >= maxParticipants
 * 
 * @author 无废技术组
 * @since 2025-01-27
 */
@Service
public class UserActivityService {

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(UserActivityService.class);

    @Autowired(required = false)
    private UserActivityMapper userActivityMapper;

    /**
     * 获取用户已报名的活动列表
     * 业务逻辑：attended = 0 且 status ≠ finished 且 status = published
     * 
     * @param userId 用户ID
     * @return 用户已报名的活动列表
     */
    public List<ActivityListResponse> getJoinedActivities(Integer userId) {
        logger.info("开始获取用户ID: {} 的已报名活动列表", userId);
        
        try {
            if (userActivityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = userActivityMapper.getJoinedActivities(userId);
                logger.info("从数据库获取到用户ID: {} 的已报名活动数量: {}", userId, activities != null ? activities.size() : 0);
                
                // 为每个活动添加最近参与者头像
                if (activities != null) {
                    for (ActivityListResponse activity : activities) {
                        logger.debug("为活动ID: {} 获取参与者头像", activity.getActivityId());
                        try {
                            List<String> avatars = userActivityMapper.getRecentParticipantAvatars(activity.getActivityId());
                            activity.setAvatars(avatars);
                            activity.setStatus("joined"); // 设置状态为joined
                            logger.debug("活动ID: {} 获取到头像数量: {}", 
                                       activity.getActivityId(), 
                                       avatars != null ? avatars.size() : 0);
                        } catch (Exception e) {
                            logger.warn("获取活动ID: {} 的参与者头像失败: {}", activity.getActivityId(), e.getMessage());
                            activity.setAvatars(new ArrayList<>());
                        }
                    }
                }
                
                logger.info("成功完成用户ID: {} 的已报名活动列表查询和处理", userId);
                return activities;
            } else {
                logger.warn("数据库连接为空，切换到模拟数据模式");
                return getMockJoinedActivities(userId);
            }
        } catch (Exception e) {
            logger.error("获取用户ID: {} 的已报名活动列表时发生异常: {}, 切换到模拟数据模式", userId, e.getMessage(), e);
            return getMockJoinedActivities(userId);
        }
    }

    /**
     * 获取用户可报名的活动列表
     * 业务逻辑：signup_end_time_and_activity_start_time > NOW() 且未满员且用户未报名且status = published
     * 
     * @param userId 用户ID
     * @return 用户可报名的活动列表
     */
    public List<ActivityListResponse> getAvailableActivities(Integer userId) {
        logger.info("开始获取用户ID: {} 的可报名活动列表", userId);
        
        try {
            if (userActivityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = userActivityMapper.getAvailableActivities(userId);
                logger.info("从数据库获取到用户ID: {} 的可报名活动数量: {}", userId, activities != null ? activities.size() : 0);
                
                // 为每个活动添加最近参与者头像
                if (activities != null) {
                    for (ActivityListResponse activity : activities) {
                        logger.debug("为活动ID: {} 获取参与者头像", activity.getActivityId());
                        try {
                            List<String> avatars = userActivityMapper.getRecentParticipantAvatars(activity.getActivityId());
                            activity.setAvatars(avatars);
                            activity.setStatus("available"); // 设置状态为available
                            logger.debug("活动ID: {} 获取到头像数量: {}", 
                                       activity.getActivityId(), 
                                       avatars != null ? avatars.size() : 0);
                        } catch (Exception e) {
                            logger.warn("获取活动ID: {} 的参与者头像失败: {}", activity.getActivityId(), e.getMessage());
                            activity.setAvatars(new ArrayList<>());
                        }
                    }
                }
                
                logger.info("成功完成用户ID: {} 的可报名活动列表查询和处理", userId);
                return activities;
            } else {
                logger.warn("数据库连接为空，切换到模拟数据模式");
                return getMockAvailableActivities(userId);
            }
        } catch (Exception e) {
            logger.error("获取用户ID: {} 的可报名活动列表时发生异常: {}, 切换到模拟数据模式", userId, e.getMessage(), e);
            return getMockAvailableActivities(userId);
        }
    }

    /**
     * 获取用户历史参与过的活动列表
     * 业务逻辑：attended = 1
     * 
     * @param userId 用户ID
     * @return 用户历史参与过的活动列表
     */
    public List<ActivityListResponse> getHistoryActivities(Integer userId) {
        logger.info("开始获取用户ID: {} 的历史参与活动列表", userId);
        
        try {
            if (userActivityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = userActivityMapper.getHistoryActivities(userId);
                logger.info("从数据库获取到用户ID: {} 的历史参与活动数量: {}", userId, activities != null ? activities.size() : 0);
                
                // 为每个活动添加最近参与者头像
                if (activities != null) {
                    for (ActivityListResponse activity : activities) {
                        logger.debug("为活动ID: {} 获取参与者头像", activity.getActivityId());
                        try {
                            List<String> avatars = userActivityMapper.getRecentParticipantAvatars(activity.getActivityId());
                            activity.setAvatars(avatars);
                            activity.setStatus("history"); // 设置状态为history
                            logger.debug("活动ID: {} 获取到头像数量: {}", 
                                       activity.getActivityId(), 
                                       avatars != null ? avatars.size() : 0);
                        } catch (Exception e) {
                            logger.warn("获取活动ID: {} 的参与者头像失败: {}", activity.getActivityId(), e.getMessage());
                            activity.setAvatars(new ArrayList<>());
                        }
                    }
                }
                
                logger.info("成功完成用户ID: {} 的历史参与活动列表查询和处理", userId);
                return activities;
            } else {
                logger.warn("数据库连接为空，切换到模拟数据模式");
                return getMockHistoryActivities(userId);
            }
        } catch (Exception e) {
            logger.error("获取用户ID: {} 的历史参与活动列表时发生异常: {}, 切换到模拟数据模式", userId, e.getMessage(), e);
            return getMockHistoryActivities(userId);
        }
    }

    // 模拟数据方法
    private List<ActivityListResponse> getMockJoinedActivities(Integer userId) {
        logger.info("使用模拟数据生成用户ID: {} 的已报名活动列表", userId);
        List<ActivityListResponse> activities = new ArrayList<>();
        
        ActivityListResponse activity1 = new ActivityListResponse();
        activity1.setActivityId(101);
        activity1.setTitle("读书分享会");
        activity1.setOrganizerName("东湖社区居委会");
        activity1.setSignupEndTimeAndActivityStartTime(LocalDateTime.of(2025, 7, 4, 18, 0));
        activity1.setActivityEndTime(LocalDateTime.of(2025, 7, 5, 11, 0));
        activity1.setStatus("joined");
        activity1.setImageUrl("https://example.com/image.jpg");
        activity1.setLocation("图书馆三楼报告厅");
        activity1.setAvatars(Arrays.asList(
            "https://example.com/avatar1.jpg",
            "https://example.com/avatar2.jpg",
            "https://example.com/avatar3.jpg"
        ));
        activity1.setParticipantCount(16);
        activity1.setMaxParticipants(30);
        
        activities.add(activity1);
        logger.debug("生成模拟已报名活动数量: {}", activities.size());
        return activities;
    }

    private List<ActivityListResponse> getMockAvailableActivities(Integer userId) {
        logger.info("使用模拟数据生成用户ID: {} 的可报名活动列表", userId);
        List<ActivityListResponse> activities = new ArrayList<>();
        
        ActivityListResponse activity1 = new ActivityListResponse();
        activity1.setActivityId(102);
        activity1.setTitle("社区篮球比赛");
        activity1.setOrganizerName("西湖社区居委会");
        activity1.setSignupEndTimeAndActivityStartTime(LocalDateTime.of(2025, 8, 10, 9, 0));
        activity1.setActivityEndTime(LocalDateTime.of(2025, 8, 10, 17, 0));
        activity1.setStatus("available");
        activity1.setImageUrl("https://example.com/basketball.jpg");
        activity1.setLocation("社区篮球场");
        activity1.setAvatars(Arrays.asList(
            "https://example.com/avatar4.jpg",
            "https://example.com/avatar5.jpg"
        ));
        activity1.setParticipantCount(8);
        activity1.setMaxParticipants(20);
        
        activities.add(activity1);
        logger.debug("生成模拟可报名活动数量: {}", activities.size());
        return activities;
    }

    private List<ActivityListResponse> getMockHistoryActivities(Integer userId) {
        logger.info("使用模拟数据生成用户ID: {} 的历史参与活动列表", userId);
        List<ActivityListResponse> activities = new ArrayList<>();
        
        ActivityListResponse activity1 = new ActivityListResponse();
        activity1.setActivityId(103);
        activity1.setTitle("环保知识讲座");
        activity1.setOrganizerName("南山社区居委会");
        activity1.setSignupEndTimeAndActivityStartTime(LocalDateTime.of(2024, 5, 15, 14, 0));
        activity1.setActivityEndTime(LocalDateTime.of(2024, 5, 15, 16, 0));
        activity1.setStatus("history");
        activity1.setImageUrl("https://example.com/lecture.jpg");
        activity1.setLocation("社区活动中心");
        activity1.setAvatars(Arrays.asList(
            "https://example.com/avatar6.jpg",
            "https://example.com/avatar7.jpg",
            "https://example.com/avatar8.jpg"
        ));
        activity1.setParticipantCount(25);
        activity1.setMaxParticipants(30);
        
        activities.add(activity1);
        logger.debug("生成模拟历史参与活动数量: {}", activities.size());
        return activities;
    }
} 