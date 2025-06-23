//EditProfileModal.vue
<template>
  <view>
    <view v-if="visible" class="modal-mask">
      <view class="modal-container">
        <view class="modal-header">
          <text>编辑个人信息</text>
        </view>
        <view class="modal-body">
          <!-- 昵称 -->
          <view class="field">
            <text>昵称：</text>
            <input v-model="form.nickname" placeholder="请输入昵称" />
          </view>
          <!-- 个人介绍 -->
          <view class="field">
            <text>个人介绍：</text>
            <textarea v-model="form.introduction"
                      placeholder="请输入个人介绍"
                      auto-height />
          </view>
          <!-- 头像 -->
          <view class="field avatar-picker">
            <text>头像：</text>
            <image :src="form.avatar" class="avatar-preview" />
            <button @click="chooseAvatar">更换头像</button>
          </view>
        </view>
        <view class="modal-footer">
          <button class="btn-cancel" @click="onCancel">取消</button>
          <button class="btn-save"   @click="onSave">保存</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'EditProfileModal',
  props: {
    visible: Boolean
  },
  emits: ['update:visible','saved'],
  data() {
    return {
      form: {
        nickname:     '',
        introduction: '',
        avatar:       ''
      }
    }
  },
  watch: {
    // 每次弹窗打开，重置表单初始值
    visible(val) {
      if (val) {
        // 父组件通过 v-model:visible 传递 visible，
        // 并在 @saved 回调里更新父组件的数据
        // 所以这里从 $attrs 或 props 再取一次也可以，
        // 也可以改成从 uni.getStorageSync 直接读取 userInfo。
        const user = uni.getStorageSync('userInfo') || {}
        this.form.nickname     = user.nickname || ''
        this.form.introduction = user.introduction || ''
        this.form.avatar       = user.avatar || ''
      }
    }
  },
  methods: {
    onCancel() {
      this.$emit('update:visible', false)
    },
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        success: res => {
          this.form.avatar = res.tempFilePaths[0]
        }
      })
    },
    onSave() {
      // 向父组件抛出新数据
      this.$emit('saved', { 
        nickname:     this.form.nickname,
        introduction: this.form.introduction,
        avatar:       this.form.avatar
      })
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.modal-mask {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex; align-items: center; justify-content: center;
}
.modal-container {
  width: 85%; background: #fff; border-radius: 8px; overflow: hidden;
}
.modal-header {
  padding: 16px; font-size: 18px; font-weight: bold; text-align: center;
  border-bottom: 1px solid #eee;
}
.modal-body {
  padding: 16px;
}
.field {
  margin-bottom: 12px; display: flex; align-items: center;
}
.field text {
  width: 80px;
}
.field input,
.field textarea {
  flex: 1; padding: 8px; border: 1px solid #ddd; border-radius: 4px;
}
.avatar-picker {
  flex-direction: column;
}
.avatar-preview {
  width: 80px; height: 80px; border-radius: 50%; margin: 8px 0;
}
.modal-footer {
  display: flex; border-top: 1px solid #eee;
}
.btn-cancel,
.btn-save {
  flex: 1; padding: 14px 0; text-align: center;
}
.btn-cancel {
  color: #666; border-right: 1px solid #eee;
}
.btn-save {
  color: #007AFF;
}
</style>