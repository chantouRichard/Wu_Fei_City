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
        <view class="form-item">
          <text class="label">活动名称</text>
          <view class="input-wrapper">
            <input
              v-model="activityData.title"
              class="input"
              placeholder="请输入"
              placeholder-class="placeholder"
            />
            <text class="arrow">></text>
          </view>
        </view>

        <view class="divider" @click.stop></view>

        <picker mode="date" :value="activityData.date" @change="onDateChange">
          <view class="form-item">
            <text class="label">活动日期</text>
            <view class="input-wrapper">
              <text
                class="input"
                :class="{ placeholder: !activityData.activity_end_time }"
                >{{ activityData.activity_end_time || "请选择" }}</text
              >
              <text class="arrow">></text>
            </view>
          </view>
        </picker>

        <view class="divider"></view>

        <view class="form-item">
          <text class="label">报名结束时间</text>
          <picker
            mode="multiSelector"
            :value="registrationEndTimeIndex"
            :range="registrationTimeRange"
            @change="onRegistrationEndTimeChange"
          >
            <view class="picker-display">
              <text class="picker-text">{{
                activityData.signup_end_time_and_activity_start_time || "请选择时间"
              }}</text>
              <text class="arrow">></text>
            </view>
          </picker>
        </view>

        <view class="divider"></view>

        <view class="form-item">
          <text class="label">活动地点</text>
          <view class="input-wrapper" @click="showLocationPicker">
            <text
              class="input"
              :class="{ placeholder: !activityData.location }"
              >{{ activityData.location || "请选择" }}</text
            >
            <text class="arrow">></text>
          </view>
        </view>

        <view class="divider"></view>

        <view class="form-item">
          <text class="label">最大参与人数</text>
          <view class="input-wrapper">
            <input
              type="number"
              v-model.number="activityData.max_participants"
              class="input"
              placeholder="默认 50"
              placeholder-class="placeholder"
            />
            <text class="arrow">人</text>
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
          />

          <view class="image-container">
            <view class="image-list">
              <view v-if="activityData.image_url.length > 0" class="image-item">
                <image
                  :src="activityData.image_url[0]"
                  class="preview-image"
                  mode="aspectFill"
                  @click="previewImage(0)"
                />
                <view class="delete-btn" @click="deleteImage(0)">X</view>
              </view>
              <view v-else class="image-upload" @click="chooseImage">
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
          saved: activeTab === 'basic' && isSaved,
          'publish-style': activeTab === 'detail' && isDetailComplete,
          published: activeTab === 'detail' && isPublished,
          incomplete: activeTab === 'detail' && !isDetailComplete,
        }"
        @click="handleButtonClick"
      >
        <view class="btn-content">
          <text class="btn-text">{{ buttonText }}</text>
        </view>
      </button>
    </view>
  </view>
</template>

<script setup>
import { reactive, computed, ref } from "vue";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

const activeTab = ref("basic");
const isSaved = ref(false);
const isPublished = ref(false);
const savedActivities = ref([]);

const activityData = reactive({
  organizerId: userStore.userInfo.userId,
  title: "测试",
  description: "123123123123123123123123123123",
  signup_end_time_and_activity_start_time: "2025-06-22T12:00:00",
  activity_end_time: "2025-06-22T14:00:00",
  location: "武汉大学图书馆",
  max_participants: 10,
  image_url: [],
});

const registrationStartTimeIndex = ref([0, 0, 0, 0, 0]);
const registrationEndTimeIndex = ref([0, 0, 0, 0, 0]);
const registrationTimeRange = [
  ["2024", "2025", "2026", "2027"],
  ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
  [...Array(31).keys()].map((i) => String(i + 1).padStart(2, "0")),
  [...Array(24).keys()].map((i) => String(i).padStart(2, "0")),
  ["00", "15", "30", "45"],
];

const buttonText = computed(() => {
  if (activeTab.value === "basic") return isSaved.value ? "已保存" : "保存数据";
  return isPublished.value ? "已发布" : "发布";
});

const isDetailComplete = computed(
  () => activityData.description.trim() && activityData.image_url.length > 0
);

const handleButtonClick = () => {
  if (activeTab.value === "basic") {
    isSaved.value = true;
    uni.showToast({ title: "保存成功", icon: "success" });
    return;
  }
  if (!isDetailComplete.value) {
    uni.showToast({ title: "请填写完整信息", icon: "none" });
    return;
  }
  publishActivity();
};

function switchTab(tab) {
  activeTab.value = tab;
}
function onDateChange(e) {
  activityData.date = e.detail.value;
}
function showLocationPicker() {
  uni.showActionSheet({
    itemList: ["社区广场", "活动中心", "公园", "其他"],
    success: (res) =>
      (activityData.location = ["社区广场", "活动中心", "公园", "其他"][
        res.tapIndex
      ]),
  });
}

function formatDateTime(val) {
  const [y, m, d, h, min] = val.map((v, i) => registrationTimeRange[i][v]);
  return `${y}-${m}-${d}T${h}:${min}:00`;
}

function onRegistrationStartTimeChange(e) {
  registrationStartTimeIndex.value = e.detail.value;
  activityData.registrationStartTime = formatDateTime(e.detail.value);
}
function onRegistrationEndTimeChange(e) {
  registrationEndTimeIndex.value = e.detail.value;
  activityData.registrationEndTime = formatDateTime(e.detail.value);
}

function publishActivity() {
  const payload = {
    organizerId: userStore.userInfo.userId,
    title: activityData.title,
    description: activityData.description,
    activity_end_time: activityData.activity_end_time,
    location: activityData.location,
    signup_end_time_and_activity_start_time: activityData.signup_end_time_and_activity_start_time,
    image_url: activityData.image_url[0] || "",
    max_participants: activityData.max_participants || 50,
  };

  console.log("image:",activityData.image_url);
  uni.request({
    url: "http://localhost:8080/api/activities/create",
    method: "POST",
    data: payload,
    success: (res) => {
      if (res.statusCode === 200) {
        isPublished.value = true;
        uni.showToast({ title: "发布成功", icon: "success" });
        setTimeout(() => uni.navigateBack(), 1000);
      } else {
        uni.showToast({ title: "发布失败", icon: "none" });
      }
    },
  });
}

function chooseImage() {
  if (activityData.image_url.length >= 1) return;

  uni.chooseImage({
    count: 1,
    success: (res) => {
      const filePath = res.tempFilePaths[0];
      uni.getFileSystemManager().readFile({
        filePath,
        encoding: 'base64',
        success: (fileRes) => {
          // 添加图片 MIME 前缀（如 PNG）
          const base64 = 'data:image/png;base64,' + fileRes.data;
          activityData.image_url[0] = base64;  // 如果你希望直接存储字符串
        },
        fail: (err) => {
          console.error('读取图片为Base64失败', err);
        },
      });
    },
    fail: (err) => {
      console.error('选择图片失败', err);
    },
  });
}

function previewImage(index) {
  uni.previewImage({ current: index, urls: activityData.image_url });
}
function deleteImage(index) {
  uni.showModal({
    title: "确认删除",
    content: "确定要删除这张图片吗？",
    success: (res) => res.confirm && activityData.image_url.splice(index, 1),
  });
}
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

.save-btn.incomplete {
  background: #7f7f7f !important;
  color: #ffffff !important;
  box-shadow: none !important;
}

.save-btn.incomplete:active {
  opacity: 1;
  transform: none;
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

  max-height: 200px;
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
