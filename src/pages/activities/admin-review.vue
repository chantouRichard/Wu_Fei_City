<template>
  <view class="admin-review-container">
    <!-- 顶部标题栏 -->
    <view class="header">
      <view class="header-title">管理员审核</view>
    </view>

    <!-- 切换标签栏 -->
    <view class="tabs">
      <view
        class="tab"
        :class="{ active: currentTab === 0 }"
        @click="switchTab(0)"
      >
        正在审核
      </view>
      <view
        class="tab"
        :class="{ active: currentTab === 1 }"
        @click="switchTab(1)"
      >
        审核通过
      </view>
      <view
        class="tab"
        :class="{ active: currentTab === 2 }"
        @click="switchTab(2)"
      >
        审核未通过
      </view>
    </view>

    <!-- 审核列表 -->
    <view class="review-list">
      <!-- 正在审核 -->
      <view v-if="currentTab === 0">
        <view class="review-item" v-for="item in pendingItems" :key="item.id">
          <!-- 活动信息卡片 -->
          <view class="activity-card">
            <view class="activity-header">
              <image
                class="activity-avatar"
                :src="item.avatar"
                mode="aspectFill"
              />
              <view class="activity-info">
                <view class="activity-organizer">{{ item.organizer }}</view>
                <view class="activity-phone"
                  >联系电话：{{ item.phone || "1271..." }}</view
                >
              </view>
            </view>

            <!-- 审核按钮 -->
            <view class="review-actions">
              <button class="reject-btn" @click="handleReject(item.id)">
                不通过
              </button>
              <button class="approve-btn" @click="handleApprove(item.id)">
                通过
              </button>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-if="pendingItems.length === 0" class="empty-state">
          <image src="/static/activity/none.png" class="empty-icon" />
          <text class="empty-text">暂无待审核活动</text>
        </view>
      </view>

      <!-- 审核通过 -->
      <view v-if="currentTab === 1">
        <view class="review-item" v-for="item in approvedItems" :key="item.id">
          <view class="activity-card approved">
            <view class="activity-header">
              <image
                class="activity-avatar"
                :src="item.avatar"
                mode="aspectFill"
              />
              <view class="activity-info">
                <view class="activity-organizer">{{ item.organizer }}</view>
                <view class="activity-bottom">
                  <view class="activity-responsible"
                    >负责人：{{ item.responsible || "张*君" }}</view
                  >
                  <view class="arrow-right"></view>
                </view>
              </view>
            </view>
          </view>
        </view>

        <view v-if="approvedItems.length === 0" class="empty-state">
          <image src="/static/activity/none.png" class="empty-icon" />
          <text class="empty-text">暂无已通过活动</text>
        </view>
      </view>

      <!-- 审核未通过 -->
      <view v-if="currentTab === 2">
        <view class="review-item" v-for="item in rejectedItems" :key="item.id">
          <view class="activity-card rejected">
            <view class="activity-header">
              <image
                class="activity-avatar"
                :src="item.avatar"
                mode="aspectFill"
              />
              <view class="activity-info">
                <view class="activity-organizer">{{ item.organizer }}</view>
                <view class="activity-bottom">
                  <view class="activity-responsible"
                    >负责人：{{ item.responsible || "张*君" }}</view
                  >
                  <view class="arrow-right"></view>
                </view>
              </view>
            </view>
          </view>
        </view>

        <view v-if="rejectedItems.length === 0" class="empty-state">
          <image src="/static/activity/none.png" class="empty-icon" />
          <text class="empty-text">暂无未通过活动</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { useUserStore } from "../../stores/user";
