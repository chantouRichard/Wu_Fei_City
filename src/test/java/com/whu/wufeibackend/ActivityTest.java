package com.whu.wufeibackend;

import com.whu.wufeibackend.dto.ActivityListResponse;
import com.whu.wufeibackend.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 活动功能测试类
 */
@SpringBootTest
public class ActivityTest {

    @Autowired
    private ActivityService activityService;

    @Test
    public void testGetOpenActivities() {
        try {
            List<ActivityListResponse> activities = activityService.getOpenActivities();
            System.out.println("开放报名活动数量: " + activities.size());
            for (ActivityListResponse activity : activities) {
                System.out.println("活动: " + activity.getTitle() + " - " + activity.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetInProgressActivities() {
        try {
            List<ActivityListResponse> activities = activityService.getInProgressActivities();
            System.out.println("进行中活动数量: " + activities.size());
            for (ActivityListResponse activity : activities) {
                System.out.println("活动: " + activity.getTitle() + " - " + activity.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFinishedActivities() {
        try {
            List<ActivityListResponse> activities = activityService.getFinishedActivities();
            System.out.println("已结束活动数量: " + activities.size());
            for (ActivityListResponse activity : activities) {
                System.out.println("活动: " + activity.getTitle() + " - " + activity.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 