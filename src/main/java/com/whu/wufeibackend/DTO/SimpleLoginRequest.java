package com.whu.wufeibackend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 简化的登录请求DTO
 * 仅包含用户名和密码，自动设置为普通用户登录
 * 支持可选的userType字段，用于居委会和管理员登录
 * 
 * @author 无废技术组
 * @since 2024-06-23
 */
public class SimpleLoginRequest {
    
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
     * 用户类型：normal-普通用户，committee-居委会，admin-管理员
     * 可选字段，默认为normal
     */
    private String userType = "normal";

    // 构造函数
    public SimpleLoginRequest() {}

    public SimpleLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.userType = "normal"; // 默认为普通用户
    }
    
    public SimpleLoginRequest(String username, String password, String userType) {
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
    
    /**
     * 判断是否为简化登录（只有用户名密码，自动设置为普通用户）
     */
    public boolean isSimpleLogin() {
        return "normal".equals(userType) || userType == null;
    }

    @Override
    public String toString() {
        return "SimpleLoginRequest{" +
                "username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
} 