const userStore = useUserStore();
export default {
  name: "AdminReview",
  data() {
    return {
      currentTab: 0, // 当前选中的标签页：0-正在审核，1-审核通过，2-审核未通过
      pendingItems: [
        {
          id: 1,
          avatar: "/static/activity/icon1.png",
          title: "武汉大学测绘社区清洁活动",
          organizer: "武汉大学测绘社区居委会",
          time: "报名截止时间：7月3日",
          location: "武汉大学-测绘社区",
        },
        {
          id: 2,
          avatar: "/static/activity/icon2.png",
          title: "武汉大学测绘社区清洁活动",
          organizer: "武汉大学测绘社区居委会",
          time: "报名截止时间：7月3日",
          location: "武汉大学-测绘社区",
        },
        {
          id: 3,
          avatar: "/static/activity/icon1.png",
          title: "武汉大学测绘社区清洁活动",
          organizer: "武汉大学测绘社区居委会",
          time: "报名截止时间：7月3日",
          location: "武汉大学-测绘社区",
        },
        {
          id: 4,
          avatar: "/static/activity/icon2.png",
          title: "武汉大学测绘社区清洁活动",
          organizer: "武汉大学测绘社区居委会",
          time: "报名截止时间：7月3日",
          location: "武汉大学-测绘社区",
        },
        {
          id: 5,
          avatar: "/static/activity/icon1.png",
          title: "武汉大学测绘社区清洁活动",
          organizer: "武汉大学测绘社区居委会",
          time: "报名截止时间：7月3日",
          location: "武汉大学-测绘社区",
        },
        {
          id: 6,
          avatar: "/static/activity/icon2.png",
          title: "武汉大学测绘社区清洁活动",
          organizer: "武汉大学测绘社区居委会",
          time: "报名截止时间：7月3日",
          location: "武汉大学-测绘社区",
        },
        {
          id: 7,
          avatar: "/static/activity/icon1.png",
          title: "武汉大学测绘社区清洁活动",
          organizer: "武汉大学测绘社区居委会",
          time: "报名截止时间：7月3日",
          location: "武汉大学-测绘社区",
        },
      ],
      approvedItems: [
        {
          id: 101,
          avatar: "/static/activity/icon1.png",
          title: "社区环保宣传活动",
          organizer: "武汉大学测绘社区居委会",
          time: "审核通过时间：6月28日",
          location: "武汉大学-测绘社区",
        },
        {
          id: 102,
          avatar: "/static/activity/icon2.png",
          title: "垃圾分类培训活动",
          organizer: "武汉大学测绘社区居委会",
          time: "审核通过时间：6月25日",
          location: "武汉大学-测绘社区",
        },
      ],
      rejectedItems: [
        {
          id: 201,
          avatar: "/static/activity/icon1.png",
          title: "不合规活动示例",
          organizer: "某组织",
          time: "审核拒绝时间：6月20日",
          location: "某地点",
        },
      ],
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    // 切换标签页
    switchTab(index) {
      this.currentTab = index;
      this.getList();
    },
    // 获取列表
    getList() {
      console.log("token:", userStore.userInfo.token);
      uni.request({
        url: `http://localhost:8080/api/admin/pending-users`,
        method: "GET",
        header: {
          Authorization: "Bearer " + userStore.userInfo.token,
        },
        success: (res) => {
          this.pendingItems = res.data.data;
        },
        fail(err) {
          console.error("失败！", err);
        },
      });
      uni.request({
        url: `http://localhost:8080/api/admin/approved-users`,
        method: "GET",
        header: {
          Authorization: "Bearer " + userStore.userInfo.token,
        },
        success: (res) => {
          this.approvedItems = res.data.data;
        },
        fail(err) {
          console.error("失败！", err);
        },
      });
      // 未审批列表
      uni.request({
        url: `http://localhost:8080/api/admin/refused-users`,
        method: "GET",
        header: {
          Authorization: "Bearer " + userStore.userInfo.token,
        },
        success: (res) => {
          console.log("获取列表成功！", res.data);

          this.rejectedItems = res.data.data;
        },
        fail(err) {
          console.error("失败！", err);
        },
      });
    },

    // 处理审核通过
    handleApprove(itemId) {
      uni.showModal({
        title: "确认操作",
        content: "确定要通过这个活动申请吗？",
        success: (res) => {
          if (res.confirm) {
            const token = userStore.userInfo.token;

            uni.request({
              url: "http://localhost:8080/api/admin/approve",
              method: "PUT",
              header: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token,
              },
              data: {
                userId: itemId, // 传入你要审核的居委会用户 ID
                approved: true, // 审批通过
              },
              success: (res) => {
                if (res.data && res.data.success) {
                  // 成功后更新 UI
                  const itemIndex = this.pendingItems.findIndex(
                    (item) => item.id === itemId
                  );
                  if (itemIndex !== -1) {
                    const approvedItem = this.pendingItems[itemIndex];
                    approvedItem.time = `审核通过时间：${new Date().toLocaleDateString()}`;
                    this.approvedItems.unshift(approvedItem);
                    this.pendingItems.splice(itemIndex, 1);
                  }

                  uni.showToast({
                    title: "审核成功",
                    icon: "success",
                  });
                } else {
                  uni.showToast({
                    title: "审批失败",
                    icon: "error",
                  });
                  console.error("审批失败：", res.data.message);
                }
              },
              fail: (err) => {
                uni.showToast({
                  title: "网络错误",
                  icon: "error",
                });
                console.error("API 请求失败：", err);
              },
            });
          }
        },
      });
    },

    // 处理审核拒绝
    handleReject(itemId) {
      uni.showModal({
        title: "确认操作",
        content: "确定要拒绝这个活动申请吗？",
        success: (res) => {
          if (res.confirm) {
            const token = userStore.userInfo.token; // 获取 JWT Token

            uni.request({
              url: "http://localhost:8080/api/admin/approve", // 替换为你的接口地址
              method: "PUT",
              header: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token,
              },
              data: {
                userId: itemId,
                approved: false, // ❗️这里为 false，表示拒绝
              },
              success: (res) => {
                if (res.data && res.data.success) {
                  // 成功后更新前端状态
                  const itemIndex = this.pendingItems.findIndex(
                    (item) => item.id === itemId
                  );
                  if (itemIndex !== -1) {
                    const rejectedItem = this.pendingItems[itemIndex];
                    rejectedItem.time = `审核拒绝时间：${new Date().toLocaleDateString()}`;
                    this.rejectedItems.unshift(rejectedItem);
                    this.pendingItems.splice(itemIndex, 1);
                  }

                  uni.showToast({
                    title: "已拒绝",
                    icon: "none",
                  });
                } else {
                  uni.showToast({
                    title: "拒绝失败",
                    icon: "error",
                  });
                  console.error("拒绝失败：", res.data.message);
                }
              },
              fail: (err) => {
                uni.showToast({
                  title: "网络错误",
                  icon: "error",
                });
                console.error("请求失败：", err);
              },
            });
          }
        },
      });
    },
  },
};
</script>

