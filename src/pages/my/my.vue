<template>
  <view class="my-page">
    <!-- 头部背景 -->
    <view class="header-section">
      <image v-if="userInfo.avatar" :src="userInfo.avatar" class="header-bg-img" mode="aspectFill"/>
      <view v-else class="header-bg-default"/>
      <view class="header-mask"/>

      <!-- 头部内容 -->
      <view class="header-content">
        <view class="header-top">
          <image v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar-small" mode="aspectFill"/>
          <view v-else class="avatar-placeholder-small">🌱</view>
          <view class="header-text">
            <text class="nickname">{{ userInfo.nickname }}</text>
            <text class="intro-small">{{ userInfo.introduction }}</text>
          </view>
        </view>
        <view class="header-bottom">
          <view class="header-stats-row">
            <view class="stat-item-small">
              <text class="value-small">{{ userInfo.green_score }}</text>
              <text class="label-small">贡献值</text>
            </view>
            <view class="stat-item-small">
              <text class="value-small">{{ userInfo.activity_participantion_num }}</text>
              <text class="label-small">参与活动</text>
            </view>
          </view>
          <button class="btn-edit" @tap="openEditModal">编辑资料</button>
        </view>
      </view>
    </view>

    <!-- 内容面板 -->
    <view class="content-panel">
      <view class="panel-header">
        <text class="panel-title">我的贡献</text>
        <button class="btn-add" @tap="addContribution">+</button>
      </view>
      <text class="panel-subtitle">点击查看我的贡献～</text>

      <view class="calendar-box">
        <view class="calendar-header">
          <button class="nav-btn" @tap="previousMonth">‹</button>
          <text class="month-year">{{ currentMonthYear }}</text>
          <button class="nav-btn" @tap="nextMonth">›</button>
        </view>
        <view class="divider"/>
        <view class="calendar-grid">
          <text v-for="(day, i) in weekdays" :key="i" class="weekday">{{ day }}</text>
        </view>
        <view class="divider"/>
        <view class="calendar-dates">
          <view v-for="(_, i) in emptyDays" :key="`e${i}`" class="calendar-day empty"/>
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

      <view class="logout-section">
        <button class="btn-logout" @tap="logout">退出登录</button>
      </view>
    </view>

    <!-- 编辑弹窗 -->
    <view v-if="showEditModal" class="modal-overlay" @tap="closeEditModal">
      <view class="modal-content" @tap.stop>
        <view class="modal-header">
          <text>编辑资料</text>
          <button class="close-btn" @tap="closeEditModal">×</button>
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

<script>
export default {
  data() {
    return {
      userInfo: {
        nickname: '',
        introduction: '',
        green_score: 0,
        activity_participantion_num: 0,
        avatar: '',
        history: []
      },
      showEditModal: false,
      editForm: { nickname: '', introduction: '' },
      currentDate: new Date(),
      weekdays: ['S','M','T','W','T','F','S']
    }
  },
  computed: {
    currentMonthYear() {
      const y = this.currentDate.getFullYear()
      const m = this.currentDate.getMonth()
      const names = ['JANUARY','FEBRUARY','MARCH','APRIL','MAY','JUNE','JULY','AUGUST','SEPTEMBER','OCTOBER','NOVEMBER','DECEMBER']
      return `${names[m]} ${y}`
    },
    daysInMonth() {
      const y = this.currentDate.getFullYear()
      const m = this.currentDate.getMonth()
      return new Date(y, m+1, 0).getDate()
    },
    emptyDays() {
      const first = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth(),1).getDay()
      return Array(first).fill(0)
    }
  },
  onLoad() {
    const stored = uni.getStorageSync('userInfo')
    if (stored) this.userInfo = stored
  },
  methods: {
    openEditModal() {
      this.editForm = { nickname: this.userInfo.nickname, introduction: this.userInfo.introduction }
      this.showEditModal = true
    },
    closeEditModal() { this.showEditModal = false },
    saveProfile() {
      this.userInfo = { ...this.userInfo, ...this.editForm }
      uni.setStorageSync('userInfo', this.userInfo)
      this.closeEditModal()
    },
    chooseAvatar() {
      uni.chooseImage({ count: 1, success: res => {
        this.userInfo.avatar = res.tempFilePaths[0]
        uni.setStorageSync('userInfo', this.userInfo)
      } })
    },
    getDayClass(idx) {
      const h = this.userInfo.history || []
      if (idx >= h.length) return 'level-0'
      return `level-${Math.min(Math.max(h[idx],0),4)}`
    },
    onDayClick(day, idx) {
      console.log(`第${day}天，贡献:${this.userInfo.history[idx]||0}`)
    },
    addContribution() {
      const idx = new Date().getDate() - 1
      const h = this.userInfo.history || []
      while (h.length <= idx) h.push(0)
      h[idx] = Math.min(h[idx] + 1, 4)
      this.userInfo.history = h
      this.userInfo.green_score = (this.userInfo.green_score || 0) + 10
      uni.setStorageSync('userInfo', this.userInfo)
    },
    previousMonth() { const d = new Date(this.currentDate); d.setMonth(d.getMonth()-1); this.currentDate = d },
    nextMonth() { const d = new Date(this.currentDate); d.setMonth(d.getMonth()+1); this.currentDate = d },
    logout() {
      uni.removeStorageSync('userInfo')
      this.userInfo = {}
    }
  }
}
</script>

