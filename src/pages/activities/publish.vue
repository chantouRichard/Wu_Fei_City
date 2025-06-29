<template>
  <view class="container">
    <!-- 标签栏 -->
    <view class="tabs">
      <view
        class="tab"
        :class="{ active: activeTab === 'basic' }"
        @click="switchTab('basic')"
        >基本资料</view
      >
      <view
        class="tab"
        :class="{ active: activeTab === 'detail' }"
        @click="switchTab('detail')"
        >详细介绍</view
      >
    </view>

    <!-- 表单内容 -->
    <view class="form-content">
      <!-- 基本资料 -->
      <view v-if="activeTab === 'basic'" class="form-container">
        <!-- 活动名称 -->
        <view class="form-item">
          <text class="label">活动名称</text>
          <view class="input-wrapper">
            <input
              v-model="activityData.name"
              class="input"
              placeholder="请输入"
              placeholder-class="placeholder"
            />
            <text class="arrow">></text>
          </view>
        </view>

        <!-- 分隔线 -->
        <view class="divider" @click.stop></view>

        <!-- 活动日期 -->
        <picker mode="date" :value="activityData.date" @change="onDateChange">
          <view class="form-item">
            <text class="label">活动日期</text>
            <view class="input-wrapper">
              <text class="input" :class="{ placeholder: !activityData.date }">
                {{ activityData.date || "请选择" }}
              </text>
              <text class="arrow">></text>
            </view>
          </view>
        </picker>

        <!-- 分隔线 -->
        <view class="divider" @click.stop></view>

        <!-- 报名开始时间 -->
        <view class="form-item">
          <text class="label">报名开始时间</text>
          <picker
            mode="multiSelector"
            :value="registrationStartTimeIndex"
            :range="registrationTimeRange"
            @change="onRegistrationStartTimeChange"
            class="picker-wrapper"
          >
            <view class="picker-display">
              <text class="picker-text">{{
                activityData.registrationStartTime || "请选择时间"
              }}</text>
              <text class="arrow">></text>
            </view>
          </picker>
        </view>

        <!-- 分隔线 -->
        <view class="divider"></view>

        <!-- 报名结束时间 -->
        <view class="form-item">
          <text class="label">报名结束时间</text>
          <picker
            mode="multiSelector"
            :value="registrationEndTimeIndex"
            :range="registrationTimeRange"
            @change="onRegistrationEndTimeChange"
            class="picker-wrapper"
          >
            <view class="picker-display">
              <text class="picker-text">{{
                activityData.registrationEndTime || "请选择时间"
              }}</text>
              <text class="arrow">></text>
            </view>
          </picker>
        </view>

        <!-- 分隔线 -->
        <view class="divider"></view>

        <!-- 活动地点 -->
        <view class="form-item">
          <text class="label">活动地点</text>
          <view class="input-wrapper" @click="showLocationPicker">
            <text
              class="input"
              :class="{ placeholder: !activityData.location }"
            >
              {{ activityData.location || "请选择" }}
            </text>
            <text class="arrow">></text>
          </view>
        </view>
      </view>

      <!-- 详细介绍 -->
      <view v-if="activeTab === 'detail'" class="detail-container">
        <view class="detail-content">
          <textarea
            v-model="activityData.description"
            class="detail-textarea"
            placeholder="请输入详细活动介绍..."
            placeholder-class="textarea-placeholder"
            auto-height
            maxlength="500"
          ></textarea>

          <!-- 图片展示区域 -->
          <view class="image-container">
            <view class="image-list">
              <view
                v-for="(image, index) in activityData.images"
                :key="index"
                class="image-item"
              >
                <image
                  :src="image"
                  class="preview-image"
                  mode="aspectFill"
                  @click="previewImage(index)"
                ></image>
                <view class="delete-btn" @click="deleteImage(index)">X</view>
              </view>
              <view class="image-upload" @click="chooseImage">
                <view class="upload-icon">+</view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 保存/发布按钮 -->
    <view class="save-section">
      <button
        class="save-btn"
        :class="{
          saved: isSaved,
          'publish-style': activeTab === 'detail',
          published: isPublished,
        }"
        @click="handleButtonClick"
        :disabled="(isSaved && activeTab === 'basic') || isPublished"
      >
        <view class="btn-content">
          <text class="btn-text">{{ getButtonText() }}</text>
        </view>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: "basic", // 当前激活的标签
      activityData: {
        name: "",
        date: "",
        location: "",
        description: "", // 添加详细介绍字段
        images: [], // 添加图片数组
        registrationStartTime: "", // 报名开始时间
        registrationEndTime: "", // 报名结束时间
      },
      isSaved: false, // 保存状态
      isPublished: false, // 发布状态
      savedActivities: [], // 本地存储的活动数据
      // 时间选择器相关数据
      registrationStartTimeIndex: [0, 0, 0, 0, 0],
      registrationEndTimeIndex: [0, 0, 0, 0, 0],
      registrationTimeRange: [
        // 年份 (2024-2030)
        ["2024", "2025", "2026", "2027", "2028", "2029", "2030"],
        // 月份 (01-12)
        [
          "01",
          "02",
          "03",
          "04",
          "05",
          "06",
          "07",
          "08",
          "09",
          "10",
          "11",
          "12",
        ],
        // 日期 (01-31)
        [
          "01",
          "02",
          "03",
          "04",
          "05",
          "06",
          "07",
          "08",
          "09",
          "10",
          "11",
          "12",
          "13",
          "14",
          "15",
          "16",
          "17",
          "18",
          "19",
          "20",
          "21",
          "22",
          "23",
          "24",
          "25",
          "26",
          "27",
          "28",
          "29",
          "30",
          "31",
        ],
        // 小时 (00-23)
        [
          "00",
          "01",
          "02",
          "03",
          "04",
          "05",
          "06",
          "07",
          "08",
          "09",
          "10",
          "11",
          "12",
          "13",
          "14",
          "15",
          "16",
          "17",
          "18",
          "19",
          "20",
          "21",
          "22",
          "23",
        ],
        // 分钟 (00-59)
        [
          "00",
          "05",
          "10",
          "15",
          "20",
          "25",
          "30",
          "35",
          "40",
          "45",
          "50",
          "55",
        ],
      ],
    };
  },
  onLoad() {
    // 从本地存储加载已保存的活动
    this.loadSavedActivities();
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },
    switchTab(tab) {
      this.activeTab = tab;
    },
    onDateChange(e) {
      this.activityData.date = e.detail.value;
    },
    showLocationPicker() {
      // 这里可以实现地点选择功能
      uni.showActionSheet({
        itemList: ["社区广场", "活动中心", "公园", "其他"],
        success: (res) => {
          const locations = ["社区广场", "活动中心", "公园", "其他"];
          this.activityData.location = locations[res.tapIndex];
        },
      });
    },
    // 报名开始时间选择器变化
    onRegistrationStartTimeChange(e) {
      const val = e.detail.value;
      this.registrationStartTimeIndex = val;
      const year = this.registrationTimeRange[0][val[0]];
      const month = this.registrationTimeRange[1][val[1]];
      const day = this.registrationTimeRange[2][val[2]];
      const hour = this.registrationTimeRange[3][val[3]];
      const minute = this.registrationTimeRange[4][val[4]];
      this.activityData.registrationStartTime = `${year}-${month}-${day} ${hour}:${minute}`;
    },
    // 报名结束时间选择器变化
    onRegistrationEndTimeChange(e) {
      const val = e.detail.value;
      this.registrationEndTimeIndex = val;
      const year = this.registrationTimeRange[0][val[0]];
      const month = this.registrationTimeRange[1][val[1]];
      const day = this.registrationTimeRange[2][val[2]];
      const hour = this.registrationTimeRange[3][val[3]];
      const minute = this.registrationTimeRange[4][val[4]];
      this.activityData.registrationEndTime = `${year}-${month}-${day} ${hour}:${minute}`;
    },
    validateTimeFormat(type) {
      const timeRegex = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/;
      let timeValue = "";
      let fieldName = "";

      if (type === "start") {
        timeValue = this.activityData.registrationStartTime;
        fieldName = "报名开始时间";
      } else {
        timeValue = this.activityData.registrationEndTime;
        fieldName = "报名结束时间";
      }

      if (timeValue && !timeRegex.test(timeValue)) {
        uni.showToast({
          title: `${fieldName}格式错误，请使用 YYYY-MM-DD HH:mm 格式`,
          icon: "none",
          duration: 3000,
        });
        return false;
      }

      // 验证时间是否有效
      if (timeValue && timeRegex.test(timeValue)) {
        const date = new Date(timeValue.replace(" ", "T"));
        if (isNaN(date.getTime())) {
          uni.showToast({
            title: `${fieldName}无效，请输入正确的日期时间`,
            icon: "none",
            duration: 3000,
          });
          return false;
        }
      }

      // 验证开始时间不能晚于结束时间
      if (
        this.activityData.registrationStartTime &&
        this.activityData.registrationEndTime
      ) {
        const startTime = new Date(
          this.activityData.registrationStartTime.replace(" ", "T")
        );
        const endTime = new Date(
          this.activityData.registrationEndTime.replace(" ", "T")
        );

        if (startTime >= endTime) {
          uni.showToast({
            title: "报名开始时间不能晚于或等于结束时间",
            icon: "none",
            duration: 3000,
          });
          return false;
        }
      }

      return true;
    },

    saveActivity() {
      // 验证表单
      if (!this.activityData.name.trim()) {
        uni.showToast({
          title: "请输入活动名称",
          icon: "none",
        });
        return;
      }
      if (!this.activityData.date) {
        uni.showToast({
          title: "请选择活动日期",
          icon: "none",
        });
        return;
      }
      if (!this.activityData.location) {
        uni.showToast({
          title: "请选择活动地点",
          icon: "none",
        });
        return;
      }

      // 验证报名时间格式
      if (
        this.activityData.registrationStartTime &&
        !this.validateTimeFormat("start")
      ) {
        return;
      }
      if (
        this.activityData.registrationEndTime &&
        !this.validateTimeFormat("end")
      ) {
        return;
      }

      // 创建活动对象
      const newActivity = {
        id: Date.now(),
        title: this.activityData.name,
        date: this.activityData.date,
        location: this.activityData.location,
        signup_end_time_and_activity_start_time:
          this.activityData.registrationStartTime,
        activity_end_time: this.activityData.registrationEndTime,
        description: this.activityData.description,
        image_url:
          this.activityData.images[0] ||
          "https://cdn.example.com/activities/cleaning.jpg",
        createTime: new Date().toISOString(),
        max_participants: 50, // 默认限制50人
      };

      // 保存到本地存储
      this.savedActivities.push(newActivity);
      uni.setStorageSync("publishedActivities", this.savedActivities);

      // 更新保存状态
      this.isSaved = true;

      uni.request({
        url: "http://localhost:8080/api/activities/create", // 替换为你的API端点
        method: "POST",
        data: newActivity,
        success: (res) => {
          if (res.statusCode === 200) {
            // 处理成功情况，例如更新界面状态或显示消息等
            // 显示成功提示
            uni.showToast({
              title: "发布成功",
              icon: "success",
            });

            // 延迟切换到详细介绍标签
            setTimeout(() => {
              this.activeTab = "detail";
            }, 1500);
            uni.navigateBack();
          } else {
            uni.showToast({
              title: "发布失败",
              icon: "none",
            });
          }
        },
      });
    },
    loadSavedActivities() {
      const saved = uni.getStorageSync("publishedActivities");
      if (saved) {
        this.savedActivities = saved;
      }
    },
    getButtonText() {
      if (this.activeTab === "basic") {
        return this.isSaved ? "已保存" : "保存数据";
      } else {
        return this.isPublished ? "已发布" : "发布";
      }
    },
    handleButtonClick() {
      if (this.activeTab === "basic") {
        this.saveActivity();
      } else {
        this.publishActivity();
      }
    },
    publishActivity() {
      // 设置发布状态
      this.isPublished = true;

      // 显示发布成功提示
      uni.showToast({
        title: "发布成功",
        icon: "success",
      });

      // 按钮状态变化后立即跳转到活动中心
      setTimeout(() => {
        uni.redirectTo({
          url: "/pages/activities/activities",
        });
      }, 800);
    },
    chooseImage() {
      console.log("开始选择图片...");
      // 添加点击反馈
      uni.showToast({
        title: "正在打开相册...",
        icon: "loading",
        duration: 1000,
      });
      uni.chooseImage({
        count: 9, // 最多可以选择的图片张数
        sizeType: ["original", "compressed"], // 可以指定是原图还是压缩图
        sourceType: ["album", "camera"], // 可以指定来源是相册还是相机
        success: (res) => {
          console.log("选择图片成功:", res);
          // 将选择的图片添加到数组中
          this.activityData.images = this.activityData.images.concat(
            res.tempFilePaths
          );
          uni.showToast({
            title: `成功添加${res.tempFilePaths.length}张图片`,
            icon: "success",
          });
        },
        fail: (err) => {
          console.error("选择图片失败:", err);
          let errorMsg = "选择图片失败";
          if (err.errMsg) {
            if (err.errMsg.includes("cancel")) {
              errorMsg = "用户取消选择";
            } else if (err.errMsg.includes("auth")) {
              errorMsg = "请授权访问相册";
            } else {
              errorMsg = `选择失败: ${err.errMsg}`;
            }
          }
          uni.showToast({
            title: errorMsg,
            icon: "none",
            duration: 3000,
          });
        },
      });
    },
    previewImage(index) {
      uni.previewImage({
        current: index,
        urls: this.activityData.images,
      });
    },
    deleteImage(index) {
      uni.showModal({
        title: "确认删除",
        content: "确定要删除这张图片吗？",
        success: (res) => {
          if (res.confirm) {
            this.activityData.images.splice(index, 1);
            uni.showToast({
              title: "删除成功",
              icon: "success",
            });
          }
        },
      });
    },
  },
};
</script>

