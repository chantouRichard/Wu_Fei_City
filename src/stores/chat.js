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

  const addUserMessage = async () => {
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

    

    history.value.push({ role: "AI", content: "", loading: true });

    // 模拟AI回复（之后替换为API调用）
    if (!inputImages.value) {
      console.log("AI回复：第一阶段:",);
      try{
        let result = await uni.request({
          url: "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation",
          method: "POST",
          header: {
            Authorization: `Bearer sk-166b19aaea874047815bf8c05daf4b6d`,
            "Content-Type": "application/json",
          },
          data: {
            model: "qwen-vl-max-latest",
            input: {
              messages: [
                {
                  role: "user",
                  content: [
                    {
                      type: "text",
                      text: inputMessage.value,
                    },
                  ],
                },
              ],
            },
          },
        });
      history.value.pop();
      history.value.push({
        role: "AI",
        content: result.data.output.choices[0].message.content[0].text,
      });
    }catch(error){
      console.log("错误显示：",error);
    }
    } else {
      console.log("AI回复：第二阶段:",inputImages.value);
      let result = await uni.request({
        url: "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
        method: "POST",
        header: {
          Authorization: `Bearer sk-166b19aaea874047815bf8c05daf4b6d`,
          "Content-Type": "application/json",
        },
        data: {
          model: "qwen-vl-max-latest",
          messages: [
            {
              role: "system",
              content: [
                {
                  type: "text",
                  text: "You are a helpful assistant.",
                },
              ],
            },
            {
              role: "user",
              content: [
                {
                  type: "image_url",
                  image_url: {
                    url: inputImages.value,
                  },
                },
                {
                  type: "text",
                  text: inputMessage.value,
                },
              ],
            },
          ],
        },
      });
      console.log("AI回复data:", result.data);
      history.value.pop();
      history.value.push({
        role: "AI",
        content: result.data.choices[0].message.content,
      });
    }
    console.log("AI回复：第三阶段");
    // 清空输入
    inputMessage.value = "";
    inputImages.value = "";

    // setTimeout(() => {
    //   addAIMessage("这是AI的模拟回复，之后会替换为真实API返回");
    // }, 500);
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
