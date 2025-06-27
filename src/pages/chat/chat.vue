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
      enable-flex
    >
      <view
        v-for="(msg, index) in chatStore.history"
        :key="index"
        :id="'msg' + index"
        :class="['message', msg.role === 'user' ? 'user' : 'bot']"
      >
        <block v-if="msg.loading">
          <view class="loading-animation">
            <view class="dot"></view>
            <view class="dot"></view>
            <view class="dot"></view>
          </view>
        </block>
        <block v-else>
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
        </block>
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
        <div :class="{ 'send-btn-disabled': chatStore.inputMessage === '' }" @click="sendMessage">
          <image src="/static/chat/send.png" class="send-btn"></image>
        </div>
        <image class="pic" src="/static/chat/image.png" @click="uploadImage" />
        <image class="pic" src="/static/chat/speak.png" @click="speak" />
      </div>
    </view>
  </view>
</template>

<script setup>
import { ref, watch, onUnmounted } from "vue";
import { useChatStore } from "@/stores/chat";
import { useUserStore } from "@/stores/user";

const chatStore = useChatStore();
const userStore = useUserStore();
const scrollToView = ref("");
const scrollTop = ref(0);
const isRecording = ref(false);
const recordTime = ref(0);
let recordTimer = null;
const recorderManager = wx.getRecorderManager();

// 录音权限检查
const checkRecordPermission = async () => {
  try {
    const res = await new Promise((resolve) => {
      wx.getSetting({ success: resolve, fail: resolve });
    });

    if (res.authSetting?.["scope.record"] === undefined) {
      const authRes = await new Promise((resolve) => {
        wx.authorize({
          scope: "scope.record",
          success: () => resolve(true),
          fail: () => resolve(false),
        });
      });
      return authRes;
    }
    return res.authSetting["scope.record"];
  } catch (e) {
    console.error("权限检查失败:", e);
    return false;
  }
};

// 增强版录音功能
const speak = async () => {
  if (isRecording.value) {
    stopRecording();
    return;
  }

  try {
    const hasPermission = await checkRecordPermission();
    if (!hasPermission) {
      await showPermissionGuide();
      return;
    }

    startRecording();
  } catch (error) {
    uni.showToast({ title: `录音失败: ${error.message}`, icon: "none" });
  }
};

const startRecording = () => {
  // 重置状态
  isRecording.value = true;
  recordTime.value = 0;

  // 计时器
  recordTimer = setInterval(() => {
    recordTime.value++;
    if (recordTime.value >= 60) stopRecording(); // 60秒自动停止
  }, 1000);

  // 录音配置
  recorderManager.start({
    format: "mp3",
    duration: 60000,
    sampleRate: 16000,
    encodeBitRate: 48000,
    frameSize: 50,
  });

  uni.showToast({ title: "请开始说话...", icon: "none", duration: 1000 });
};

const stopRecording = () => {
  clearInterval(recordTimer);
  recorderManager.stop();
  isRecording.value = false;
};

// 权限引导
const showPermissionGuide = () => {
  return new Promise((resolve) => {
    uni.showModal({
      title: "需要麦克风权限",
      content: "请允许使用麦克风进行语音输入",
      success(res) {
        if (res.confirm) {
          wx.openSetting({
            success(settingRes) {
              resolve(settingRes.authSetting["scope.record"] === true);
            },
          });
        } else {
          resolve(false);
        }
      },
    });
  });
};

// 录音结果处理
recorderManager.onStop(async (res) => {
  const { tempFilePath, duration } = res;

  // 过滤短录音
  if (duration < 800) {
    uni.showToast({ title: "录音时间太短", icon: "none" });
    return;
  }

  try {
    // 获取base64
    const base64 = await new Promise((resolve, reject) => {
      uni.getFileSystemManager().readFile({
        filePath: tempFilePath,
        encoding: "base64",
        success: (res) => resolve(res.data),
        fail: (err) => reject(new Error("音频读取失败")),
      });
    });
    console.log("音频base64:", base64);

    // 调用语音识别API
    const result = await uni.request({
      url: "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation",
      method: "POST",
      header: {
        Authorization: `Bearer sk-166b19aaea874047815bf8c05daf4b6d`,
        "Content-Type": "application/json",
      },
      data: {
        model: "qwen-audio-asr",
        input: {
          messages: [
            {
              role: "user",
              content: [
                {
                  audio: `data:audio/mp3;base64,${base64}`,
                },
              ],
            },
          ],
        },
      },
    });

    console.log("语音识别结果:", result.data);

    // 处理结果
    if (result.data.output) {
      for (
        let i = 0;
        i < result.data.output.choices[0].message.content.length;
        i++
      ) {
        chatStore.inputMessage +=
          result.data.output.choices[0].message.content[i].text + ".";
      }

      uni.pageScrollTo({ scrollTop: 999999 }); // 确保输入框可见
    }
  } catch (error) {
    console.error("语音识别失败:", error);
    uni.showToast({
      title: "识别失败: " + (error.errMsg || error.message),
      icon: "none",
    });
  }
});

// 错误监听
recorderManager.onError((err) => {
  console.error("录音错误:", err);
  uni.showToast({ title: `录音错误: ${err.errMsg}`, icon: "none" });
  isRecording.value = false;
  clearInterval(recordTimer);
});

// 组件卸载时清理
onUnmounted(() => {
  clearInterval(recordTimer);
  recorderManager.stop();
});

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

// 发送消息
const sendMessage = () => {
  if (chatStore.inputMessage) {
    chatStore.addUserMessage(); // 添加用户消息
    userStore.modifyGreenPlantScore(5); // 修改绿分
  }
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

.send-btn-disabled{
  opacity: 0.5;
  cursor: not-allowed;
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

.loading-animation {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  height: 30rpx;
}

.loading-animation .dot {
  width: 12rpx;
  height: 12rpx;
  background-color: #ffffff;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}

.loading-animation .dot:nth-child(1) {
  animation-delay: -0.32s;
}

.loading-animation .dot:nth-child(2) {
  animation-delay: -0.16s;
}
</style>