<style scoped>
.container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部导航 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 15px;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
  height: 44px;
}

.back-btn {
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 24px;
  color: #333;
  font-weight: bold;
}

.title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
}

.menu-btn,
.target-btn {
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-icon,
.target-icon {
  font-size: 18px;
  color: #333;
}

/* 标签栏 */
.tabs {
  display: flex;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.tab {
  flex: 1;
  padding: 15px 0;
  text-align: center;
  font-size: 16px;
  color: #999;
  position: relative;
}

.tab.active {
  color: #333;
  font-weight: 500;
}

.tab.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 2px;
  background-color: #007aff;
}

/* 表单内容 */
.form-content {
  padding: 20px 15px;
}

.form-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 0;
  overflow: hidden;
}

.form-item {
  padding: 0 15px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.divider {
  height: 1px;
  background-color: #f0f0f0;
  margin: 0 15px;
}

.label {
  font-size: 16px;
  color: #333;
  width: 100px;
  flex-shrink: 0;
  white-space: nowrap;
}

.input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-left: 15px;
}

.input {
  flex: 1;
  font-size: 16px;
  color: #333;
  border: none;
  outline: none;
  background: transparent;
  text-align: right;
  margin-right: 10px;
}

.placeholder {
  color: #7f7f7f;
  text-align: right;
}

