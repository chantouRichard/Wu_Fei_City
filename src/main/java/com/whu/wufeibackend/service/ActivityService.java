package com.whu.wufeibackend.service;

import com.whu.wufeibackend.DTO.ActivityListResponse;
import com.whu.wufeibackend.DTO.CreateActivityRequest;
import com.whu.wufeibackend.DTO.CreateActivityResponse;
import com.whu.wufeibackend.DTO.MakeSureUser;
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
@Service
public class ActivityService {

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Autowired(required = false)
    private ActivityMapper activityMapper;
    
    @Autowired
    private TimeService timeService;

    /**
     * 获取开放报名的活动列表
     * 逻辑：created_at_and_signup_start_time < NOW() AND signup_end_time_and_activity_start_time > NOW() AND status != 'finished'
     * 
     * @return 开放报名的活动列表
     */
    public List<ActivityListResponse> getOpenActivities(Integer userId) {
        logger.info("开始获取开放报名的活动列表");
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = activityMapper.getOpenActivities(userId);
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
     * 逻辑：signup_end_time_and_activity_start_time < NOW() AND activity_end_time > NOW() AND status != 'finished'
     * 
     * @return 进行中的活动列表
     */
    public List<ActivityListResponse> getInProgressActivities(Integer userId) {
        logger.info("开始获取进行中的活动列表");
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = activityMapper.getInProgressActivities(userId);
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
     * 逻辑：activity_end_time < NOW() OR status = 'finished'
     * 
     * @return 已结束的活动列表
     */
    public List<ActivityListResponse> getFinishedActivities(Integer userId) {
        logger.info("开始获取已结束的活动列表");
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<ActivityListResponse> activities = activityMapper.getFinishedActivities(userId);
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
        activity1.setCreatedAtAndSignupStartTime(LocalDateTime.of(2025, 6, 28, 8, 0)); // 报名开始时间
        activity1.setSignupEndTimeAndActivityStartTime(LocalDateTime.of(2025, 7, 5, 9, 0)); // 报名结束/活动开始时间
        activity1.setActivityEndTime(LocalDateTime.of(2025, 7, 5, 18, 0)); // 活动结束时间
        activity1.setStatus("open");
        activity1.setImageUrl("https://cdn.example.com/activities/cleaning.jpg");
        activity1.setLocation("东湖社区小广场");
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
        activity1.setCreatedAtAndSignupStartTime(LocalDateTime.of(2024, 6, 10, 8, 0)); // 报名开始时间
        activity1.setSignupEndTimeAndActivityStartTime(LocalDateTime.of(2024, 6, 20, 9, 0)); // 报名结束/活动开始时间
        activity1.setActivityEndTime(LocalDateTime.of(2024, 7, 20, 20, 0)); // 活动结束时间
        activity1.setStatus("inprogress");
        activity1.setImageUrl("https://cdn.example.com/activities/green-travel.jpg");
        activity1.setLocation("西湖社区文化广场");
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
        activity1.setTitle("垃圾分类知识竞赛");
        activity1.setOrganizerName("南山社区居委会");
        activity1.setCreatedAtAndSignupStartTime(LocalDateTime.of(2024, 5, 15, 8, 0)); // 报名开始时间
        activity1.setSignupEndTimeAndActivityStartTime(LocalDateTime.of(2024, 5, 25, 14, 0)); // 报名结束/活动开始时间
        activity1.setActivityEndTime(LocalDateTime.of(2024, 5, 25, 17, 0)); // 活动结束时间
        activity1.setStatus("finished");
        activity1.setImageUrl("https://cdn.example.com/activities/quiz.jpg");
        activity1.setLocation("南山社区活动中心");
        activity1.setAvatars(Arrays.asList(
            "https://cdn.example.com/users/avatar6.jpg",
            "https://cdn.example.com/users/avatar7.jpg",
            "https://cdn.example.com/users/avatar8.jpg"
        ));
        activity1.setParticipantCount(45);
        activity1.setMaxParticipants(50);

        activities.add(activity1);
        logger.debug("生成模拟已结束活动数量: {}", activities.size());
        return activities;
    }

    /**
     * 获取最近参与的用户头像列表
     * 
     * @param activityId 活动ID
     * @return 最近参与的三位用户头像列表
     */
    public List<String> getRecentParticipantAvatars(Integer activityId) {
        logger.info("开始获取活动ID: {} 的最近参与者头像", activityId);
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<String> avatars = activityMapper.getRecentParticipantAvatars(activityId);
                logger.info("从数据库获取到活动ID: {} 的头像数量: {}", activityId, avatars != null ? avatars.size() : 0);
                return avatars != null ? avatars : new ArrayList<>();
            } else {
                logger.warn("数据库连接为空，返回空列表");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("获取活动ID: {} 的参与者头像时发生异常: {}", activityId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取活动参与人数
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    public Integer getParticipantCount(Integer activityId) {
        logger.info("开始获取活动ID: {} 的参与人数", activityId);
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                Integer count = activityMapper.getParticipantCount(activityId);
                logger.info("从数据库获取到活动ID: {} 的参与人数: {}", activityId, count);
                return count != null ? count : 0;
            } else {
                logger.warn("数据库连接为空，返回0");
                return 0;
            }
        } catch (Exception e) {
            logger.error("获取活动ID: {} 的参与人数时发生异常: {}", activityId, e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 根据ID查询活动详情
     * 
     * @param id 活动ID
     * @return 活动详情
     */
    public Activity findById(Integer id) {
        logger.info("开始查询活动ID: {} 的详情", id);
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                Activity activity = activityMapper.findById(id);
                logger.info("查询活动ID: {} 的结果: {}", id, activity != null ? "找到" : "未找到");
                return activity;
            } else {
                logger.warn("数据库连接为空，返回null");
                return null;
            }
        } catch (Exception e) {
            logger.error("查询活动ID: {} 时发生异常: {}", id, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 查询所有活动
     * 
     * @return 所有活动列表
     */
    public List<Activity> findAll() {
        logger.info("开始查询所有活动");
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行数据库查询");
                List<Activity> activities = activityMapper.findAll();
                logger.info("查询到活动总数: {}", activities != null ? activities.size() : 0);
                return activities != null ? activities : new ArrayList<>();
            } else {
                logger.warn("数据库连接为空，返回空列表");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("查询所有活动时发生异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 创建新活动（用于API接口）
     * 根据CreateActivityRequest创建活动，自动设置创建时间和状态
     * 
     * @param request 创建活动请求
     * @param organizerId 组织者ID（居委会用户ID）
     * @return 创建的活动响应信息
     */
    public CreateActivityResponse createActivity(CreateActivityRequest request, Long organizerId) {
        logger.info("开始创建新活动，标题: {}, 组织者ID: {}", request.getTitle(), organizerId);
        
        try {
            // 验证时间逻辑（使用时间服务）
            LocalDateTime currentTime = timeService.now();
            if (!request.isTimeSequenceValid(currentTime)) {
                logger.warn("创建活动失败：时间逻辑不正确，当前时间: {}, 活动开始时间: {}, 活动结束时间: {}", 
                           currentTime, 
                           request.getSignupEndTimeAndActivityStartTime(), 
                           request.getActivityEndTime());
                return null;
            }
            
            // 构建Activity实体
            Activity activity = new Activity();
            activity.setTitle(request.getTitle());
            activity.setDescription(request.getDescription());
            activity.setOrganizerId(organizerId.intValue());
            activity.setSignupEndTimeAndActivityStartTime(request.getSignupEndTimeAndActivityStartTime());
            activity.setActivityEndTime(request.getActivityEndTime());
            activity.setLocation(request.getLocation());
            activity.setMaxParticipants(request.getMaxParticipants());
            activity.setImageUrl(request.getImageUrl());
            
            // 设置创建时间为当前时间（这就是报名开始时间）
            LocalDateTime now = timeService.now();
            activity.setCreatedAtAndSignupStartTime(now);
            
            logger.debug("使用时间服务获取当前时间: {}, 时间服务信息: {}", now, timeService.getTimeInfo());
            
            // 默认状态为发布状态
            activity.setStatus("published");
            
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行插入操作");
                int result = activityMapper.insert(activity);
                if (result > 0) {
                    logger.info("成功创建活动，ID: {}, 标题: {}", activity.getId(), activity.getTitle());
                    
                    // 构建响应对象
                    CreateActivityResponse response = new CreateActivityResponse();
                    response.setActivityId(activity.getId().longValue());
                    response.setTitle(activity.getTitle());
                    response.setDescription(activity.getDescription());
                    response.setCreatedAtAndSignupStartTime(activity.getCreatedAtAndSignupStartTime());
                    response.setSignupEndTimeAndActivityStartTime(activity.getSignupEndTimeAndActivityStartTime());
                    response.setActivityEndTime(activity.getActivityEndTime());
                    response.setLocation(activity.getLocation());
                    response.setMaxParticipants(activity.getMaxParticipants());
                    response.setStatus(activity.getStatus());
                    response.setImageUrl(activity.getImageUrl());
                    response.setOrganizerId(activity.getOrganizerId().longValue());
                    
                    return response;
                } else {
                    logger.warn("创建活动失败，插入操作返回: {}", result);
                    return null;
                }
            } else {
                logger.warn("数据库连接为空，无法创建活动");
                return null;
            }
        } catch (Exception e) {
            logger.error("创建活动时发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 创建新活动（原有方法，保持向后兼容）
     * 
     * @param activity 活动信息
     * @return 创建的活动（包含生成的ID）
     */
    public Activity createActivity(Activity activity) {
        logger.info("开始创建新活动: {}", activity.getTitle());
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行插入操作");
                int result = activityMapper.insert(activity);
                if (result > 0) {
                    logger.info("成功创建活动，ID: {}, 标题: {}", activity.getId(), activity.getTitle());
                    return activity;
                } else {
                    logger.warn("创建活动失败，插入操作返回: {}", result);
                    return null;
                }
            } else {
                logger.warn("数据库连接为空，无法创建活动");
                return null;
            }
        } catch (Exception e) {
            logger.error("创建活动时发生异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 更新活动信息
     * 
     * @param activity 活动信息
     * @return 更新后的活动信息
     */
    public Activity updateActivity(Activity activity) {
        logger.info("开始更新活动ID: {}, 标题: {}", activity.getId(), activity.getTitle());
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行更新操作");
                int result = activityMapper.update(activity);
                if (result > 0) {
                    logger.info("成功更新活动ID: {}", activity.getId());
                    return activity;
                } else {
                    logger.warn("更新活动失败，更新操作返回: {}", result);
                    return null;
                }
            } else {
                logger.warn("数据库连接为空，无法更新活动");
                return null;
            }
        } catch (Exception e) {
            logger.error("更新活动ID: {} 时发生异常: {}", activity.getId(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 删除活动
     * 
     * @param id 活动ID
     * @return 是否删除成功
     */
    public boolean deleteActivity(Integer id) {
        logger.info("开始删除活动ID: {}", id);
        
        try {
            if (activityMapper != null) {
                logger.debug("数据库连接正常，开始执行删除操作");
                int result = activityMapper.deleteById(id);
                boolean success = result > 0;
                if (success) {
                    logger.info("成功删除活动ID: {}", id);
                } else {
                    logger.warn("删除活动失败，删除操作返回: {}", result);
                }
                return success;
            } else {
                logger.warn("数据库连接为空，无法删除活动");
                return false;
            }
        } catch (Exception e) {
            logger.error("删除活动ID: {} 时发生异常: {}", id, e.getMessage(), e);
            return false;
        }
    }

    public List<MakeSureUser> makeSureActivity(Integer activityId){
        return activityMapper.makeSureActivity(activityId);
    }

    public int confirmAttendance(Integer activityId, List<Integer> userIds) {
        // 批量更新参与状态
        return activityMapper.confirmAttendance(activityId, userIds);
    }
} 