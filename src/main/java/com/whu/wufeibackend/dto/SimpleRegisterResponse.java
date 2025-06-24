package com.whu.wufeibackend.dto;

/**
 * 超简化的注册响应DTO
 * 直接返回message和userInfo，不使用标准ApiResponse包装
 * 完全符合客户端要求的响应格式
 * 
 * @author 无废技术组
 * @since 2024-06-23
 */
public class SimpleRegisterResponse {
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 用户基本信息
     */
    private UserInfo userInfo;

    // 构造函数
    public SimpleRegisterResponse() {}

    public SimpleRegisterResponse(String message, String username, String userType) {
        this.message = message;
        this.userInfo = new UserInfo(username, userType);
    }

    // Getter和Setter方法
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 用户信息内部类
     */
    public static class UserInfo {
        
        /**
         * 用户名
         */
        private String username;
        
        /**
         * 用户类型
         */
        private String user_type;

        // 构造函数
        public UserInfo() {}

        public UserInfo(String username, String userType) {
            this.username = username;
            this.user_type = userType;
        }

        // Getter和Setter方法
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "username='" + username + '\'' +
                    ", user_type='" + user_type + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SimpleRegisterResponse{" +
                "message='" + message + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
} 