<template>
	<view class="chat-container">
		<view v-if="photoBase64" class="photo-msg">
			<image :src="'data:image/png;base64,' + photoBase64" mode="aspectFit" class="photo-image" />
		</view>
		<text>对话</text>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				photoBase64: ''
			}
		},
		onShow() {
			// 进入页面时自动检测 base64 图片
			const base64 = uni.getStorageSync('chat_photo_base64');
			if (base64) {
				this.photoBase64 = base64;
				// 可选：显示后清除，避免重复
				uni.removeStorageSync('chat_photo_base64');
			}
		}
	}
</script>

<style scoped>
	.chat-container {
		padding: 20px;
	}
	.photo-msg {
		margin-bottom: 16px;
		display: flex;
		justify-content: flex-start;
	}
	.photo-image {
		width: 180px;
		height: 180px;
		border-radius: 8px;
		background: #eee;
	}
</style> 