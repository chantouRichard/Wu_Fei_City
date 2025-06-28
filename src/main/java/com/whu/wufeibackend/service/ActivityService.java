package com.whu.wufeibackend.service;

import com.whu.wufeibackend.dto.ActivityListResponse;
import com.whu.wufeibackend.entity.Activity;
import com.whu.wufeibackend.mapper.ActivityMapper;
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

    @Autowired(required = false)
    private ActivityMapper activityMapper;

    /**
     * 获取开放报名的活动列表
     * 逻辑：start_time < NOW() AND end_time > NOW() AND status != 'finished'
     * 
     * @return 开放报名的活动列表
     */
    public List<ActivityListResponse> getOpenActivities() {
        try {
            if (activityMapper != null) {
                List<ActivityListResponse> activities = activityMapper.getOpenActivities();
                
                // 为每个活动添加最近参与者头像
                for (ActivityListResponse activity : activities) {
                    List<String> avatars = activityMapper.getRecentParticipantAvatars(activity.getActivityId());
                    activity.setAvatars(avatars);
                }
                
                return activities;
            } else {
                // 返回模拟数据
                return getMockOpenActivities();
            }
        } catch (Exception e) {
            // 如果数据库连接失败，返回模拟数据
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
        try {
            if (activityMapper != null) {
                List<ActivityListResponse> activities = activityMapper.getInProgressActivities();
                
                // 为每个活动添加最近参与者头像
                for (ActivityListResponse activity : activities) {
                    List<String> avatars = activityMapper.getRecentParticipantAvatars(activity.getActivityId());
                    activity.setAvatars(avatars);
                }
                
                return activities;
            } else {
                return getMockInProgressActivities();
            }
        } catch (Exception e) {
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
        try {
            if (activityMapper != null) {
                List<ActivityListResponse> activities = activityMapper.getFinishedActivities();
                
                // 为每个活动添加最近参与者头像
                for (ActivityListResponse activity : activities) {
                    List<String> avatars = activityMapper.getRecentParticipantAvatars(activity.getActivityId());
                    activity.setAvatars(avatars);
                }
                
                return activities;
            } else {
                return getMockFinishedActivities();
            }
        } catch (Exception e) {
            return getMockFinishedActivities();
        }
    }

    // 模拟数据方法
    private List<ActivityListResponse> getMockOpenActivities() {
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
        return activities;
    }

    private List<ActivityListResponse> getMockInProgressActivities() {
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
        return activities;
    }

    private List<ActivityListResponse> getMockFinishedActivities() {
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
        return activities;
    }

    /**
     * 根据活动ID获取最近报名的三位用户头像
     * 
     * @param activityId 活动ID
     * @return 用户头像URL列表（最多3个）
     */
    public List<String> getRecentParticipantAvatars(Integer activityId) {
        if (activityMapper != null) {
            try {
                return activityMapper.getRecentParticipantAvatars(activityId);
            } catch (Exception e) {
                return Arrays.asList("https://cdn.example.com/users/default-avatar.jpg");
            }
        }
        return Arrays.asList("https://cdn.example.com/users/default-avatar.jpg");
    }

    /**
     * 根据活动ID获取参与人数
     * 
     * @param activityId 活动ID
     * @return 参与人数
     */
    public Integer getParticipantCount(Integer activityId) {
        if (activityMapper != null) {
            try {
                return activityMapper.getParticipantCount(activityId);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 根据ID查询活动详情
     * 
     * @param id 活动ID
     * @return 活动详情
     */
    public Activity findById(Integer id) {
        if (activityMapper != null) {
            try {
                return activityMapper.findById(id);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 查询所有活动
     * 
     * @return 活动列表
     */
    public List<Activity> findAll() {
        if (activityMapper != null) {
            try {
                return activityMapper.findAll();
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * 创建新活动
     * 
     * @param activity 活动信息
     * @return 创建的活动
     */
    public Activity createActivity(Activity activity) {
        if (activityMapper != null) {
            try {
                activityMapper.insert(activity);
                return activity;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 更新活动信息
     * 
     * @param activity 活动信息
     * @return 更新后的活动
     */
    public Activity updateActivity(Activity activity) {
        if (activityMapper != null) {
            try {
                activityMapper.update(activity);
                return activity;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 删除活动
     * 
     * @param id 活动ID
     * @return 是否删除成功
     */
    public boolean deleteActivity(Integer id) {
        if (activityMapper != null) {
            try {
                return activityMapper.deleteById(id) > 0;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
} 