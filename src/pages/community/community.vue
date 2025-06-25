<template>
	<view class="community-container">
		<!-- 顶部栏 -->
		<view class="header">
			<view class="header-bg"></view>
			<text class="title">社区</text>
		</view>

		<!-- 功能区 -->
		<view class="features">
			<view class="feature-item" @click="goToMyPage">
				<image class="feature-img" src="/static/community/my_2.png" mode="aspectFit"/>
				<text class="feature-label">我的绿值</text>
			</view>
			<view class="feature-item" @click="goToPhotoChat">
				<image class="feature-img" src="/static/community/camera_2.png" mode="aspectFit"/>
				<text class="feature-label">拍照识别</text>
			</view>
			<view class="feature-item">
				<image class="feature-img" src="/static/community/shop_2.png" mode="aspectFit"/>
				<text class="feature-label">兑换商城</text>
			</view>
		</view>

		<!-- 绿值排行 -->
		<view class="rank-card">
			<view class="rank-header">
				<text class="rank-title">绿值排行</text>
				<image class="crown" src="/static/community/huangguan1.png" mode="aspectFit"/>
			</view>
			<view class="rank-list">
				<view class="rank-item" v-for="(item, index) in rankList" :key="index">
					<image v-if="index < 3" class="medal" :src="getMedalImage(index)" mode="aspectFit"/>
					<text v-else class="rank-num">{{ formatRankNumber(index + 1) }}</text>
					<text class="user-name">{{ item.nickname }}</text>
					<text class="user-score">{{ item.green_score }}绿值</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useChatStore } from '../../stores/chat'
import { useUserStore } from '../../stores/user'

// 获取全局状态
const chatStore = useChatStore();
const userStore = useUserStore();

// 排行榜数据
const rankList = computed(() => userStore.rank || [])

// 获取奖牌图片
const getMedalImage = (index) => {
	return [
		'/static/community/rk_1.png',
		'/static/community/rk_2.png',
		'/static/community/rk_3.png'
	][index]
}

// 格式化排名数字
const formatRankNumber = (num) => {
	return num < 10 ? '0' + num : num
}

// 原有方法保持不变
const goToMyPage = () => {
	uni.switchTab({ url: '/pages/my/my' })
}

const goToPhotoChat = () => {
	uni.chooseImage({
		count: 1,
		sizeType: ['original', 'compressed'],
		sourceType: ['camera'],
		success: (res) => {
			const filePath = res.tempFilePaths[0]
			uni.getFileSystemManager().readFile({
				filePath,
				encoding: 'base64',
				success: (fileRes) => {
					chatStore.setImage("data:image/jpeg;base64," + fileRes.data);
					uni.switchTab({ url: '/pages/chat/chat' })
				},
				fail: () => {
					uni.showToast({ title: '图片读取失败', icon: 'none' })
				}
			})
		},
		fail: () => {
			uni.showToast({ title: '未选择图片', icon: 'none' })
		}
	})
}
</script>

<style scoped>
/* 保持原有样式完全不变 */
.community-container {
	background: #f7f7f7;
	min-height: 100vh;
	padding-bottom: 20px;
}
.header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-top: 0px;
	margin-bottom: 10px;
	position: relative;
}
.header-bg {
    position: absolute;
    left: 0; 
    top: 0;
    width: 200px; 
    height: 100px;
    border-radius: 0 0 40px 0;
    background: linear-gradient(-45deg, rgba(230,248,230,0) 0%, #F1FFDF 100%);
    z-index: 0;
}
.title, .plus-btn {
	position: relative;
	z-index: 1;
}
.title {
	font-size: 26px;
	font-weight: 600;
	color: #1a1a1a;
	margin-left: 16px;
	line-height: 34.48px;
}

.features {
	display: flex;
	justify-content: space-around;
	margin: 50px 0 10px 0;
}
.feature-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	width: 64px;
}
.feature-img {
	width: 64px;
	height: 64px;
	background: #fff;
	border-radius: 12px;
}
.feature-label {
	margin-top: 6px;
	font-size: 14px;
	color: #33333386;
}
.rank-card {
	left: 0rpx; top: 60rpx;
	position: relative;
	background: linear-gradient(180deg, #C8ED93 0%, #E2F4CA 100%);
	border-top-left-radius: 30px;
	border-top-right-radius: 30px;
	margin: 0 0px;
	padding: 5px 0 10px 0;
	box-shadow: 0 2px 8px rgba(0,0,0,0.03);
}
.rank-header {
	left: 0rpx; top: -56rpx;
    
	position: relative;
/* 	left: 0rpx; 
	top: -56rpx; */
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 10px;
    height: 60px;
}

.rank-title {
    position: relative;
    z-index: 10; 
    font-size: 30px;
	letter-spacing: 10rpx;
    font-weight: bold;
    color: #222;
    margin-top: 28px;
    line-height: 44px;
	left: 0rpx; 
	top: -24rpx
}

.crown {
    position: absolute;
    left: 50%;
    top: 0;
    transform: translateX(-50%);
    width: 80px;
    height: 38px;
    z-index: 1; /* 将皇冠图片的z-index值设为1 */
    pointer-events: none;
}
.rank-list {
	margin-top: 10px;
}
.rank-item {
	display: flex;
	align-items: center;
	background: #fff;
	border-radius: 16px;
	margin: 10px 16px;
	padding: 10px 12px;
	min-height: 60px;
}
.medal {
	width: 70px;
	height: 70px;
	margin-right: 10px;
}
.rank-num {
	width: 122rpx; height: 89rpx;
	
	font-size: 38px;
	font-weight: 700;
	color: #bbb;
	margin-right: 10px;
	text-align: center;
}

/* 新增的用户名和分数样式（保持与原有风格一致） */
.user-name {
	font-size: 18px;
	font-weight: 700;
	color: hsl(85, 59%, 66%);
	flex: 1;
	margin-left: 10px;
}

.user-score {
	font-size: 16px;
	color: #4CAF50;
	font-weight: 600;
	margin-right: 10px;
}
</style>