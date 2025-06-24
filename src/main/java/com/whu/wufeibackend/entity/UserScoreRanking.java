package com.whu.wufeibackend.entity;

/**
 * 用户分数排名实体类
 * 对应数据库视图 user_score_ranking
 * 
 * @author 无废技术组
 * @since 2024-01-01
 */
public class UserScoreRanking {
    
    /**
     * 用户ID
     */
    private Integer id;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    /**
     * 总积分
     */
    private Integer totalScore;
    
    /**
     * 排名
     */
    private Integer rank;
    
    // 默认构造函数
    public UserScoreRanking() {}
    
    // 带参构造函数
    public UserScoreRanking(Integer id, String nickname, String avatar, Integer totalScore, Integer rank) {
        this.id = id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.totalScore = totalScore;
        this.rank = rank;
    }
    
    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Integer getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
    
    public Integer getRank() {
        return rank;
    }
    
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    
    @Override
    public String toString() {
        return "UserScoreRanking{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", totalScore=" + totalScore +
                ", rank=" + rank +
                '}';
    }
} 