.arrow {
  font-size: 16px;
  color: #333;
  font-weight: bold;
  margin-left: 10px;
}

.picker-wrapper {
  flex: 1;
  margin-left: 15px;
}

.picker-display {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  min-height: 20px;
}

.picker-text {
  flex: 1;
  font-size: 16px;
  color: #7f7f7f;
  text-align: right;
  margin-right: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.picker-text:empty::before {
  content: "请选择时间";
  color: #7f7f7f;
}

/* 保存按钮 */
.save-section {
  position: fixed;
  bottom: 30px;
  left: 15px;
  right: 15px;
}

.save-btn {
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, #b7ef68, #03e996);
  color: #fff;
  border: none;
  border-radius: 25px;
  font-size: 18px;
  font-weight: 500;
  box-shadow: 0 4px 15px rgba(76, 217, 100, 0.3);
}

.save-btn:active {
  opacity: 0.8;
  transform: scale(0.98);
}

.save-btn.saved {
  background: #fff;
  color: #7f7f7f;
  /* border: 2px solid #8B5CF6; */
  box-shadow: none;
}

.save-btn.publish-style {
  background: linear-gradient(135deg, #b7ef68, #03e996);
  color: white;
}

.save-btn.publish-style:active {
  opacity: 0.8;
}

.save-btn.published {
  background: #ffffff;
  color: #7f7f7f;
  box-shadow: none;
}

.btn-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-text {
  font-size: 16px;
}

.save-btn.published .btn-text {
  color: #7f7f7f;
}

.save-btn:not(.saved):not(.published) .btn-text {
  color: #000000;
}

.save-btn.saved:active {
  opacity: 1;
  transform: none;
}

/* 详细介绍页面样式 */
.detail-container {
  padding: 15px;
  background-color: #fff;
  margin: 10px;
  border-radius: 10px;
  width: 343px;
  min-height: 292px;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

.detail-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 10px;
  position: relative;
  min-height: 100%;
}

.detail-textarea {
  width: 100%;
  font-size: 16px;
  color: #333;
  border: none;
  outline: none;
  background: transparent;
  text-align: left;
  padding: 0;
  margin: 0;
  margin-bottom: 20px;
  line-height: 1.5;
  resize: none;
  box-sizing: border-box;
  min-height: 100px;
}

.textarea-placeholder {
  color: #999;
  font-size: 16px;
}

.image-upload {
  width: 121px;
  height: 121px;
  top: 305px;
  left: 32px;
  border-radius: 9px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #e6e6e6;
  cursor: pointer;
}

.upload-icon {
  font-size: 72px;
  color: #7f7f7f;
  font-weight: 450;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  line-height: 1;
}

.upload-text {
  font-size: 12px;
  color: #7f7f7f;
  margin-top: 5px;
  text-align: center;
}

/* 图片容器样式 */
.image-container {
  margin-top: 20px;
}

/* 图片列表样式 */
.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: flex-start;
}

.image-item {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: visible;
}

.preview-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.delete-btn {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 20px;
  height: 20px;
  background-color: #ff4757;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: bold;
  line-height: 1;
  cursor: pointer;
  z-index: 10;
}

.delete-btn:active {
  opacity: 0.8;
}
</style>
