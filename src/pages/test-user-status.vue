<template>
  <view class="status-container">
    <view class="header">
      <text class="title">用户状态测试页面</text>
    </view>
    
    <!-- 用户信息展示 -->
    <view class="info-section">
      <view class="info-item">
        <text class="label">登录状态：</text>
        <text class="value" :class="{ 'logged-in': userStore.userInfo.isLoggedIn }">
          {{ userStore.userInfo.isLoggedIn ? '已登录' : '未登录' }}
        </text>
      </view>
      
      <view class="info-item">
        <text class="label">用户ID：</text>
        <text class="value">{{ userStore.userInfo.userId || '未设置' }}</text>
      </view>
      
      <view class="info-item">
        <text class="label">用户昵称：</text>
        <text class="value">{{ userStore.userInfo.nickname }}</text>
      </view>
      
      <view class="info-item">
        <text class="label">用户介绍：</text>
        <text class="value">{{ userStore.userInfo.introduction }}</text>
      </view>
      
      <view class="info-item">
        <text class="label">用户类型：</text>
        <text class="value user-type" :class="userStore.userInfo.user_type">
          {{ userStore.getUserTypeLabel() }}
        </text>
      </view>
      
      <view class="info-item">
        <text class="label">绿分：</text>
        <text class="value">{{ userStore.userInfo.green_score }}</text>
      </view>
    </view>
    
    <!-- 权限测试 -->
    <view class="permission-section">
      <text class="section-title">权限测试</text>
      
      <view class="permission-item">
        <text class="permission-label">普通用户权限：</text>
        <text class="permission-value" :class="{ 'has-permission': userStore.checkUserPermission('normal') }">
          {{ userStore.checkUserPermission('normal') ? '✓ 有权限' : '✗ 无权限' }}
        </text>
      </view>
      
      <view class="permission-item">
        <text class="permission-label">居委会权限：</text>
        <text class="permission-value" :class="{ 'has-permission': userStore.checkUserPermission('committee') }">
          {{ userStore.checkUserPermission('committee') ? '✓ 有权限' : '✗ 无权限' }}
        </text>
      </view>
      
      <view class="permission-item">
        <text class="permission-label">管理员权限：</text>
        <text class="permission-value" :class="{ 'has-permission': userStore.checkUserPermission('admin') }">
          {{ userStore.checkUserPermission('admin') ? '✓ 有权限' : '✗ 无权限' }}
        </text>
      </view>
    </view>
    
    <!-- 操作按钮 -->
    <view class="action-section">
      <button class="action-btn refresh-btn" @click="refreshData">刷新数据</button>
      <button class="action-btn logout-btn" @click="handleLogout">退出登录</button>
    </view>
    
    <!-- 登录入口 -->
    <view class="login-section">
      <text class="section-title">登录入口</text>
      <button class="login-btn normal-login" @click="goToLogin('normal')">普通用户登录</button>
      <button class="login-btn committee-login" @click="goToLogin('committee')">居委会登录</button>
      <button class="login-btn admin-login" @click="goToLogin('admin')">管理员登录</button>
    </view>
  </view>
</template>

<script>
import { useUserStore } from '@/stores/user.js'

export default {
  name: 'TestUserStatus',
  setup() {
    const userStore = useUserStore()
    return {
      userStore
    }
  },
  onShow() {
    // 页面显示时刷新数据
    this.refreshData()
  },
  methods: {
    // 刷新数据
    refreshData() {
      // 从本地存储重新加载用户信息
      this.userStore.loadUserFromStorage()
      console.log('数据已刷新', this.userStore.userInfo)
    },
    
    // 处理退出登录
    handleLogout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            this.userStore.logoutUser()
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
          }
        }
      })
    },
    
    // 跳转到登录页面
    goToLogin(type) {
      let url = ''
      switch (type) {
        case 'normal':
          url = '/pages/login/login'
          break
        case 'committee':
          url = '/pages/login/jwhlogin'
          break
        case 'admin':
          url = '/pages/login/admin-login'
          break
      }
      
      if (url) {
        uni.navigateTo({ url })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.status-container {
  padding: 40rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  text-align: center;
  margin-bottom: 40rpx;
  
  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
}

.info-section, .permission-section, .action-section, .login-section {
  background-color: white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.info-item, .permission-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #eee;
  
  &:last-child {
    border-bottom: none;
  }
}

.label, .permission-label {
  font-size: 28rpx;
  color: #666;
  font-weight: 500;
}

.value, .permission-value {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
}

.value.logged-in {
  color: #4CAF50;
}

.user-type {
  &.normal {
    color: #2196F3;
  }
  
  &.committee {
    color: #FF9800;
  }
  
  &.admin {
    color: #F44336;
  }
}

.permission-value {
  &.has-permission {
    color: #4CAF50;
  }
  
  &:not(.has-permission) {
    color: #F44336;
  }
}

.action-section {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 12rpx;
  font-size: 28rpx;
  font-weight: bold;
  border: none;
  cursor: pointer;
}

.refresh-btn {
  background-color: #2196F3;
  color: white;
}

.logout-btn {
  background-color: #F44336;
  color: white;
}

.login-section {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.login-btn {
  height: 80rpx;
  border-radius: 12rpx;
  font-size: 28rpx;
  font-weight: bold;
  border: none;
  cursor: pointer;
  color: white;
}

.normal-login {
  background-color: #2196F3;
}

.committee-login {
  background-color: #FF9800;
}

.admin-login {
  background-color: #F44336;
}
</style> 