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
          flex-direction: column; /* 设置为列布局，实现上下排列 */
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
      ref="scrollView"
      class="messages"
      scroll-top="{{scrollTop}}"
    >
      <view
        v-for="(msg, index) in messages"
        :key="index"
        :id="'msg' + index"
        :class="['message', msg.from === 'user' ? 'user' : 'bot']"
      >
        <view>{{ msg.text }}</view>
      </view>
    </scroll-view>

    <!-- 输入框和发送按钮 -->
    <view class="message-input">
      <div style="border: 1px solid #ccc;display: flex;border-radius: 8px;align-items: center;">
        <textarea v-model="newMessage" placeholder="请输入消息..." class="input" ></textarea>
        <div @click="sendMessage">
          <image src="/static/chat/send.png" class="send-btn"></image>
        </div>
      </div>
      <image class="pic" src="/src/static/chat/image.png" />

      <image class="pic" src="/src/static/chat/speak.png" />
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      newMessage: "",
      messages: [
        {
          from: "bot",
          text: "你好呀，我是无聊AI助手·小绿，我会回答你提出的任何问题，解答各种疑惑～",
        },
      ],
      scrollTop: 0,
      scrollToView: "",
    };
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() !== "") {
        this.messages.push({ from: "user", text: this.newMessage });
        this.newMessage = "";

        // 模拟AI回复
        setTimeout(() => {
          this.messages.push({ from: "bot", text: "这是机器人给出的回答！" });
          // 延迟后更新 scrollTop
          this.scrollToBottom();
        }, 1000);
      }
    },
    // 自动滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        this.scrollToView = "msg" + (this.messages.length - 1);
      });
    },
  },
  watch: {
    messages() {
      this.scrollToBottom(); // 每次 messages 改变时自动滚动到底部
    },
  },
};
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

  align-items: center;
  justify-content: center;
  height: 50px;
  position: fixed;
  left: 0;
  bottom: 0;
  display: flex;
  padding: 10px;
  border-top: 1px solid #ccc;
  background-color: #f9f9f9;
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
}

.send-btn {
  width: 28px;
  height: 28px;
}
</style>