<style scoped>
.my-page {
  background: #f5f6fa;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header-section {
  position: relative;
  height: 500rpx;
}

.header-bg-img,
.header-bg-default {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.header-bg-default {
  background: linear-gradient(135deg,#667eea 0%,#764ba2 100%);
}

.header-mask {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.4);
}

.header-content {
  position: absolute;
  inset: 0;
  padding: 32rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-top: 220rpx;
}

.header-top {
  display: flex;
  align-items: center;
}

.avatar-small {
  width: 96rpx;
  height: 96rpx;
  border-radius: 48rpx;
  overflow: hidden;
  border: 4rpx solid #fff;
}

.avatar-placeholder-small {
  width: 96rpx;
  height: 96rpx;
  background: #666;
  border-radius: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 48rpx;
}

.header-text {
  margin-left: 24rpx;
  flex: 1;
}

.nickname {
  font-size: 32rpx;
  color: #fff;
  font-weight: bold;
}

.intro-small {
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 8rpx;
}

.header-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-stats-row {
  display: flex;
  gap: 48rpx;
}

.value-small {
  font-size: 36rpx;
  color: #fff;
  font-weight: bold;
}

.label-small {
  font-size: 24rpx;
  color: rgba(255,255,255,0.9);
}

.btn-edit {
  background: rgba(0,0,0,0.3);
  color: #fff;
  padding: 16rpx 32rpx;
  border-radius: 24rpx;
}

.content-panel {
  flex: 1;
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 32rpx;
}

.content-panel .panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.content-panel .panel-title {
  font-size: 36rpx;
  color: #333;
  font-weight: bold;
}

.content-panel .btn-add {
  width: 56rpx;
  height: 56rpx;
  background: #00c853;
  color: #fff;
  border-radius: 28rpx;
  font-size: 40rpx;
}

.content-panel .panel-subtitle {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 48rpx;
}

.content-panel .calendar-box {
  flex: 1;
  background: #fff;
  border: 2rpx solid #e0e0e0;
  border-radius: 24rpx;
  padding: 32rpx;
  display: flex;
  flex-direction: column;
}

.content-panel .calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 24rpx;
}

.content-panel .nav-btn {
  font-size: 32rpx;
  color: #666;
}

.content-panel .divider {
  height: 2rpx;
  background: #e0e0e0;
  margin: 24rpx 0;
}

.content-panel .calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 16rpx;
}

.content-panel .weekday {
  font-size: 24rpx;
  color: #888;
  text-align: center;
}

.content-panel .calendar-dates {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8rpx;
  flex: 1;
}

.content-panel .calendar-day {
  font-size: 48rpx;
  text-align: center;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.content-panel .calendar-day.level-1 { background: #dcedc8; color: #33691e; }
.content-panel .calendar-day.level-2 { background: #aed581; color: #fff; }
.content-panel .calendar-day.level-3 { background: #7cb342; color: #fff; }
.content-panel .calendar-day.level-4 { background: #558b2f; color: #fff; }

.logout-section {
  display: flex;
  justify-content: center;
  margin-top: auto;
}

.btn-logout {
  background: #ff3b30;
  color: #fff;
  padding: 32rpx 72rpx;
  border-radius: 44rpx;
  font-size: 28rpx;
  font-weight: bold;
}
</style>
