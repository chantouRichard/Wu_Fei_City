<template>
  <view class="my-page">
    <!-- 头部背景 -->
    <view class="header-section">
      <image
        v-if="userStore.userInfo.avatar"
        :src="userStore.userInfo.avatar"
        class="header-bg-img"
        mode="aspectFill"
      />
      <view v-else class="header-bg-default" />
      <view class="header-mask" />

      <!-- 头部内容 -->
      <view class="header-content">
        <view class="header-main">
          <!-- 左侧头像 -->
          <view class="avatar-section">
            <image
              v-if="userStore.userInfo.avatar"
              :src="userStore.userInfo.avatar"
              class="avatar-large"
              mode="aspectFill"
            />
            <view v-else class="avatar-placeholder-large">🌱</view>
          </view>
          
          <!-- 右侧用户信息 -->
          <view class="user-info">
            <text class="nickname-large">{{ userStore.userInfo.nickname }}</text>
            <text class="intro-grey">{{ userStore.userInfo.introduction }}</text>
          </view>
        </view>
        
        <!-- 统计信息和编辑按钮在同一行 -->
        <view class="stats-edit-row">
          <view class="stats-container">
            <view class="stat-item">
              <text class="value-large">{{ userStore.userInfo.green_score }}</text>
              <text class="label-small">绿植总分</text>
            </view>
            <view class="stat-item">
              <text class="value-large">{{ userStore.userInfo.activity_participantion_num }}</text>
              <text class="label-small">参与活动</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 内容面板 -->
    <view class="content-panel">
      <view class="panel-header">
        <view class="panel-title-section">
          <text class="panel-title">绿植记录</text>
        </view>
        <!-- 扁平椭圆形加号，在右边 -->
      </view>

      <!-- 日历容器，添加上下居中和空白 -->
      <view class="calendar-container">
        <view class="calendar-box">
          <view class="calendar-nav-section">
            <view class="month-nav-row">
              <text class="month-year-grey">{{ currentMonthYear }}</text>
            </view>
            <view class="nav-divider"></view>
          </view>
          
          <view class="calendar-grid">
            <text
              v-for="(day, i) in weekdays"
              :key="i"
              class="weekday"
            >
              {{ day }}
            </text>
          </view>
          <view class="divider" />
          <view class="calendar-dates">
            <view
              v-for="(_, i) in emptyDays"
              :key="`e${i}`"
              class="calendar-day empty"
            />
            <view
              v-for="(day, idx) in daysInMonth"
              :key="day"
              class="calendar-day"
              :class="getDayClass(idx)"
              @tap="onDayClick(day, idx)"
            >
              <text>{{ day }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 编辑资料弹窗（保持不变） -->
    <view v-if="showEditModal" class="modal-overlay" @tap="closeEditModal">
      <view class="modal-content" @tap.stop>
        <view class="modal-header">
          <text>编辑资料</text>
        </view>
        <view class="modal-body">
          <view class="form-group">
            <text>昵称</text>
            <input v-model="editForm.nickname" class="form-input" />
          </view>
          <view class="form-group">
            <text>个人介绍</text>
            <textarea v-model="editForm.introduction" class="form-textarea" />
          </view>
          <view class="form-group">
            <text>头像</text>
            <button @tap="chooseAvatar">上传头像</button>
          </view>
        </view>
        <view class="modal-footer">
          <button class="btn-cancel" @tap="closeEditModal">取消</button>
          <button class="btn-save" @tap="saveProfile">保存</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();
const showEditModal = ref(false);
const currentDate = ref(new Date());
const weekdays = ['S','M','T','W','T','F','S'];

// 编辑表单
const editForm = ref({
    nickname: '',
    introduction: '',
    avatar: null
});

// 计算属性
const currentMonthYear = computed(() => {
    const y = currentDate.value.getFullYear();
    const m = currentDate.value.getMonth();
    const names = ['JANUARY','FEBRUARY','MARCH','APRIL','MAY','JUNE','JULY','AUGUST','SEPTEMBER','OCTOBER','NOVEMBER','DECEMBER'];
    return `${names[m]} ${y}`;
});

const daysInMonth = computed(() => {
    const y = currentDate.value.getFullYear();
    const m = currentDate.value.getMonth();
    return new Date(y, m+1, 0).getDate();
});

const emptyDays = computed(() => {
    const first = new Date(
        currentDate.value.getFullYear(), 
        currentDate.value.getMonth(),
        1
    ).getDay();
    return Array(first).fill(0);
});

// 方法
const openEditModal = () => {
    editForm.value = { 
        nickname: userStore.userInfo.nickname, 
        introduction: userStore.userInfo.introduction 
    };
    showEditModal.value = true;
};

const closeEditModal = () => {
    showEditModal.value = false;
};

const saveProfile = () => {
    userStore.updateProfile(
        editForm.value.nickname,
        editForm.value.introduction,
        editForm.value.avatar
    );
    uni.setStorageSync('userInfo', userStore.userInfo);
    closeEditModal();
};

const chooseAvatar = () => {
    uni.chooseImage({ 
        count: 1, 
        success: res => {
            editForm.value.avatar = res.tempFilePaths[0];
        } 
    });
};

const getDayClass = (idx) => {
    const h = userStore.userInfo.history || [];
    if (idx >= h.length) return 'level-0';
    return `level-${Math.min(Math.max(h[idx], 0), 4)}`;
};

const onDayClick = (day, idx) => {
    console.log(`第${day}天，贡献:${userStore.userInfo.history[idx] || 0}`);
};

</script>

<style lang="scss" scoped>
.my-page {
  background: #f5f6fa;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header-section {
  position: relative;
  box-sizing: border-box;
  height: 350rpx;
  margin-bottom: 20rpx;
}

.header-bg-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: blur(8rpx) brightness(0.6);
  transform: scale(1.02);
}

