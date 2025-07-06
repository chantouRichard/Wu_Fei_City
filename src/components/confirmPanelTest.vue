<template>
  <!-- 遮罩层 -->
  <view class="overlay" @tap="close"></view>

  <!-- 弹出列表容器 -->
  <view class="list-container">
    <scroll-view scroll-y class="list-scroll">
      <view v-if="list.length == 0" style="display: flex;align-items: center;justify-content: center;font-size: 24px;margin-top: 100px;">
        暂无参与人员
      </view>
      <view
        v-for="(item, index) in list"
        :key="index"
        class="list-item"
        @tap="toggleSelect(index)"
      >
        <text class="item-name">{{ item.nickname }}</text>
        <view class="checkbox" :class="{ selected: item.selected }"></view>
      </view>
    </scroll-view>

    <!-- 底部按钮 -->
    <view class="footer-btn" @tap="confirm">
      <text class="btn-text">确认</text>
    </view>
  </view>
</template>

<script>
import { useUserStore } from "../stores/user";
const userStore = useUserStore();
export default {
  name: "confirmPanel",
  props: {
    activityId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      list: [],
    };
  },
  methods: {
    close() {
      this.$emit("close");
    },
    toggleSelect(index) {
      this.list[index].selected = !this.list[index].selected;
    },
    confirm() {
      const selectedIds = this.list
        .filter((item) => item.selected)
        .map((item) => item.id);
      if (selectedIds.length === 0) {
        return;
      }
      uni.request({
        url: `http://localhost:8080/api/activities/${userStore.userInfo.userId}/confirmAttendance`,
        method: "PUT",
        data: selectedIds,
        success: (response) => {
          console.log("成功返回：", response.data);
          uni.showToast({ title: "确认成功", icon: "success", duration: 2000 });
          this.$emit("close");
        },
        fail: (error) => {
          console.error("请求失败：", error);
        },
      });
    },
  },
  mounted() {
    console.log(this.activityId);
    uni.request({
      url: `http://localhost:8080/api/activities/${this.activityId}/participants`, // 替换为真实接口
      method: "GET",
      success: (res) => {
        console.log("后端返回数据：", res);

        if (res.data) {
          // 给每项加上 selected 字段以支持多选
          this.list = res.data.map((item) => ({
            ...item,
            selected: false,
          }));
        } else {
          console.error("返回数据格式不正确");
        }
      },
      fail: (err) => {
        console.error("请求失败：", err);
      },
    });
  },
};
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1999;
}

.list-container {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 60%;
  background-color: #ffffff;
  z-index: 2000;
  border-top-left-radius: 20rpx;
  border-top-right-radius: 20rpx;
  overflow: hidden;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.list-scroll {
  flex: 1;
  padding: 20rpx;
}

.list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx 0;
  border-bottom: 1px solid #b3b3b3;
  height: 48px;
}

.item-name {
  font-weight: 400;
  margin-left: auto;
  margin-right: auto;
  font-size: 16px;
  color: #333;
}

.checkbox {
  position: absolute;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2rpx solid #ccc;
  right: 30px;
  box-sizing: border-box;
}

.checkbox.selected {
  background-image: url("../static/activity/confirm.png");
  background-size: cover;
  background-position: center;

  border-color: #333;
}

.footer-btn {
  width: 200px;
  height: 48px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 40px;
  background-color: #000;
  border-radius: 24px;
  text-align: center;

  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-text {
  color: #fff;
  font-size: 20px;
  font-weight: 700;
}
</style>