<style scoped>
.admin-review-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #e8f5e8 0%, #f0f8f0 100%);
  padding-bottom: 20rpx;
}

.header {
  background: linear-gradient(135deg, #b7ef68, #03e996);
  padding: 80rpx 40rpx 40rpx;
  text-align: center;
}

.header-title {
  color: white;
  font-size: 36rpx;
  font-weight: bold;
}

/* 标签栏样式 */
.tabs {
  display: flex;
  background: white;
  border-bottom: 2rpx solid #f0f0f0;
}

.tab {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
  font-size: 28rpx;
  color: #666;
  position: relative;
  transition: all 0.3s ease;
}

.tab.active {
  color: #03e996;
  font-weight: bold;
}

.tab.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60rpx;
  height: 4rpx;
  background: #03e996;
  border-radius: 2rpx;
}

.review-list {
  padding: 20rpx;
}

.review-item {
  margin-bottom: 20rpx;
}

.activity-card {
  background: linear-gradient(135deg, #b7ef68, #03e996);
  border-radius: 20rpx;
  padding: 25rpx 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
  position: relative;
  height: 120rpx;
  display: flex;
  align-items: center;
}

.activity-header {
  display: flex;
  align-items: center;
  margin-bottom: 0;
  position: relative;
  flex: 1;
}

.activity-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50rpx;
  margin-right: 25rpx;
  flex-shrink: 0;
}

.activity-info {
  flex: 1;
}

/* 状态徽章 */
.status-badge {
  position: absolute;
  top: 0;
  right: 0;
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: bold;
}

.approved-badge {
  background: #e8f5e8;
  color: #4caf50;
}

.rejected-badge {
  background: #ffebee;
  color: #f44336;
}

/* 不同状态的卡片样式 */
.activity-card.approved {
  border-left: 6rpx solid #4caf50;
}

.activity-card.rejected {
  border-left: 6rpx solid #f44336;
  opacity: 0.8;
}

.activity-organizer {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
  font-weight: bold;
}

.activity-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-phone {
  font-size: 24rpx;
  color: #555;
}

.activity-responsible {
  font-size: 24rpx;
  color: #555;
}

.arrow-right {
  width: 16.5px;
  height: 16.5px;
  border: 3.5px solid #000000;
  border-left: none;
  border-bottom: none;
  transform: rotate(45deg);
  position: absolute;
  right: 30rpx;
  top: 50%;
  transform: rotate(45deg) translateY(-50%);
}

.review-actions {
  display: flex;
  gap: 15rpx;
  margin-left: auto;
  align-items: center;
}

.reject-btn {
  background: #333;
  color: white;
  border: none;
  border-radius: 50rpx;
  padding: 2rpx 18rpx;
  font-size: 24rpx;
  font-weight: bold;
  min-width: 80rpx;
}

.approve-btn {
  background: white;
  color: #333;
  border: none;
  border-radius: 90rpx;
  padding: 2rpx 18rpx;
  font-size: 24rpx;
  font-weight: bold;
  min-width: 80rpx;
}

.reject-btn:active {
  background: #e64a19;
}

.approve-btn:active {
  background: #45a049;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 40rpx;
}

.empty-icon {
  width: 200rpx;
  height: 200rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>
