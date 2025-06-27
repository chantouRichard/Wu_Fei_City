package com.whu.wufeibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 简化的注册请求DTO
 * 仅包含用户名和密码，其他字段使用默认值
 * 支持可选的字段，用于居委会注册
 * 
 * @author 无废技术组
 * @since 2024-06-23
 */
public class SimpleRegisterRequest {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度应在3-20个字符之间")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度应在6-50个字符之间")
    private String password;
    
    /**
     * 用户类型：normal-普通用户，committee-居委会
     * 可选字段，默认为normal
     */
    private String userType = "normal";
    
    /**
     * 用户昵称（可选）
     */
    private String nickname;
    
    /**
     * 个人介绍（可选）
     */
    private String introduction;
    
    /**
     * 头像URL（可选）
     */
    private String avatar;
    
    /**
     * 居委会描述（仅committee用户使用）
     */
    private String committeeDesc;
    
    /**
     * 联系电话（仅committee用户使用）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入正确的手机号码")
    private String contact;

    // 构造函数
    public SimpleRegisterRequest() {}

    public SimpleRegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.userType = "normal"; // 默认为普通用户
    }
    
    public SimpleRegisterRequest(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType != null ? userType : "normal";
    }

    // Getter和Setter方法
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
        this.userType = userType != null ? userType : "normal";
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
    
    /**
     * 判断是否为简化注册（只有用户名密码，自动设置为普通用户）
     */
    public boolean isSimpleRegister() {
        return "normal".equals(userType) || userType == null;
    }
    
    /**
     * 判断是否为居委会注册
     */
    public boolean isCommitteeRegister() {
        return "committee".equals(userType);
    }

    @Override
    public String toString() {
        return "SimpleRegisterRequest{" +
                "username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                ", nickname='" + nickname + '\'' +
                ", committeeDesc='" + committeeDesc + '\'' +
                ", contact='" + contact + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
} 