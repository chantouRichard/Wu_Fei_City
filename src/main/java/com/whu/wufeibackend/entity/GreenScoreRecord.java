package com.whu.wufeibackend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 绿色积分记录实体类
 * 
 * @author 无废技术组
 * @since 2024-01-01
 */
public class GreenScoreRecord {
    
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 积分值(可正可负)
     */
    private Integer score;
    
    /**
     * 行为类型(如:recycling,energy_saving,activity_participation等)
     */
    private String actionType;
    
    /**
     * 行为描述
     */
    private String description;
    
    /**
     * 记录日期
     */
    private LocalDate recordDate;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    // 默认构造函数
    public GreenScoreRecord() {}
    
    // 带参构造函数
    public GreenScoreRecord(Integer userId, Integer score, String actionType, String description, LocalDate recordDate) {
        this.userId = userId;
        this.score = score;
        this.actionType = actionType;
        this.description = description;
        this.recordDate = recordDate;
    }
    
    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getRecordDate() {
        return recordDate;
    }
    
    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "GreenScoreRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", score=" + score +
                ", actionType='" + actionType + '\'' +
                ", description='" + description + '\'' +
                ", recordDate=" + recordDate +
                ", createdAt=" + createdAt +
                '}';
    }
} 