<template>
  <view class="chat-container">
    <!-- 顶部聊天框 -->
    <view class="chat-header">
      <image class="avatar" src="/src/static/chat/head.png" />
      <div
        style="
          margin-top: 16px;
          width: 233px;
          display: flex;
          flex-direction: column;
        "
      >
        <div
          style="
            font-size: 20px;
            font-weight: bold;
            color: black;
            left: 110px;
            top: 124px;
            margin-bottom: 8px;
          "
        >
          哈喽~
        </div>
        <div style="font-size: 13px">
          我是无废AI小助手-小绿，我会回答你提出的任何问题，解答各种疑惑~
        </div>
      </div>
    </view>

    <!-- 消息列表 -->
    <scroll-view
      scroll-y
      :scroll-into-view="scrollToView"
      scroll-with-animation
      class="messages"
      :scroll-top="scrollTop"
    >
      <view
        v-for="(msg, index) in chatStore.history"
        :key="index"
        :id="'msg' + index"
        :class="['message', msg.role === 'user' ? 'user' : 'bot']"
      >
        <view v-if="msg.content">{{ msg.content }}</view>
        <image
          v-if="msg.image"
          :src="msg.image"
          mode="widthFix"
          style="
            max-width: 120px;
            max-height: 120px;
            border-radius: 8px;
            margin-top: 5px;
          "
        />
      </view>
    </scroll-view>

    <!-- 输入框和发送按钮 -->
    <view class="message-input">
      <!-- 上传图片显示区域 -->
      <view v-if="chatStore.inputImages" class="image-preview">
        <view class="image-wrapper">
          <image
            :src="chatStore.inputImages"
            mode="widthFix"
            class="uploaded-image"
          />
          <text class="close-btn" @click="chatStore.inputImages = null">×</text>
        </view>
      </view>

      <div style="display: flex; border-radius: 18px; align-items: center">
        <textarea
          v-model="chatStore.inputMessage"
          placeholder="请输入消息..."
          class="input"
          placeholder-style="letter-spacing: 20px; color: #999;"
          @confirm="chatStore.addUserMessage"
        ></textarea>
        <div @click="chatStore.addUserMessage">
          <image src="/static/chat/send.png" class="send-btn"></image>
        </div>
        <image class="pic" src="/static/chat/image.png" @click="uploadImage" />
        <image class="pic" src="/static/chat/speak.png" @click="speak" />
      </div>
    </view>
  </view>
</template>

<script setup>
import { ref, watch } from "vue";
import { useChatStore } from "@/stores/chat";

const chatStore = useChatStore();
const scrollToView = ref("");
const scrollTop = ref(0);

const uploadImage = () => {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      const tempFilePaths = res.tempFilePaths;
      uni.getFileSystemManager().readFile({
        filePath: tempFilePaths[0],
        encoding: "base64",
        success: (res) => {
          chatStore.setImage("data:image/jpeg;base64," + res.data);
        },
      });
    },
  });
};

const speak = () => {
  const recorderManager = wx.getRecorderManager();
  recorderManager.start({
    format: "mp3", // 推荐格式
    duration: 60000, // 最长1分钟
  });
  
};

const scrollToBottom = () => {
  if (chatStore.history.length > 0) {
    scrollToView.value = "msg" + (chatStore.history.length - 1);
    scrollTop.value = 999999; // 确保滚动到底部
  }
};

watch(
  () => chatStore.history,
  () => {
    scrollToBottom();
  },
  { deep: true }
);
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-header {
  display: flex;
  margin-left: auto;
  margin-right: auto;
  left: 16px;
  top: 108px;
  width: 350px;
  height: 112px;
  opacity: 1;
  border-radius: 12px;
  background: linear-gradient(
    180deg,
    rgba(200, 255, 122, 1) 0%,
    rgba(255, 255, 255, 0.97) 100%
  );
  border: 1px solid rgba(200, 255, 122, 1);
}

.avatar {
  left: -10px;
  top: 10px;
  width: 110px;
  height: 122px;
}

.username {
  font-size: 18px;
  font-weight: bold;
  margin-left: 10px;
}

.status {
  margin-left: auto;
  color: green;
}

.messages {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  height: 450px;
  padding: 10px;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: #fff;
}

.message {
  display: block;
  max-width: 80%;
  word-break: break-all;
  clear: both;
  margin-bottom: 20px;
  padding: 10px;
}

.message.user {
  width: auto;
  float: right;
  align-items: flex-end;
  max-width: 305px;
  min-width: 60px;
  border-radius: 21px 21px 0px 21px;
  background: rgba(184, 240, 105, 1);
  margin-right: 25px;
  margin-left: auto;
  text-align: right;
}

.message.bot {
  float: left;
  align-items: flex-start;
  width: auto;
  max-width: 305px;
  min-width: 60px;
  border-radius: 21px 21px 21px 0px;
  background: rgba(27, 27, 27, 1);
  color: white;
  margin-left: 10px;
  margin-right: auto;
}

.message-input {
  width: 375px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50px;
  position: fixed;
  left: 0;
  bottom: 0;
  padding: 10px;
  background-color: #f5f5f5;
}

.pic {
  width: 28px;
  height: 28px;
  margin-left: 4px;
  margin-right: 4px;
}

.input {
  width: 240px;
  height: 36px;
  padding: 2px;
  border-radius: 20px;
  border: none;

  background-color: white;
}

.send-btn {
  width: 28px;
  height: 28px;
}

/* 上传图片显示样式 */
/* 上传图片显示样式 */
.image-preview {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  padding: 8px 16px;
  background-color: transparent;
}

.image-wrapper {
  position: relative;
  width: 40px;
  height: 40px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: transparent;
  overflow: hidden;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

/* 右上角叉号按钮 */
.close-btn {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 16px;
  height: 16px;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 50%;
  font-size: 12px;
  text-align: center;
  line-height: 16px;
  color: #333;
  cursor: pointer;
  z-index: 2;
}
</style>
