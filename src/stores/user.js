import { defineStore } from "pinia";
import { ref , reactive } from "vue";

// 现在数据都是前端固定的模拟数据，等后端登录接口完成后，会在界面界面的登录函数处，修改数据
export const useUserStore = defineStore("user", () => {
    const userInfo = reactive({
        // 用户ID，用户的唯一标识
        userId: "1234567890",
        // 渲染我的界面的用户名，数据内容是当前用户名
        nickname: "默认用户名",
        // 渲染我的界面的简介，数据内容是当前用户简介
        introduction: "默认个人介绍",
        // 渲染我的界面的头像，数据内容是当前用户头像的Base64编码
        avatar: "",
        // 渲染我的界面的绿分，数据内容是当前用户总绿分
        green_score: 10,
        // 渲染我的界面的热力图，数据内容是当月的每日绿植分数档次
        history: [0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        // 
        activity_participantion_num: 3
    })

    const rank = ref([
        {
            "title": "1",
            "nickname": "user1",
            "green_score": "100"
        },
        {
            "title": "2",
            "nickname": "user2",
            "scogreen_scorere": "90"
        },
        {
            "title": "3",
            "nickname": "user3",
            "green_score": "80"
        },
        {
            "title": "4",
            "nickname": "user4",
            "green_score": "70"
        },
        {
            "title": "5",
            "nickname": "user5",
            "green_score": "60"
        }
    ]);

    // “我的”界面修改用户的用户名、个人介绍、头像
    // 后续调用后端提供的修改接口，现在前端本地修改渲染数据
    const modifyUserInfo = (nickname, introduction, avatar) => {
        
        // 正确更新 reactive 对象的方式
        Object.assign(userInfo, {
            nickname: nickname || userInfo.nickname,
            introduction: introduction || userInfo.introduction,
            ...(avatar && { avatar }) // 只有当avatar存在时才更新
        });
        
    };

    // 后续调用后端接口修改分数，现在前端本地修改渲染数据
    const modifyGreenPlantScore = (score) => {
        userInfo.green_score += score;
        console.log("更新绿分:", userInfo.green_score);
    };

    return { 
        userInfo,
        rank,
        modifyUserInfo,
        modifyGreenPlantScore
    }
})