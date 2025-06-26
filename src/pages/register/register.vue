<template>
  <view class="register-container">
    <!-- 绿色背景图片 -->
    <image src="/static/login/jwhbackground.png" class="background-image" />

    <!-- 绿叶装饰背景 -->
    <image src="/static/login/leaf.png" class="leaves-overlay" />

    <!-- 绿叶装饰背景 -->
    <image src="/static/login/leaf1.png" class="leaves-overlay2" />

    <!-- 返回按钮 -->
    <view class="return-button" @click="goBack">
      <image src="/static/login/return1.png" class="return-icon" />
    </view>

    <!-- 表单容器 -->
    <view class="form-container">
      <!-- 账号输入框 -->
      <view class="input-group">
        <view class="input-header">
          <image src="/static/login/usericon.png" class="input-icon" />
          <text class="input-label">账号</text>
        </view>
        <view class="input-content">
          <input
            type="text"
            class="input-field"
            placeholder="请输入账号"
            v-model="account"
          />
        </view>
      </view>

      <!-- 密码输入框 -->
      <view class="input-group">
        <view class="input-header">
          <image src="/static/login/passwordicon.png" class="input-icon" />
          <text class="input-label">密码</text>
        </view>
        <view class="input-content">
          <input
            :type="showPassword ? 'text' : 'password'"
            class="input-field"
            placeholder="请设置6-16位密码"
            v-model="password"
          />
          <view class="password-toggle" @click="togglePassword">
            <image
              :src="
                showPassword
                  ? '/static/login/hideicon.png'
                  : '/static/login/showicon.png'
              "
              class="toggle-icon"
            />
          </view>
        </view>
      </view>

      <!-- 确认密码输入框 -->
      <view class="input-group">
        <view class="input-header">
          <image src="/static/login/passwordicon.png" class="input-icon" />
          <text class="input-label">确认密码</text>
        </view>
        <view class="input-content">
          <input
            :type="showConfirmPassword ? 'text' : 'password'"
            class="input-field"
            placeholder="请再次输入密码"
            v-model="confirmPassword"
          />
          <view class="password-toggle" @click="toggleConfirmPassword">
            <image
              :src="
                showConfirmPassword
                  ? '/static/login/hideicon.png'
                  : '/static/login/showicon.png'
              "
              class="toggle-icon"
            />
          </view>
        </view>
      </view>

      <!-- 联系电话输入框 -->
      <view class="input-group">
        <view class="input-header">
          <image src="/static/login/phoneicon.png" class="input-icon" />
          <text class="input-label">联系电话</text>
        </view>
        <view class="input-content">
          <input
            type="tel"
            class="input-field"
            placeholder="请输入联系电话"
            v-model="phone"
          />
        </view>
      </view>

      <!-- 注册按钮 -->
      <view class="register-button" @click="handleRegister">
        <image src="/static/login/registerbutton.png" class="register-btn-bg" />
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      account: "", // 账号
      password: "", // 密码
      confirmPassword: "", // 确认密码
      phone: "", // 联系电话
      showPassword: false, // 是否显示密码
      showConfirmPassword: false, // 是否显示确认密码
    };
  },
  methods: {
    // 返回上一页
    goBack() {
      uni.navigateBack({
        delta: 1,
      });
    },

    // 切换密码显示状态
    togglePassword() {
      this.showPassword = !this.showPassword;
    },

    // 切换确认密码显示状态
    toggleConfirmPassword() {
      this.showConfirmPassword = !this.showConfirmPassword;
    },

    // 图片加载错误处理
    onImageError() {
      console.log("注册按钮图片加载失败");
    },

    // 处理注册
    handleRegister() {
      // 验证输入
      if (!this.account.trim()) {
        uni.showToast({
          title: "请输入账号",
          icon: "none",
        });
        return;
      }

      if (!this.password.trim()) {
        uni.showToast({
          title: "请设置密码",
          icon: "none",
        });
        return;
      }

      if (this.password.length < 6 || this.password.length > 16) {
        uni.showToast({
          title: "密码长度应为6-16位",
          icon: "none",
        });
        return;
      }

      if (!this.confirmPassword.trim()) {
        uni.showToast({
          title: "请确认密码",
          icon: "none",
        });
        return;
      }

      if (this.password !== this.confirmPassword) {
        uni.showToast({
          title: "两次输入的密码不一致",
          icon: "none",
        });
        return;
      }

      if (!this.phone.trim()) {
        uni.showToast({
          title: "请输入联系电话",
          icon: "none",
        });
        return;
      }

      // 验证手机号格式
      const phoneRegex = /^1[3-9]\d{9}$/;
      if (!phoneRegex.test(this.phone)) {
        uni.showToast({
          title: "请输入正确的手机号",
          icon: "none",
        });
        return;
      }

      // 这里添加注册逻辑
      uni.showLoading({
        title: "注册中...",
      });

      // 模拟注册请求
      uni.request({
        url: "http://localhost:8080/api/register",
        method: "POST",
        data: {
          username: this.account,
          password: this.password,
          userType: "normal",
        },
        success(res) {
          console.log("注册成功！", res.data);
          uni.hideLoading();
          uni.showToast({
            title: "注册成功",
            icon: "success",
          });

          // 注册成功后返回登录页面
          setTimeout(() => {
            uni.navigateBack({
              delta: 1,
            });
          }, 1500);
        },
        fail(err) {
          console.error("失败！", err);
        },
      });
    },
  },
};
</script>

