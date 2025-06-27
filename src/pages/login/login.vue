<template>
  <view class="login-container">
    <!-- 上方背景区域 -->
    <view class="background-area" :class="backgroundStyle">
      <image 
        class="background-image" 
        src="/static/login/background.png" 
        mode="aspectFill"
      />
      <view class="gradient-overlay" :class="maskStyle"></view>
    </view>
    
    <!-- 返回按钮 -->
    <view class="return-button" @click="goBack">
      <image src="/static/login/returnbutton.png" class="return-icon" />
    </view>
    
    <!-- 登录表单区域 -->
    <view class="form-container">
      <!-- 账号输入框 -->
      <view class="input-group">
        <image src="/static/login/usericon.png" class="input-icon" />
        <input 
          v-model="account"
          type="text" 
          placeholder="请输入账号" 
          class="input-field"
          placeholder-style="color: #666666; font-size: 2.5vh; font-weight: 500;"
        />
      </view>
      
      <!-- 密码输入框 -->
      <view class="input-group">
        <image src="/static/login/passwordicon.png" class="input-icon" />
        <input 
          v-model="password"
          :type="showPassword ? 'text' : 'password'" 
          placeholder="请输入密码" 
          class="input-field"
          placeholder-style="color: #666666; font-size: 2.5vh; font-weight: 500;"
        />
        <view class="password-toggle" @click="togglePassword">
          <image 
            :src="showPassword ? '/static/login/showicon.png' : '/static/login/hideicon.png'" 
            class="toggle-icon" 
          />
        </view>
      </view>
      
      <!-- 找回密码 -->
      <view class="forgot-password" @click="forgotPassword">
        <text class="forgot-text">找回密码</text>
      </view>
      
      <!-- 登录按钮 -->
      <view class="login-button" @click="handleLogin">
        <image src="/static/login/loginbutton.png" class="login-btn-bg" />
      </view>
      
      <!-- 注册链接 -->
      <view class="register-link">
        <text class="register-text">没有账号？点击</text>
        <text class="register-action" @click="goToRegister">注册</text>
        <text class="register-text">申请账号</text>
      </view>
      
      <!-- 用户协议 -->
      <view class="agreement-section">
        <view class="checkbox-container" @click="toggleAgreement">
          <view class="checkbox" :class="{ 'checked': isAgreed }">
            <view v-if="isAgreed" class="checkbox-inner"></view>
          </view>
          <text class="agreement-text">我已阅读并同意</text>
          <text class="agreement-link" @click.stop="showUserAgreement">《用户协议》</text>
          <text class="agreement-text">和</text>
          <text class="agreement-link" @click.stop="showPrivacyPolicy">《隐私政策》</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { useUserStore } from '@/stores/user.js'

