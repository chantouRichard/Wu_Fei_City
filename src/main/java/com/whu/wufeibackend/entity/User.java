package com.whu.wufeibackend.entity;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库users表
 */
public class User {
    
    /**
     * 用户ID，主键，自增
     */
    private Integer id;
    
    /**
     * 登录用户名，唯一
     */
    private String username;
    
    /**
     * 登录密码
     */
    private String password;
    
    /**
     * 用户类型：normal-普通用户，committee-居委会，admin-管理员
     */
    private String userType;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 个人介绍，仅normal用户使用
     */
    private String introduction;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 居委会描述
     */
    private String committeeDesc;
    
    /**
     * 联系电话
     */
    private String contact;
    
    /**
     * 是否待审批：0-否，1-是
     */
    private Integer pending;
    
    /**
     * 审批状态：null-未处理，1-通过，0-拒绝
     */
    private Integer approved;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // 构造函数
    public User() {}

    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCommitteeDesc() {
        return committeeDesc;
    }

    public void setCommitteeDesc(String committeeDesc) {
        this.committeeDesc = committeeDesc;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                ", nickname='" + nickname + '\'' +
                ", pending=" + pending +
                ", approved=" + approved +
                ", createdAt=" + createdAt +
                '}';
    }
} 