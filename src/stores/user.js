import { defineStore } from "pinia";
import { ref, reactive } from "vue";

// 现在数据都是前端固定的模拟数据，等后端登录接口完成后，会在界面界面的登录函数处，修改数据
export const useUserStore = defineStore("user", () => {
  const userInfo = reactive({
    // 用户ID，用户的唯一标识
    userId: "-1",
    // 渲染我的界面的用户名，数据内容是当前用户名
    nickname: "海峰",
    // 渲染我的界面的简介，数据内容是当前用户简介
    introduction: "默认个人介绍",
    // 渲染我的界面的头像，数据内容是当前用户头像的Base64编码
    avatar: "",
    // 渲染我的界面的绿分，数据内容是当前用户总绿分
    green_score: 0,
    // 渲染我的界面的热力图，数据内容是当月的每日绿植分数档次
    history: [],
    // 活动参与数量
    activity_participantion_num: 3,
    // 用户类型：normal(普通用户), committee(居委会), admin(管理员)
    user_type: "normal",
  });

  const rank = ref([]);

    // “我的”界面修改用户的用户名、个人介绍、头像
    // 后续调用后端提供的修改接口，现在前端本地修改渲染数据
    const modifyUserInfo = (nickname, introduction, avatar) => {

        // 正确更新 reactive 对象的方式
        Object.assign(userInfo, {
            nickname: nickname || userInfo.nickname,
            introduction: introduction || userInfo.introduction,
            avatar: avatar || userInfo.avatar
        });

        // 调用后端的更新用户信息的接口
        wx.request({
            url: 'http://localhost:8080/user/update',
            method: 'Put',
            data: {
                userId: userInfo.userId,
                nickname: nickname || userInfo.nickname,
                introduction: introduction || userInfo.introduction,
                avatar: avatar || userInfo.avatar
            },
            success(res) {
                console.log("更新用户信息成功！", res.data);
            },
            fail(err) {
                console.error("更新用户信息失败！", err);
            }
        });

    };

    // 后续调用后端接口修改分数，现在前端本地修改渲染数据
    const modifyGreenPlantScore = async (score) => {
        userInfo.green_score += score;
        console.log("更新绿分:", userInfo.green_score);
        // 调用后端的更新分数的接口
        wx.request({
            url: 'http://localhost:8080/score/add',
            method: 'POST',
            data: {
                userId: userInfo.userId,
                score: score,
                description: '',
                actionType: ''
            },
            success(res) {
                console.log("更新分数成功！", res.data);
            },
            fail(err) {
                console.error("更新分数失败！", err);
            }
        });
    };
    // 用户登录方法 - 设置用户类型和登录状态
    const loginUser = (userData, userType) => {
        // 更新用户信息
        Object.assign(userInfo, {
            ...userData,
            user_type: userType,
        });
        uni.request({
            url: 'http://localhost:8080/api/login',
            method: 'POST',
            data: {
                username: userData.username,
                password: userData.password,
                userType: userType
            },
            success(res) {
                console.log("响应数据", res.data);
                if (res.data.code == 200) {
                    userInfo.userId = res.data.data.id;
                    userInfo.nickname = res.data.data.nickname;
                    userInfo.avatar = res.data.data.avatar;
                    userInfo.introduction = res.data.data.introduction;
                    userInfo.green_score = res.data.data.greenScore;
                    userInfo.activity_participantion_num = res.data.data.activityParticipationNum;
                    userInfo.history = res.data.data.history;
                    userInfo.user_type = res.data.data.userType;
                    rank.value = res.data.data.rank;

                    console.log("用户信息：",userInfo);

                    uni.showToast({
                        title: '登录成功',
                        icon: 'success'
                    })

                    // 登录成功后跳转到主页
                    setTimeout(() => {
                        uni.reLaunch({
                            url: '/pages/index/index'
                        })
                    }, 1500)
                } else {
                    uni.showToast({
                        title: '登录失败',
                        icon: 'error'
                    })
                }
            },
            fail(err) {
                console.error("登录失败！", err);
                uni.showToast({
                    title: '登录失败',
                    icon: 'error'
                })
            }
        });
    };
    // 更新绿植分数排行方法
    const updateRank = () => {
        uni.request({
            url: 'http://localhost:8080/score/ranking',
            method: 'GET',
            success(res) {
                console.log("更新排行成功！", res.data);
                rank.value = res.data.data;
            },
            fail(err) {
                console.error("更新排行失败！", err);
            }
        });

    }

    const updateHistory = (history) => {
        userInfo.history = history;
        uni.request({
            url: `http://localhost:8080/score/history/${userInfo.userId}`,
            method: 'GET',
            success(res) {
                console.log("更新历史成功！", res.data);
                userInfo.history = res.data.data;
            },
            fail(err) {
                console.error("更新历史失败！", err);
            }
        })
    }

    return {
        userInfo,
        rank,
        modifyUserInfo,
        modifyGreenPlantScore,
        loginUser,
        updateRank,
        updateHistory
    }
})
