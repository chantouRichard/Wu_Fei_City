package com.whu.wufeibackend.entity;

import java.time.LocalDateTime;

/**
 * 活动参与记录实体类
 * 对应activity_participants表
 */
public class ActivityParticipant {
    
    /**
     * 记录ID
     */
    private Integer id;
    
    /**
     * 关联活动ID
     */
    private Integer activityId;
    
    /**
     * 参与用户ID
     */
    private Integer userId;
    
    /**
     * 是否实际参与（居委会确认）
     */
    private Boolean attended;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    // 默认构造函数
    public ActivityParticipant() {}

    // 带参构造函数
    public ActivityParticipant(Integer activityId, Integer userId, Boolean attended) {
        this.activityId = activityId;
        this.userId = userId;
        this.attended = attended;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ActivityParticipant{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", userId=" + userId +
                ", attended=" + attended +
                ", createdAt=" + createdAt +
                '}';
    }
} 