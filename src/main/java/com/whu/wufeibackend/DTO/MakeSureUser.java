package com.whu.wufeibackend.DTO;

public class MakeSureUser{
    String nickname;

    Integer id;

    public MakeSureUser() {
    }

    public MakeSureUser(String nickName, Integer id) {
        this.nickname = nickName;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int userId) {
        this.id = userId;
    }
}
