package com.whu.wufeibackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 创建活动请求DTO
 * 
 * @author Wu Fei City Team
 * @since 2025-01-27
 */
public class CreateActivityRequest {
    
    /**
     * 活动标题
     * 必须至少2个字符
     */
    @NotBlank(message = "活动标题不能为空")
    @Size(min = 2, message = "活动标题长度至少为2个字符")
    private String title;
    
    /**
     * 活动详情描述
     */
    private String description;
    
    /**
     * 报名结束时间/活动开始时间
     * 必须晚于当前时间
     */
    @NotNull(message = "活动开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("signup_end_time_and_activity_start_time")
    private LocalDateTime signupEndTimeAndActivityStartTime;
    
    /**
     * 活动结束时间
     * 必须晚于活动开始时间
     */
    @NotNull(message = "活动结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("activity_end_time")
    private LocalDateTime activityEndTime;
    
    /**
     * 活动地点
     */
    @NotBlank(message = "活动地点不能为空")
    private String location;
    
    /**
     * 最大参与人数
     * 可选字段
     */
    @JsonProperty("max_participants")
    private Integer maxParticipants;
    
    /**
     * 活动图片URL
     * 可选字段
     */
    @JsonProperty("image_url")
    private String imageUrl;

    // 无参构造函数
    public CreateActivityRequest() {}

    // 全参构造函数
    public CreateActivityRequest(String title, String description, 
                               LocalDateTime signupEndTimeAndActivityStartTime,
                               LocalDateTime activityEndTime, String location, 
                               Integer maxParticipants, String imageUrl) {
        this.title = title;
        this.description = description;
        this.signupEndTimeAndActivityStartTime = signupEndTimeAndActivityStartTime;
        this.activityEndTime = activityEndTime;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.imageUrl = imageUrl;
    }

    // Getter和Setter方法
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 验证时间逻辑是否正确
     * 当前时间 < 活动开始时间 < 活动结束时间
     * 
     * @param currentTime 当前时间（支持测试模式下传入模拟时间）
     * @return 时间逻辑是否正确
     */
    public boolean isTimeSequenceValid(LocalDateTime currentTime) {
        if (signupEndTimeAndActivityStartTime == null || activityEndTime == null) {
            return false;
        }
        
        return currentTime.isBefore(signupEndTimeAndActivityStartTime) && 
               signupEndTimeAndActivityStartTime.isBefore(activityEndTime);
    }

    /**
     * 验证时间逻辑是否正确（使用当前系统时间）
     * 当前时间 < 活动开始时间 < 活动结束时间
     * 
     * @return 时间逻辑是否正确
     */
    public boolean isTimeSequenceValid() {
        return isTimeSequenceValid(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "CreateActivityRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", signupEndTimeAndActivityStartTime=" + signupEndTimeAndActivityStartTime +
                ", activityEndTime=" + activityEndTime +
                ", location='" + location + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
} 