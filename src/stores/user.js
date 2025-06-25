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
        // 活动参与数量
        activity_participantion_num: 3,
        // 用户类型：normal(普通用户), committee(居委会), admin(管理员)
        user_type: "normal",
        // 登录状态
        isLoggedIn: false
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
        // 保存到本地存储
        saveUserToStorage();
    };

    // 用户登录方法 - 设置用户类型和登录状态
    const loginUser = (userData, userType) => {
        // 更新用户信息
        Object.assign(userInfo, {
            ...userData,
            user_type: userType,
            isLoggedIn: true
        });
        
        // 保存到本地存储
        saveUserToStorage();
        
        console.log(`用户登录成功 - 类型: ${userType}`, userInfo);
    };

    // 用户退出登录
    const logoutUser = () => {
        // 重置用户信息为默认值
        Object.assign(userInfo, {
            userId: "",
            nickname: "默认用户名",
            introduction: "默认个人介绍", 
            avatar: "",
            green_score: 10,
            history: [0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            activity_participantion_num: 3,
            user_type: "normal",
            isLoggedIn: false
        });
        
        // 清除本地存储
        uni.removeStorageSync('userInfo');
        console.log("用户已退出登录");
    };

    // 保存用户信息到本地存储
    const saveUserToStorage = () => {
        try {
            uni.setStorageSync('userInfo', JSON.stringify(userInfo));
            console.log("用户信息已保存到本地存储");
        } catch (error) {
            console.error("保存用户信息失败:", error);
        }
    };

    // 从本地存储加载用户信息
    const loadUserFromStorage = () => {
        try {
            const savedUserInfo = uni.getStorageSync('userInfo');
            if (savedUserInfo) {
                const parsedUserInfo = JSON.parse(savedUserInfo);
                // 恢复用户信息
                Object.assign(userInfo, parsedUserInfo);
                console.log("从本地存储恢复用户信息:", userInfo);
                return true;
            }
        } catch (error) {
            console.error("加载用户信息失败:", error);
        }
        return false;
    };

    // 检查用户权限
    const checkUserPermission = (requiredType) => {
        return userInfo.user_type === requiredType;
    };

    // 获取用户类型显示名称
    const getUserTypeLabel = () => {
        const typeLabels = {
            'normal': '普通用户',
            'committee': '居委会',
            'admin': '管理员'
        };
        return typeLabels[userInfo.user_type] || '未知类型';
    };

    // 初始化时从本地存储加载用户信息
    loadUserFromStorage();

    return { 
        userInfo,
        rank,
        modifyUserInfo,
        modifyGreenPlantScore,
        loginUser,
        logoutUser,
        saveUserToStorage,
        loadUserFromStorage,
        checkUserPermission,
        getUserTypeLabel
    }
})