<style lang="scss" scoped>
/* 重置页面默认样式 */

.register-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  overflow-x: hidden;
  box-sizing: border-box;
}

/* 绿色背景图片 */
.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;
}

/* 绿叶装饰背景 */
.leaves-overlay {
  position: absolute;
  bottom: 0; /* 改为底部对齐 */
  left: 0;
  width: 100vw; /* 使用视口宽度单位 */
  height: 100vw; /* 使用相同的视口宽度单位，确保长宽相等 */
  object-fit: cover;
  object-position: bottom; /* 确保图片底部与容器底部对齐 */
  z-index: 2;
}

.leaves-overlay2 {
  position: absolute;
  right: 0; /* 改为右侧对齐 */
  width: 60vw; /* 使用视口宽度单位 */
  height: 60vw; /* 使用相同的视口宽度单位，确保长宽相等 */
  object-fit: cover;
  z-index: 2;
}

/* 返回按钮 */
.return-button {
  position: absolute;
  top: 80rpx;
  left: 40rpx;
  z-index: 20;
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
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%; /* 调整为屏幕宽度的85% */
  padding: 60rpx 40rpx;
  z-index: 10;
  box-sizing: border-box;
}

/* 输入框组 */
.input-group {
  position: relative;
  display: flex;
  flex-direction: column;
  margin-bottom: 2.5vh; /* 进一步减少间距 */
  padding-bottom: 0.5vh; /* 减少底部内边距 */
  border-bottom: 0.15vh solid #000000; /* 黑色底部边框，粗细减半 */
}

.input-header {
  display: flex;
  align-items: center;
  margin-bottom: 0.2vh; /* 减少标签和占位符之间的间距 */
}

.input-icon {
  width: 2.5vh; /* 进一步缩小图标 */
  height: 2.5vh;
  margin-right: 1.2vh; /* 减少右边距 */
  flex-shrink: 0;
}

.input-label {
  font-size: 1.8vh; /* 进一步缩小标签字体 */
  color: #333333;
  font-weight: bold;
  line-height: 1.1; /* 减少行高 */
}

.input-content {
  flex: 1;
  display: flex;
  align-items: center;
  margin-left: 3.7vh; /* 调整对齐位置 */
}

.input-field {
  flex: 1;
  height: 3vh; /* 进一步缩小高度 */
  font-size: 1.6vh; /* 进一步缩小字体 */
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;

  /* 自定义placeholder样式 */
  &::placeholder {
    color: #999999;
    font-size: 1.4vh; /* 进一步缩小占位符字体 */
    opacity: 1;
  }

  &::-webkit-input-placeholder {
    color: #999999;
    font-size: 1.4vh;
    opacity: 1;
  }

  &::-moz-placeholder {
    color: #999999;
    font-size: 1.4vh;
    opacity: 1;
  }
}

.password-toggle {
  width: 2.5vh; /* 与图标大小保持一致 */
  height: 2.5vh;
  margin-left: 1.2vh; /* 减少左边距 */
  flex-shrink: 0;
}

.toggle-icon {
  width: 100%;
  height: 100%;
}

/* 注册按钮 */
.register-button {
  position: relative;
  width: 65%; /* 70% × 80% = 56% */
  height: 6.5vh; /* 调整为原本的三分之二：8vh × 2/3 = 5.33vh */
  margin: 8vh auto 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 15; /* 提高层级，确保在绿叶之上 */
}

.register-btn-bg {
  width: 100%;
  height: 100%;
}

.register-btn-text {
  position: absolute;
  color: #ffffff;
  font-size: 2.5vh;
  font-weight: bold;
  z-index: 16;
  pointer-events: none; /* 确保文字不阻挡点击事件 */
}
</style>