.header-bg-default {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg,#667eea 0%,#764ba2 100%);
}

.header-mask {
  position: absolute;
  inset: 0;
  height: 110%;
  z-index: 0;
  background: rgba(0,0,0,0.4);
}

.header-content {
  position: absolute;
  inset: 0;
  padding: 32rpx;
  display: flex;
  flex-direction: column;
  color: #fff;
}

.header-main {
  display: flex;
  align-items: flex-start;
  gap: 32rpx;
  margin-bottom: 24rpx;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
}

.avatar-large {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  overflow: hidden;
  border: 4rpx solid #fff;
}

.avatar-placeholder-large {
  width: 120rpx;
  height: 120rpx;
  background: #666;
  border-radius: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 60rpx;
}

.user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 8rpx;
}

.nickname-large {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
}

.intro-grey {
  font-size: 26rpx;
  color: #ccc;
}

/* 统计信息和编辑按钮同一行，编辑按钮放最右边 */
.stats-edit-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  margin-top: auto;
  margin-bottom: 10rpx;
}

.stats-container {
  display: flex;
  gap: 40rpx;
  flex: 1;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.value-large {
  font-size: 32rpx;
  font-weight: bold;
  color: #fff;
  line-height: 1;
  margin-bottom: 4rpx;
}

.label-small {
  font-size: 22rpx;
  color: white;
  line-height: 1;
}

.content-panel {
  flex: 1;
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 32rpx;
  display: flex;
  flex-direction: column;
}

/* 标题行，加号按钮放最右边 */
.panel-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 32rpx;
  width: 100%;
}

.panel-title-section {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
  flex: 1;
}

.panel-title {
  font-size: 45rpx;
  letter-spacing: 5rpx;
  font-weight: bold;
  color: #1B1B1B;
}

/* 日历容器，添加上下居中和空白 */
.calendar-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx 0; /* 上下留出空白 */
}

.calendar-box {
  width: 100%;
  max-width: 100%;
  background: #fff;
  border: 2rpx solid #e0e0e0;
  border-radius: 24rpx;
  padding: 32rpx;
  display: flex;
  flex-direction: column;
}

.calendar-nav-section {
  margin-bottom: 24rpx;
}

.month-nav-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.nav-divider {
  height: 2rpx;
  background: #e0e0e0;
  margin-bottom: 16rpx;
}

.month-year-grey {
  font-size: 36rpx;
  color: #888;
  font-weight: 500;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 16rpx;
  margin-bottom: 16rpx;
}

.weekday {
  text-align: center;
  font-size: 24rpx;
  color: #888;
  font-weight: 500;
}

.divider {
  height: 2rpx;
  background: #e0e0e0;
  margin: 16rpx 0;
}

.calendar-dates {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 6rpx;
}

.calendar-day {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 35rpx;
  border-radius: 8rpx;
  aspect-ratio: 1;
  min-height: 80rpx; /* 确保日期格子有最小高度 */
}

.calendar-day.empty {
  color: #777;
}

.calendar-day.level-1 {
  background: #dcedc8;
  color: #33691e;
}

.calendar-day.level-2 {
  background: #aed581;
  color: #fff;
}

.calendar-day.level-3 {
  background: #7cb342;
  color: #fff;
}

.calendar-day.level-4 {
  background: #558b2f;
  color: #fff;
}

/* 弹窗样式保持不变 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  border-radius: 16rpx;
  width: 80%;
  max-width: 600rpx;
  max-height: 80%;
  overflow: hidden;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32rpx;
  border-bottom: 2rpx solid #e0e0e0;
  font-size: 32rpx;
  font-weight: bold;
}

.close-btn {
  background: none;
  border: none;
  font-size: 40rpx;
  color: #999;
}

.modal-body {
  padding: 32rpx;
}

.form-group {
  margin-bottom: 32rpx;
}

.form-group text {
  display: block;
  margin-bottom: 16rpx;
  font-size: 28rpx;
  color: #333;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 16rpx;
  border: 2rpx solid #e0e0e0;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.form-textarea {
  height: 120rpx;
  resize: none;
}

.modal-footer {
  display: flex;
  gap: 16rpx;
  padding: 32rpx;
  border-top: 2rpx solid #e0e0e0;
}

.btn-cancel, .btn-save {
  flex: 1;
  padding: 24rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  text-align: center;
  border: none;
  outline: none;
}

.btn-cancel {
  background: #f5f5f5;
  color: #666;
}

.btn-save {
  background: #00b386;
  color: #fff;
}
</style>