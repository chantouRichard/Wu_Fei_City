package com.whu.wufeibackend.DTO;

/**
 * 登录响应DTO
 */
public class LoginResponse {
    
    /**
     * 用户ID
     */
    private Integer id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户类型
     */
    private String userType;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 个人介绍
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
     * JWT令牌（仅管理员登录时返回）
     */
    private String token;
    
    /**
     * 是否成功
     */
    private Boolean success;
    
    /**
     * 失败原因
     */
    private String reason;

    /**
     * 绿色积分（普通用户专用）
     */
    private Integer greenScore;
    
    /**
     * 历史数据数组（普通用户专用）
     */
    private java.util.List<Integer> history;
    
    /**
     * 活动参与次数（普通用户专用）
     */
    private Integer activityParticipationNum;
    
    /**
     * 排行榜数据（普通用户专用）
     */
    private java.util.List<RankItem> rank;

    // 构造函数
    public LoginResponse() {}

    public LoginResponse(Boolean success) {
        this.success = success;
    }

    public LoginResponse(Boolean success, String reason) {
        this.success = success;
        this.reason = reason;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getGreenScore() {
        return greenScore;
    }

    public void setGreenScore(Integer greenScore) {
        this.greenScore = greenScore;
    }

    public java.util.List<Integer> getHistory() {
        return history;
    }

    public void setHistory(java.util.List<Integer> history) {
        this.history = history;
    }

    public Integer getActivityParticipationNum() {
        return activityParticipationNum;
    }

    public void setActivityParticipationNum(Integer activityParticipationNum) {
        this.activityParticipationNum = activityParticipationNum;
    }

    public java.util.List<RankItem> getRank() {
        return rank;
    }

    public void setRank(java.util.List<RankItem> rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                ", nickname='" + nickname + '\'' +
                ", success=" + success +
                ", reason='" + reason + '\'' +
                '}';
    }
} 