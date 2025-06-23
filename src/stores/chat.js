// stores/chat.js
import { defineStore } from "pinia";
import { ref } from "vue";

export const useChatStore = defineStore("chat", () => {
  // 状态定义
  const inputMessage = ref("");
  const inputImages = ref("");
  const history = ref([
    {
      role: "AI",
      content: "你好！我是无废AI小助手-小绿，有什么可以帮你的吗？",
    },
  ]);

  // 操作方法
  const setMessage = (message) => {
    inputMessage.value = message;
  };

  const setImage = (image) => {
    inputImages.value = image;
  };

  const addUserMessage = () => {
    if (history.value.length >= 20) {
      history.value.shift();
      if (inputImages.value) {
        history.value.shift();
      }
    }

    // 添加用户消息
    if (inputMessage.value) {
      history.value.push({
        role: "user",
        content: inputMessage.value,
        image: null,
      });
    }
    if (inputImages.value) {
      history.value.push({
        role: "user",
        content: null,
        image: inputImages.value,
      });
    }

    // 清空输入
    inputMessage.value = "";
    inputImages.value = "";

    // 模拟AI回复（之后替换为API调用）
    setTimeout(() => {
      addAIMessage("这是AI的模拟回复，之后会替换为真实API返回");
    }, 500);
  };

  const addAIMessage = (content) => {
    history.value.push({
      role: "AI",
      content: content,
    });
  };

  return {
    inputMessage,
    inputImages,
    history,
    setMessage,
    setImage,
    addUserMessage,
    addAIMessage,
  };
});
