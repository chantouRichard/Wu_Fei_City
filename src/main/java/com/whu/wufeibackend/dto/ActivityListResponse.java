package com.whu.wufeibackend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动列表响应DTO
 * 用于GET /api/activities/* 接口的响应
 * 
 * @author 无废技术组
 * @since 2024-06-27
 */
public class ActivityListResponse {
    
    /**
     * 活动ID
     */
    private Integer activityId;
    
    /**
     * 活动标题
     */
    private String title;
    
    /**
     * 组织者名称
     */
    private String organizerName;
    
    /**
     * 活动开始时间（报名开始时间）
     */
    private LocalDateTime startTime;
    
    /**
     * 活动结束时间（报名结束时间）
     */
    private LocalDateTime endTime;
    
    /**
     * 活动状态：open/inprogress/finished
     * 由时间 + status 字段计算得出
     */
    private String status;
    
    /**
     * 活动图片URL
     */
    private String imageUrl;
    
    /**
     * 活动地点
     */
    private String location;
    
    /**
     * 最近报名的三位用户头像
     */
    private List<String> avatars;
    
    /**
     * 当前参与人数
     */
    private Integer participantCount;
    
    /**
     * 最大参与人数
     */
    private Integer maxParticipants;

    // 构造函数
    public ActivityListResponse() {}

    public ActivityListResponse(Integer activityId, String title, String organizerName,
                               LocalDateTime startTime, LocalDateTime endTime, String status,
                               String imageUrl, String location, List<String> avatars,
                               Integer participantCount, Integer maxParticipants) {
        this.activityId = activityId;
        this.title = title;
        this.organizerName = organizerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.imageUrl = imageUrl;
        this.location = location;
        this.avatars = avatars;
        this.participantCount = participantCount;
        this.maxParticipants = maxParticipants;
    }

    // Getter和Setter方法
    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<String> avatars) {
        this.avatars = avatars;
    }

    public Integer getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    @Override
    public String toString() {
        return "ActivityListResponse{" +
                "activityId=" + activityId +
                ", title='" + title + '\'' +
                ", organizerName='" + organizerName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location='" + location + '\'' +
                ", avatars=" + avatars +
                ", participantCount=" + participantCount +
                ", maxParticipants=" + maxParticipants +
                '}';
    }
} 