package com.whu.wufeibackend.service;

import com.whu.wufeibackend.dto.ActivityListResponse;
import com.whu.wufeibackend.entity.Activity;
import com.whu.wufeibackend.mapper.ActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 活动业务逻辑服务类
 * 
 * @author 无废技术组
 * @since 2024-06-27
 */
@Service
public class ActivityService {

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Autowired(required = false)
    private ActivityMapper activityMapper;

    /**
     * 获取开放报名的活动列表
     * 逻辑：start_time < NOW() AND end_time > NOW() AND status != 'finished'
     * 
     * @return 开放报名的活动列表
     */
    public List<ActivityListResponse> getOpenActivities() {
        logger.info("开始获取开放报名的活动列表");
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = activityMapper.getOpenActivities();
                logger.info("从数据库获取到开放报名活动数量: {}", activities != null ? activities.size() : 0);
                
                // 为每个活动添加最近参与者头像
                if (activities != null) {
                    for (ActivityListResponse activity : activities) {
                        logger.debug("为活动ID: {} 获取参与者头像", activity.getActivityId());
                        try {
                            List<String> avatars = activityMapper.getRecentParticipantAvatars(activity.getActivityId());
                            activity.setAvatars(avatars);
                            logger.debug("活动ID: {} 获取到头像数量: {}", 
                                       activity.getActivityId(), 
                                       avatars != null ? avatars.size() : 0);
                        } catch (Exception e) {
                            logger.warn("获取活动ID: {} 的参与者头像失败: {}", activity.getActivityId(), e.getMessage());
                            activity.setAvatars(new ArrayList<>());
                        }
                    }
                }
                
                logger.info("成功完成开放报名活动列表的数据库查询和处理");
                return activities;
            } else {
                logger.warn("数据库连接为空，切换到模拟数据模式");
                return getMockOpenActivities();
            }
        } catch (Exception e) {
            logger.error("获取开放报名活动列表时发生异常: {}, 切换到模拟数据模式", e.getMessage(), e);
            return getMockOpenActivities();
        }
    }

    /**
     * 获取进行中的活动列表
     * 逻辑：end_time < NOW() AND status != 'finished'
     * 
     * @return 进行中的活动列表
     */
    public List<ActivityListResponse> getInProgressActivities() {
        logger.info("开始获取进行中的活动列表");
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = activityMapper.getInProgressActivities();
                logger.info("从数据库获取到进行中活动数量: {}", activities != null ? activities.size() : 0);
                
                // 为每个活动添加最近参与者头像
                if (activities != null) {
                    for (ActivityListResponse activity : activities) {
                        logger.debug("为活动ID: {} 获取参与者头像", activity.getActivityId());
                        try {
                            List<String> avatars = activityMapper.getRecentParticipantAvatars(activity.getActivityId());
                            activity.setAvatars(avatars);
                            logger.debug("活动ID: {} 获取到头像数量: {}", 
                                       activity.getActivityId(), 
                                       avatars != null ? avatars.size() : 0);
                        } catch (Exception e) {
                            logger.warn("获取活动ID: {} 的参与者头像失败: {}", activity.getActivityId(), e.getMessage());
                            activity.setAvatars(new ArrayList<>());
                        }
                    }
                }
                
                logger.info("成功完成进行中活动列表的数据库查询和处理");
                return activities;
            } else {
                logger.warn("数据库连接为空，切换到模拟数据模式");
                return getMockInProgressActivities();
            }
        } catch (Exception e) {
            logger.error("获取进行中活动列表时发生异常: {}, 切换到模拟数据模式", e.getMessage(), e);
            return getMockInProgressActivities();
        }
    }

    /**
     * 获取已结束的活动列表
     * 逻辑：status == 'finished'
     * 
     * @return 已结束的活动列表
     */
    public List<ActivityListResponse> getFinishedActivities() {
        logger.info("开始获取已结束的活动列表");
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = activityMapper.getFinishedActivities();
                logger.info("从数据库获取到已结束活动数量: {}", activities != null ? activities.size() : 0);
                
                // 为每个活动添加最近参与者头像
                if (activities != null) {
                    for (ActivityListResponse activity : activities) {
                        logger.debug("为活动ID: {} 获取参与者头像", activity.getActivityId());
                        try {
                            List<String> avatars = activityMapper.getRecentParticipantAvatars(activity.getActivityId());
                            activity.setAvatars(avatars);
                            logger.debug("活动ID: {} 获取到头像数量: {}", 
                                       activity.getActivityId(), 
                                       avatars != null ? avatars.size() : 0);
                        } catch (Exception e) {
                            logger.warn("获取活动ID: {} 的参与者头像失败: {}", activity.getActivityId(), e.getMessage());
                            activity.setAvatars(new ArrayList<>());
                        }
                    }
                }
                
                logger.info("成功完成已结束活动列表的数据库查询和处理");
                return activities;
            } else {
                logger.warn("数据库连接为空，切换到模拟数据模式");
                return getMockFinishedActivities();
            }
        } catch (Exception e) {
            logger.error("获取已结束活动列表时发生异常: {}, 切换到模拟数据模式", e.getMessage(), e);
            return getMockFinishedActivities();
        }
    }

    // 模拟数据方法
    private List<ActivityListResponse> getMockOpenActivities() {
        logger.info("使用模拟数据生成开放报名活动列表");
        List<ActivityListResponse> activities = new ArrayList<>();
        
        ActivityListResponse activity1 = new ActivityListResponse();
        activity1.setActivityId(1);
        activity1.setTitle("社区清洁日");
        activity1.setOrganizerName("东湖社区居委会");
        activity1.setStartTime(LocalDateTime.of(2025, 7, 1, 8, 0));
        activity1.setEndTime(LocalDateTime.of(2025, 7, 5, 18, 0));
        activity1.setStatus("open");
        activity1.setImageUrl("https://cdn.example.com/activities/cleaning.jpg");
        activity1.setLocation("东社区小广场");
        activity1.setAvatars(Arrays.asList(
            "https://cdn.example.com/users/avatar1.jpg",
            "https://cdn.example.com/users/avatar2.jpg",
            "https://cdn.example.com/users/avatar3.jpg"
        ));
        activity1.setParticipantCount(26);
        activity1.setMaxParticipants(50);
        
        activities.add(activity1);
        logger.debug("生成模拟开放报名活动数量: {}", activities.size());
        return activities;
    }

    private List<ActivityListResponse> getMockInProgressActivities() {
        logger.info("使用模拟数据生成进行中活动列表");
        List<ActivityListResponse> activities = new ArrayList<>();
        
        ActivityListResponse activity1 = new ActivityListResponse();
        activity1.setActivityId(2);
        activity1.setTitle("绿色出行倡议");
        activity1.setOrganizerName("西湖社区居委会");
        activity1.setStartTime(LocalDateTime.of(2024, 6, 10, 8, 0));
        activity1.setEndTime(LocalDateTime.of(2024, 6, 20, 20, 0));
        activity1.setStatus("inprogress");
        activity1.setImageUrl("https://cdn.example.com/activities/green-travel.jpg");
        activity1.setLocation("社区门口");
        activity1.setAvatars(Arrays.asList(
            "https://cdn.example.com/users/avatar4.jpg",
            "https://cdn.example.com/users/avatar5.jpg"
        ));
        activity1.setParticipantCount(18);
        activity1.setMaxParticipants(30);
        
        activities.add(activity1);
        logger.debug("生成模拟进行中活动数量: {}", activities.size());
        return activities;
    }

    private List<ActivityListResponse> getMockFinishedActivities() {
        logger.info("使用模拟数据生成已结束活动列表");
        List<ActivityListResponse> activities = new ArrayList<>();
        
        ActivityListResponse activity1 = new ActivityListResponse();
        activity1.setActivityId(3);
        activity1.setTitle("社区植树活动");
        activity1.setOrganizerName("南湖社区居委会");
        activity1.setStartTime(LocalDateTime.of(2024, 5, 1, 7, 0));
        activity1.setEndTime(LocalDateTime.of(2024, 5, 10, 19, 0));
        activity1.setStatus("finished");
        activity1.setImageUrl("https://cdn.example.com/activities/tree-planting.jpg");
        activity1.setLocation("社区花园");
        activity1.setAvatars(Arrays.asList(
            "https://cdn.example.com/users/avatar6.jpg",
            "https://cdn.example.com/users/avatar7.jpg",
            "https://cdn.example.com/users/avatar8.jpg"
        ));
        activity1.setParticipantCount(25);
        activity1.setMaxParticipants(25);
        
        activities.add(activity1);
        logger.debug("生成模拟已结束活动数量: {}", activities.size());
        return activities;
    }

    /**
     * 根据活动ID获取最近报名的三位用户头像
     * 
     * @param activityId 活动ID
     * @return 用户头像URL列表（最多3个）
     */
    public List<String> getRecentParticipantAvatars(Integer activityId) {
        logger.info("开始获取活动ID: {} 的参与者头像", activityId);
        
        if (activityMapper != null) {
            try {
                logger.debug("执行数据库查询获取活动ID: {} 的参与者头像", activityId);
                List<String> avatars = activityMapper.getRecentParticipantAvatars(activityId);
                logger.info("成功获取活动ID: {} 的参与者头像，数量: {}", 
                           activityId, avatars != null ? avatars.size() : 0);
                return avatars;
            } catch (Exception e) {
                logger.error("获取活动ID: {} 的参与者头像失败: {}, 返回默认头像", 
                            activityId, e.getMessage(), e);
                return Arrays.asList("https://cdn.example.com/users/default-avatar.jpg");
            }
        }
        
        logger.warn("数据库连接为空，返回默认头像");
        return Arrays.asList("https://cdn.example.com/users/default-avatar.jpg");
    }

    /**
     * 根据活动ID获取参与人数
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    public Integer getParticipantCount(Integer activityId) {
        logger.info("开始获取活动ID: {} 的参与人数", activityId);
        
        if (activityMapper != null) {
            try {
                logger.debug("执行数据库查询获取活动ID: {} 的参与人数", activityId);
                Integer count = activityMapper.getParticipantCount(activityId);
                logger.info("成功获取活动ID: {} 的参与人数: {}", activityId, count);
                return count;
            } catch (Exception e) {
                logger.error("获取活动ID: {} 的参与人数失败: {}, 返回0", 
                            activityId, e.getMessage(), e);
                return 0;
            }
        }
        
        logger.warn("数据库连接为空，返回参与人数0");
        return 0;
    }

    /**
     * 根据ID查询活动详情
     * 
     * @param id 活动ID
     * @return 活动详情
     */
    public Activity findById(Integer id) {
        logger.info("开始查询活动详情，活动ID: {}", id);
        
        if (activityMapper != null) {
            try {
                logger.debug("执行数据库查询获取活动ID: {} 的详情", id);
                Activity activity = activityMapper.findById(id);
                if (activity != null) {
                    logger.info("成功获取活动ID: {} 的详情，标题: {}", id, activity.getTitle());
                } else {
                    logger.warn("未找到活动ID: {} 的详情", id);
                }
                return activity;
            } catch (Exception e) {
                logger.error("查询活动ID: {} 的详情失败: {}", id, e.getMessage(), e);
                return null;
            }
        }
        
        logger.warn("数据库连接为空，无法查询活动详情");
        return null;
    }

    /**
     * 查询所有活动
     * 
     * @return 活动列表
     */
    public List<Activity> findAll() {
        logger.info("开始查询所有活动");
        
        if (activityMapper != null) {
            try {
                logger.debug("执行数据库查询获取所有活动");
                List<Activity> activities = activityMapper.findAll();
                logger.info("成功获取所有活动，数量: {}", activities != null ? activities.size() : 0);
                return activities;
            } catch (Exception e) {
                logger.error("查询所有活动失败: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        }
        
        logger.warn("数据库连接为空，返回空列表");
        return new ArrayList<>();
    }

    /**
     * 创建新活动
     * 
     * @param activity 活动信息
     * @return 创建的活动
     */
    public Activity createActivity(Activity activity) {
        logger.info("开始创建新活动，标题: {}", activity != null ? activity.getTitle() : "null");
        
        if (activityMapper != null) {
            try {
                logger.debug("执行数据库插入操作");
                activityMapper.insert(activity);
                logger.info("成功创建活动，ID: {}, 标题: {}", 
                           activity.getId(), activity.getTitle());
                return activity;
            } catch (Exception e) {
                logger.error("创建活动失败: {}", e.getMessage(), e);
                return null;
            }
        }
        
        logger.warn("数据库连接为空，无法创建活动");
        return null;
    }

    /**
     * 更新活动信息
     * 
     * @param activity 活动信息
     * @return 更新后的活动
     */
    public Activity updateActivity(Activity activity) {
        logger.info("开始更新活动，ID: {}, 标题: {}", 
                   activity != null ? activity.getId() : "null",
                   activity != null ? activity.getTitle() : "null");
        
        if (activityMapper != null) {
            try {
                logger.debug("执行数据库更新操作");
                activityMapper.update(activity);
                logger.info("成功更新活动，ID: {}, 标题: {}", 
                           activity.getId(), activity.getTitle());
                return activity;
            } catch (Exception e) {
                logger.error("更新活动失败: {}", e.getMessage(), e);
                return null;
            }
        }
        
        logger.warn("数据库连接为空，无法更新活动");
        return null;
    }

    /**
     * 删除活动
     * 
     * @param id 活动ID
     * @return 是否删除成功
     */
    public boolean deleteActivity(Integer id) {
        logger.info("开始删除活动，ID: {}", id);
        
        if (activityMapper != null) {
            try {
                logger.debug("执行数据库删除操作");
                boolean result = activityMapper.deleteById(id) > 0;
                if (result) {
                    logger.info("成功删除活动，ID: {}", id);
                } else {
                    logger.warn("删除活动失败，可能活动不存在，ID: {}", id);
                }
                return result;
            } catch (Exception e) {
                logger.error("删除活动失败，ID: {}, 错误: {}", id, e.getMessage(), e);
                return false;
            }
        }
        
        logger.warn("数据库连接为空，无法删除活动");
        return false;
    }
} 