package com.whu.wufeibackend.dto;

/**
 * 排行榜项目DTO
 * 用于表示排行榜中的单个条目
 */
public class RankItem {
    
    /**
     * 称号/头衔
     */
    private String title;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 绿色积分
     */
    private Integer greenScore;

    // 构造函数
    public RankItem() {}

    public RankItem(String title, String nickname, Integer greenScore) {
        this.title = title;
        this.nickname = nickname;
        this.greenScore = greenScore;
    }

    // Getter和Setter方法
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGreenScore() {
        return greenScore;
    }

    public void setGreenScore(Integer greenScore) {
        this.greenScore = greenScore;
    }

    @Override
    public String toString() {
        return "RankItem{" +
                "title='" + title + '\'' +
                ", nickname='" + nickname + '\'' +
                ", greenScore=" + greenScore +
                '}';
    }
} 