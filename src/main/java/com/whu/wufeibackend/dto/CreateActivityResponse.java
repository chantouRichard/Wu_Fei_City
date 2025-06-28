package com.whu.wufeibackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * 创建活动成功响应DTO
 * 
 * @author Wu Fei City Team
 * @since 2025-01-27
 */
public class CreateActivityResponse {
    
    /**
     * 活动ID
     */
    @JsonProperty("activity_id")
    private Long activityId;
    
    /**
     * 活动标题
     */
    private String title;
    
    /**
     * 活动详情描述
     */
    private String description;
    
    /**
     * 活动创建时间/报名开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("created_at_and_signup_start_time")
    private LocalDateTime createdAtAndSignupStartTime;
    
    /**
     * 报名结束时间/活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("signup_end_time_and_activity_start_time")
    private LocalDateTime signupEndTimeAndActivityStartTime;
    
    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("activity_end_time")
    private LocalDateTime activityEndTime;
    
    /**
     * 活动地点
     */
    private String location;
    
    /**
     * 最大参与人数
     */
    @JsonProperty("max_participants")
    private Integer maxParticipants;
    
    /**
     * 活动状态
     */
    private String status;
    
    /**
     * 活动图片URL
     */
    @JsonProperty("image_url")
    private String imageUrl;
    
    /**
     * 组织者ID
     */
    @JsonProperty("organizer_id")
    private Long organizerId;

    // 无参构造函数
    public CreateActivityResponse() {}

    // 全参构造函数
    public CreateActivityResponse(Long activityId, String title, String description,
                                LocalDateTime createdAtAndSignupStartTime,
                                LocalDateTime signupEndTimeAndActivityStartTime,
                                LocalDateTime activityEndTime, String location,
                                Integer maxParticipants, String status, String imageUrl,
                                Long organizerId) {
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.createdAtAndSignupStartTime = createdAtAndSignupStartTime;
        this.signupEndTimeAndActivityStartTime = signupEndTimeAndActivityStartTime;
        this.activityEndTime = activityEndTime;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.status = status;
        this.imageUrl = imageUrl;
        this.organizerId = organizerId;
    }

    // Getter和Setter方法
    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAtAndSignupStartTime() {
        return createdAtAndSignupStartTime;
    }

    public void setCreatedAtAndSignupStartTime(LocalDateTime createdAtAndSignupStartTime) {
        this.createdAtAndSignupStartTime = createdAtAndSignupStartTime;
    }

    public LocalDateTime getSignupEndTimeAndActivityStartTime() {
        return signupEndTimeAndActivityStartTime;
    }

    public void setSignupEndTimeAndActivityStartTime(LocalDateTime signupEndTimeAndActivityStartTime) {
        this.signupEndTimeAndActivityStartTime = signupEndTimeAndActivityStartTime;
    }

    public LocalDateTime getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(LocalDateTime activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
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

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    @Override
    public String toString() {
        return "CreateActivityResponse{" +
                "activityId=" + activityId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAtAndSignupStartTime=" + createdAtAndSignupStartTime +
                ", signupEndTimeAndActivityStartTime=" + signupEndTimeAndActivityStartTime +
                ", activityEndTime=" + activityEndTime +
                ", location='" + location + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", status='" + status + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", organizerId=" + organizerId +
                '}';
    }
} 