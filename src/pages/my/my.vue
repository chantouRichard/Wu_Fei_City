// MyPage.vue
<template>
  <view class="my-page">
    <!-- 顶部背景与头像 -->
    <view class="header-bg">
      <image :src="user.avatar" class="header-bg-img" mode="aspectFill" />
      <view class="header-mask" />
    </view>
    <view class="header-content">
      <image :src="user.avatar" class="avatar" />
      <view class="user-info">
        <view class="nickname">{{ user.nickname }}</view>
        <view class="introduction">{{ user.introduction }}</view>
        <view class="user-desc">
          <text class="desc-badge">武大法学本科生</text>
          <text class="desc-badge">📚有书就困</text>
          <text class="desc-badge">应当构成自始不能🤔</text>
        </view>
      </view>
      <button class="btn-edit" @tap="openModal">编辑资料</button>
    </view>

    <!-- 统计卡片 -->
    <view class="stats-card">
      <view class="stat-item">
        <text class="stat-value">{{ user.greenScore }}</text>
        <text class="stat-label">绿植总分</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ user.activityCount }}</text>
        <text class="stat-label">参与活动</text>
      </view>
    </view>

    <!-- 绿植记录 + 热力图 -->
    <view class="contrib-card large">
      <view class="contrib-header">
        <text class="contrib-title">绿植记录</text>
        <view class="btn-add" @tap="onAddContribution">
          <text>+</text>
        </view>
      </view>
      <view class="contrib-body large-body">
        <HeatmapCalendar :history="user.history" />
      </view>
    </view>

    <!-- 编辑资料弹窗 -->
    <EditProfileModal v-model:visible="showModal" @saved="onProfileSaved" />

    <!-- 退出登录按钮（底部固定） -->
    <button class="btn-logout" @tap="logout">退出登录</button>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/user.js'
import HeatmapCalendar from '@/pages/my/HeatmapCalendar.vue'
import EditProfileModal from '@/pages/my/EditProfileModal.vue'

const userStore = useUserStore()
const user = userStore.userInfo
const showModal = ref(false)

function openModal() { showModal.value = true }
function onAddContribution() { /* 可扩展 */ }
function onProfileSaved(updated) {
  userStore.userInfo = { ...userStore.userInfo, ...updated }
}
function logout() {
  userStore.logout && userStore.logout();
  uni.removeStorageSync('token')
  uni.removeStorageSync('userInfo')
  uni.removeStorageSync('history')
  uni.reLaunch({ url: '/pages/login/login' })
}
</script>

<style scoped>
.my-page {
  position: relative;
  background: #F5F6FA;
  min-height: 100vh;
  padding-bottom: 120rpx;
}
.header-bg {
  width: 100%;
  height: 240rpx;
  position: relative;
  overflow: hidden;
}
.header-bg-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: blur(10px) brightness(0.7);
}
.header-mask {
  position: absolute;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.32);
}
.header-content {
  position: absolute;
  top: 100rpx;
  left: 0; right: 0;
  display: flex;
  align-items: flex-end;
  padding: 0 32rpx;
  z-index: 2;
}
.avatar {
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  border: 4rpx solid #fff;
  background: #fff;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.08);
}
.user-info {
  margin-left: 18rpx;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.nickname {
  font-size: 36rpx;
  color: #fff;
  font-weight: bold;
  margin-bottom: 4rpx;
  letter-spacing: 1rpx;
}
.introduction {
  font-size: 22rpx;
  color: rgba(255,255,255,0.95);
  margin-bottom: 6rpx;
  letter-spacing: 0.5rpx;
}
.user-desc {
  display: flex;
  gap: 8rpx;
  flex-wrap: wrap;
  margin-bottom: 2rpx;
}
.desc-badge {
  background: rgba(255,255,255,0.22);
  color: #fff;
  font-size: 18rpx;
  border-radius: 12rpx;
  padding: 2rpx 12rpx;
  margin-right: 4rpx;
  display: flex;
  align-items: center;
}
.btn-edit {
  background: #fff;
  color: #007AFF;
  padding: 8rpx 28rpx;
  border-radius: 22rpx;
  font-size: 24rpx;
  margin-left: 18rpx;
  font-weight: 500;
  box-shadow: 0 2rpx 8rpx rgba(0,122,255,0.08);
}
.stats-card {
  background: #fff;
  margin: 32rpx 24rpx 0 24rpx;
  border-radius: 18rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx 0;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.07);
  position: relative;
}
.stat-item {
  text-align: center;
  flex: 1;
}
.stat-value {
  font-size: 34rpx;
  color: #222;
  font-weight: bold;
  margin-bottom: 4rpx;
}
.stat-label {
  font-size: 20rpx;
  color: #888;
  margin-top: 6rpx;
  display: block;
}
.stats-card .stat-item:not(:last-child) {
  border-right: 1rpx solid #F0F0F0;
}
.contrib-card.large {
  background: #fff;
  margin: 28rpx 24rpx 0 24rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.07);
  padding: 0 0 28rpx 0;
}
.contrib-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 26rpx 26rpx 0 26rpx;
}
.contrib-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #222;
  letter-spacing: 1rpx;
}
.btn-add {
  width: 44rpx;
  height: 44rpx;
  background: #E8F5E9;
  color: #44A340;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  font-weight: bold;
  box-shadow: 0 2rpx 8rpx rgba(68,163,64,0.08);
}
.contrib-body.large-body {
  padding: 0 18rpx;
}
.btn-logout {
  background: #007AFF;
  color: #fff;
  padding: 14rpx 0;
  border-radius: 22rpx;
  font-size: 26rpx;
  position: fixed;
  bottom: 32rpx;
  left: 32rpx;
  right: 32rpx;
  font-weight: 500;
  box-shadow: 0 2rpx 12rpx rgba(0,122,255,0.10);
}
</style>