export default {
  name: 'Login',
  setup() {
    const useUserStoreInstance = useUserStore()
    return {
      useUserStore: () => useUserStoreInstance
    }
  },
  data() {
    return {
      account: '', // 账号
      password: '', // 密码
      showPassword: false, // 是否显示密码
      isAgreed: false, // 是否同意用户协议
      backgroundStyle: 'default', // 背景样式：default, blur, dark, gradient
      maskStyle: 'center-medium' // 遮罩样式：center-small, center-medium, center-large, ellipse
    }
  },
  methods: {
    // 返回上一页
    goBack() {
      uni.navigateBack({
        delta: 1
      })
    },
    
    // 切换密码显示状态
    togglePassword() {
      this.showPassword = !this.showPassword
    },
    
    // 找回密码
    forgotPassword() {
      uni.showToast({
        title: '找回密码功能待开发',
        icon: 'none'
      })
    },
    
    // 处理登录
    handleLogin() {
      // 验证输入
      if (!this.account.trim()) {
        uni.showToast({
          title: '请输入账号',
          icon: 'none'
        })
        return
      }
      
      if (!this.password.trim()) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return
      }
      
      if (!this.isAgreed) {
        uni.showToast({
          title: '请先同意用户协议',
          icon: 'none'
        })
        return
      }
      
      // 这里添加登录逻辑
      uni.showLoading({
        title: '登录中...'
      })
      
      // 模拟登录请求
      setTimeout(() => {
        uni.hideLoading()
        
        // 使用用户状态管理 - 设置为普通用户
        const userStore = this.useUserStore()
        const userData = {
          userId: this.account,
          nickname: this.account,
          introduction: "普通用户",
          // 其他用户信息可以从后端获取
        }
        
        // 登录并设置用户类型为 normal
        userStore.loginUser(userData, 'normal')
        
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
      }, 2000)
    },
    
    // 跳转到注册页面
    goToRegister() {
      uni.navigateTo({
        url: '/pages/register/register'
      })
    },
    
    // 切换协议同意状态
    toggleAgreement() {
      this.isAgreed = !this.isAgreed
    },
    
    // 显示用户协议
    showUserAgreement() {
      uni.showModal({
        title: '用户协议',
        content: '这里是用户协议的详细内容...',
        showCancel: false
      })
    },
    
    // 显示隐私政策
    showPrivacyPolicy() {
      uni.showModal({
        title: '隐私政策',
        content: '这里是隐私政策的详细内容...',
        showCancel: false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
/* 重置页面默认样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.login-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  background-color: #FFFFFF; /* 纯白色背景在最底层 */
  overflow-x: hidden; /* 只隐藏水平滚动 */
  box-sizing: border-box; /* 确保padding和border计算在内 */
}

/* 上方背景区域 */
.background-area {
  position: relative;
  width: 100%;
  height: 100vh; /* 缩小为原来的三分之二：66.67vh * 2/3 = 44.44vh */
  overflow: hidden;
  box-sizing: border-box;
}

.background-image {
  position: absolute;
  top: 0; /* 图片顶部与屏幕顶部对齐 */
  left: 0;
  right: 0; /* 添加right: 0 */
  width: 100%;
  height: 66.67vh; /* 缩小为原来的三分之二 */
  object-fit: cover; /* 保持图片比例，填满容器 */
  object-position: top; /* 确保图片从顶部开始显示 */
  z-index: 1; /* 背景图在纯白色背景之上 */
  max-width: 100%; /* 确保图片不会超出容器 */
}

.gradient-overlay {
  position: absolute;
  top: 33.33vh; /* 从背景区域的3/4位置开始 */
  left: 0;
  right: 0;
  width: 100%;
  height: 33.33vh; /* 调整渐变高度为背景区域的三分之二 */
  background: -webkit-linear-gradient(bottom, 
    #ffffff 0%, 
    rgba(255, 255, 255, 0.98) 15%,
    rgba(255, 255, 255, 0.92) 30%,
    rgba(255, 255, 255, 0.8) 45%,
    rgba(255, 255, 255, 0.6) 60%,
    rgba(255, 255, 255, 0.35) 75%,
    rgba(255, 255, 255, 0.1) 90%,
    transparent 100%
  );
  background: linear-gradient(to top, 
    #ffffff 0%, 
    rgba(255, 255, 255, 0.98) 15%,
    rgba(255, 255, 255, 0.92) 30%,
    rgba(255, 255, 255, 0.8) 45%,
    rgba(255, 255, 255, 0.6) 60%,
    rgba(255, 255, 255, 0.35) 75%,
    rgba(255, 255, 255, 0.1) 90%,
    transparent 100%
  );
  z-index: 2; /* 渐变层在背景图之上，用于创建渐隐效果 */
}

/* 背景样式变体 */
.background-area.blur .background-image {
  filter: blur(8rpx);
}

.background-area.dark .gradient-overlay {
  background: linear-gradient(to top, 
    rgba(0, 0, 0, 0.8) 0%, 
    rgba(0, 0, 0, 0.6) 20%,
    rgba(0, 0, 0, 0.4) 40%,
    rgba(0, 0, 0, 0.2) 70%,
    rgba(0, 0, 0, 0) 100%
  );
}

.background-area.gradient .gradient-overlay {
  background: linear-gradient(to top, 
    rgba(76, 175, 80, 0.9) 0%, 
    rgba(76, 175, 80, 0.7) 20%,
    rgba(76, 175, 80, 0.5) 40%,
    rgba(76, 175, 80, 0.3) 70%,
    rgba(76, 175, 80, 0) 100%
  );
}

.background-area.soft .gradient-overlay {
  height: 400rpx;
  background: linear-gradient(to top, 
    rgba(248, 250, 252, 1) 0%, 
    rgba(248, 250, 252, 0.95) 15%,
    rgba(248, 250, 252, 0.8) 30%,
    rgba(248, 250, 252, 0.6) 50%,
    rgba(248, 250, 252, 0.3) 70%,
    rgba(248, 250, 252, 0.1) 85%,
    rgba(248, 250, 252, 0) 100%
  );
}

/* 返回按钮 */
.return-button {
  position: absolute;
  top: 80rpx;
  left: 40rpx;
  z-index: 20; /* 返回按钮在所有内容之上 */
  width: 60rpx;
  height: 60rpx;
}

.return-icon {
  width: 100%;
  height: 100%;
}

/* 表单容器 */
.form-container {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0; /* 添加right: 0确保完全覆盖 */
  width: 100%;
  padding: 80rpx 60rpx 120rpx;
  background-color: transparent; /* 改为透明，让底层白色背景显示 */
  z-index: 10; /* 表单内容在最上层 */
  box-sizing: border-box; /* 确保padding计算正确 */
}

/* 输入框组 */
.input-group {
  position: relative;
  display: flex;
  align-items: center;
  margin-bottom: 4vh; /* 使用视口高度单位，固定相对屏幕的间距 */
  padding-bottom: 0.8vh; /* 减少底部内边距，让边框离文本更近 */
  border-bottom: 0.3vh solid #999999; /* 边框高度相对屏幕固定 */
  transition: border-color 0.3s ease; /* 添加过渡效果 */
  
  &:focus-within {
    border-bottom-color: #007AFF; /* 聚焦时变为蓝色 */
  }
}

.input-icon {
  width: 3vh; /* 图标宽度相对屏幕固定 */
  height: 3vh; /* 图标高度相对屏幕固定 */
  margin-right: 1.5vh; /* 右边距相对屏幕固定 */
  flex-shrink: 0;
}

.input-field {
  flex: 1;
  height: 6vh; /* 输入框高度相对屏幕固定 */
  font-size: 2.5vh; /* 字体大小相对屏幕固定 */
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
  font-weight: 500; /* 增加字体重量 */
  
  /* 自定义placeholder样式（备用方案） */
  &::placeholder {
    color: #666666;
    font-weight: 500;
    opacity: 1; /* 确保placeholder完全不透明 */
  }
  
  &::-webkit-input-placeholder {
    color: #666666;
    font-weight: 500;
    opacity: 1;
  }
  
  &::-moz-placeholder {
    color: #666666;
    font-weight: 500;
    opacity: 1;
  }
}

.password-toggle {
  width: 3vh; /* 密码切换按钮宽度相对屏幕固定 */
  height: 3vh; /* 密码切换按钮高度相对屏幕固定 */
  margin-left: 1.5vh; /* 左边距相对屏幕固定 */
  flex-shrink: 0;
}

.toggle-icon {
  width: 100%;
  height: 100%;
}

/* 找回密码 */
.forgot-password {
  display: flex;
  justify-content: flex-start; /* 改为左对齐 */
  margin-bottom: 4vh; /* 底部边距相对屏幕固定 */
}

.forgot-text {
  font-size: 2.2vh; /* 字体大小相对屏幕固定 */
  color: #666666;
}

/* 登录按钮 */
.login-button {
  position: relative;
  width: 60%; /* 改为屏幕宽度的五分之三 */
  height: 7vh; /* 登录按钮高度相对屏幕固定 */
  margin: 0 auto 3vh auto; /* 水平居中，底部边距相对屏幕固定 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-btn-bg {
  width: 100%;
  height: 100%;
}

/* 注册链接 */
.register-link {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 4vh; /* 底部边距相对屏幕固定 */
}

.register-text {
  font-size: 2.2vh; /* 字体大小相对屏幕固定 */
  color: #999999;
  margin-right: 0.8vh; /* 右边距相对屏幕固定 */
}

.register-action {
  font-size: 2.2vh; /* 字体大小相对屏幕固定 */
  color: #1E90FF; /* 深蓝色 */
  font-weight: bold;
  cursor: pointer; /* 添加手型光标 */
  
  /* 添加点击效果 */
  &:active {
    opacity: 0.7;
    transform: scale(0.98);
  }
}

/* 用户协议 */
.agreement-section {
  display: flex;
  justify-content: center;
}

.checkbox-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  justify-content: center;
}

.checkbox {
  position: relative;
  width: 2.5vh; /* 复选框宽度相对屏幕固定 */
  height: 2.5vh; /* 复选框高度相对屏幕固定 */
  border: 0.15vh solid #CCCCCC; /* 边框宽度相对屏幕固定 */
  border-radius: 50%;
  margin-right: 1.2vh; /* 右边距相对屏幕固定 */
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  
  &.checked {
    border-color: #4CAF50;
    background-color: #4CAF50;
  }
}

.checkbox-inner {
  width: 1.2vh; /* 内部圆点宽度相对屏幕固定 */
  height: 1.2vh; /* 内部圆点高度相对屏幕固定 */
  background-color: #FFFFFF;
  border-radius: 50%;
}

.agreement-text {
  font-size: 1.8vh; /* 字体大小相对屏幕固定 */
  color: #999999;
  margin-right: 0.6vh; /* 右边距相对屏幕固定 */
}

.agreement-link {
  font-size: 1.8vh; /* 字体大小相对屏幕固定 */
  color: #1E90FF; /* 深蓝色 */
  margin-right: 0.6vh; /* 右边距相对屏幕固定 */
  text-decoration: underline;
}
</style> 