package com.whu.wufeibackend.dto;

import com.whu.wufeibackend.entity.Activity;
import java.util.List;

/**
 * 活动列表响应DTO
 */
public class ActivityListResponse {
    
    /**
     * 可参与的活动列表（status='published'且未结束）
     */
    private List<Activity> available;
    
    /**
     * 用户已报名的活动ID列表
     */
    private List<Integer> joined;
    
    /**
     * 历史活动列表（包含attended状态）
     */
    private List<HistoryActivity> history;

    // 默认构造函数
    public ActivityListResponse() {}

    // 带参构造函数
    public ActivityListResponse(List<Activity> available, List<Integer> joined, List<HistoryActivity> history) {
        this.available = available;
        this.joined = joined;
        this.history = history;
    }

    // Getter和Setter方法
    public List<Activity> getAvailable() {
        return available;
    }

    public void setAvailable(List<Activity> available) {
        this.available = available;
    }

    public List<Integer> getJoined() {
        return joined;
    }

    public void setJoined(List<Integer> joined) {
        this.joined = joined;
    }

    public List<HistoryActivity> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryActivity> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "ActivityListResponse{" +
                "available=" + available +
                ", joined=" + joined +
                ", history=" + history +
                '}';
    }

    /**
     * 历史活动内部类
     */
    public static class HistoryActivity extends Activity {
        
        /**
         * 是否实际参与（0-未参与，1-已参与）
         */
        private Integer attended;

        // 默认构造函数
        public HistoryActivity() {}

        // 带参构造函数
        public HistoryActivity(Activity activity, Integer attended) {
            super.setId(activity.getId());
            super.setTitle(activity.getTitle());
            super.setDescription(activity.getDescription());
            super.setOrganizerId(activity.getOrganizerId());
            super.setStartTime(activity.getStartTime());
            super.setEndTime(activity.getEndTime());
            super.setLocation(activity.getLocation());
            super.setMaxParticipants(activity.getMaxParticipants());
            super.setStatus(activity.getStatus());
            super.setCreatedAt(activity.getCreatedAt());
            super.setUpdatedAt(activity.getUpdatedAt());
            this.attended = attended;
        }

        // Getter和Setter方法
        public Integer getAttended() {
            return attended;
        }

        public void setAttended(Integer attended) {
            this.attended = attended;
        }

        @Override
        public String toString() {
            return "HistoryActivity{" +
                    "attended=" + attended +
                    ", activity=" + super.toString() +
                    '}';
        }
    }
} 