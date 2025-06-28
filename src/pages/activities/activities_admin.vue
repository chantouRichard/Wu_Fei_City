<template>
	<view class="activities-bg">
		<!-- 顶部渐变背景和Tab栏 -->
		<view class="header-bg"></view>
		<view class="tabs">
			<view
				class="tab"
				:class="{ active: currentTab === 0 }"
				@click="switchTab(0)"
			>
				正在报名活动
			</view>
			<view
				class="tab"
				:class="{ active: currentTab === 1 }"
				@click="switchTab(1)"
			>
				正在执行活动
			</view>
			<view
				class="tab"
				:class="{ active: currentTab === 2 }"
				@click="switchTab(2)"
			>
				发布过的活动
			</view>
		</view>
		<!-- 内容区 -->
		<view class="content">
			<!-- 可报名活动 -->
			<view v-if="currentTab === 0" class="activity-list">
				<view class="card" v-for="item in availableActivities" :key="item.id">
					<view class="card-content">
						<image class="card-img" :src="item.img" mode="aspectFill" />
						<view class="card-main">
							<view class="card-title">{{ item.title }}</view>
							<view class="card-info">
								<view class="card-line card-org">{{ item.desc }}</view>
								<view class="card-line card-meta">{{ item.time }}</view>
								<view class="card-line card-meta">{{ item.place }}</view>
							</view>
						</view>
					</view>
					<view class="card-bottom">
						<view class="avatars-group">
							<view class="avatars">
								<image v-for="(a, i) in item.avatars" :key="i" class="avatar" :src="a" />
							</view>
							<text class="join-num">{{ item.joined }}/{{ item.limit }}人</text>
						</view>
					</view>
				</view>
			</view>
			<!-- 已报名活动 -->
			<view v-if="currentTab === 1" class="activity-list">
				<view class="card" v-for="item in registeredActivities" :key="item.id">
					<view class="card-content">
						<image class="card-img" :src="item.img" mode="aspectFill" />
						<view class="card-main">
							<view class="card-title">{{ item.title }}</view>
							<view class="card-info">
								<view class="card-line card-org">{{ item.desc }}</view>
								<view class="card-line card-meta">{{ item.time }}</view>
								<view class="card-line card-meta">{{ item.place }}</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 已完成活动 -->
			<view v-if="currentTab === 2" class="activity-list">
				<view class="card" v-for="item in historyActivities" :key="item.id">
					<view class="card-content">
						<image class="card-img" :src="item.img" mode="aspectFill" />
						<view class="card-main">
							<view class="card-title">{{ item.title }}</view>
							<view class="card-info">
								<view class="card-line card-org">{{ item.desc }}</view>
								<view class="card-line card-meta">{{ item.time }}</view>
								<view class="card-line card-meta">{{ item.place }}</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
	<!-- 页面右下角悬浮按钮组，始终渲染两个按钮 -->
	<view class="fab-group">
		<view
			v-if="currentTab === 1"
			class="page-fab"
			@click="onFabClick('user')"
		>
			<image class="fab-icon" src="/static/activity/btn_2.png" mode="aspectFit" />
		</view>
		<view
			class="page-fab"
			@click="onFabClick('send')"
			:style="{ marginTop: currentTab === 1 ? '16px' : '0' }"
		>
			<image class="fab-icon" src="/static/activity/btn.png" mode="aspectFit" />
		</view>
	</view>
</template>

<script setup>
import { ref, computed } from 'vue'

const currentTab = ref(0)
const switchTab = (idx) => {
	currentTab.value = idx
}

const availableActivities = ref([
	{
		id: 1,
		img: '/static/activity/activi1.png',
		title: '测绘社区打扫活动',
		desc: '武汉大学测绘社区居委会',
		time: '7月3日 14:30~16:30',
		place: '武汉大学-测绘社区',
		avatars: [
			'/static/activity/icon1.png',
			'/static/activity/icon2.png'
		],
		joined: 18,
		limit: 35
	},
	{
		id: 2,
		img: '/static/activity/activi2.png',
		title: '环保知识竞赛',
		desc: '洪山区南湖街社区居委会',
		time: '7月6日 9:30~11:00',
		place: '洪山区洪山大道南湖公园管理处',
		avatars: [
			'/static/activity/icon1.png',
			'/static/activity/icon2.png'
		],
		joined: 125,
		limit: 125
	}
])

const registeredActivities = ref([
	{
		id: 3,
		img: '/static/activity/activi1.png',
		title: '测绘社区打扫活动',
		desc: '武汉大学测绘社区居委会',
		time: '7月3日 14:30~16:30',
		place: '武汉大学-测绘社区',
		avatars: [
			'/static/activity/icon1.png',
			'/static/activity/icon2.png'
		],
		joined: 18,
		limit: 35
	},
	{
		id: 4,
		img: '/static/activity/activi2.png',
		title: '环保知识竞赛',
		desc: '洪山区南湖街社区居委会',
		time: '7月6日 9:30~11:00',
		place: '洪山区洪山大道南湖公园管理处',
		avatars: [
			'/static/activity/icon1.png',
			'/static/activity/icon2.png'
		],
		joined: 125,
		limit: 125
	}
])

const historyActivities = ref([
	{
		id: 5,
		img: '/static/activity/activi1.png',
		title: '测绘社区打扫活动',
		desc: '武汉大学测绘社区居委会',
		time: '7月3日 14:30~16:30',
		place: '武汉大学-测绘社区',
		avatars: [
			'/static/activity/icon1.png',
			'/static/activity/icon2.png'
		],
		joined: 18,
		limit: 35
	},
	{
		id: 6,
		img: '/static/activity/activi2.png',
		title: '环保知识竞赛',
		desc: '洪山区南湖街社区居委会',
		time: '7月6日 9:30~11:00',
		place: '洪山区洪山大道南湖公园管理处',
		avatars: [
			'/static/activity/icon1.png',
			'/static/activity/icon2.png'
		],
		joined: 125,
		limit: 125
	}
])



function getBtnText(item, type) {
	if (type === 'available') {
		return item.joined >= item.limit ? '人数已满' : '去报名';
	} else if (type === 'registered') {
		return '退出活动';
	} else {
		return '已结束';
	}
}
function getBtnClass(item, type) {
	if (type === 'available') {
		return item.joined >= item.limit ? 'btn-full' : 'btn-join';
	} else if (type === 'registered') {
		return 'btn-quit';
	} else {
		return 'btn-disabled';
	}
}

function onFabClick(type) {
	uni.showToast({ title: `FAB点击: ${type}`, icon: 'none' })
}
</script>

<style scoped>
.activities-bg {
	min-height: 100vh;
	background: linear-gradient(180deg, #b7ef68 0%, #f7f7f7 100%);
	padding-bottom: 20px;
}
.header-bg {
	position: absolute;
	left: 0; top: 0;
	width: 100vw;
	height: 120px;
	background: linear-gradient(135deg, #b7ef68 0%, #b7ef68 100%);
	z-index: 0;
}
.tabs {
	display: flex;
	background: transparent;
	position: relative;
	z-index: 1;
	padding-top: 20px;
	margin-bottom: 10px;
}
.tab {
	flex: 1;
	padding: 14px 0 10px 0;
	text-align: center;
	font-size: 17px;
	
	font-weight: 500;
	position: relative;
	background: transparent;
	transition: color 0.2s;
}
.tab.active {
	
	font-weight: bold;
	font-size: 19px;
}
.tab.active::after {
	content: '';
	position: absolute;
	bottom: 0;
	left: 50%;
	transform: translateX(-50%);
	width: 36px;
	height: 5px;
	background: #b7ef68;
	border-radius: 3px;
}
.content {
	padding: 10px 0 0 0;
	position: relative;
	z-index: 2;
}
.activity-list {
	display: flex;
	flex-direction: column;
	gap: 22px;
	margin-top: -5px;
}
.card {
	width: 95%;
	background: #fff;
	border-radius: 18px;
	box-shadow: 0 4px 18px rgba(60,197,31,0.10);
	overflow: hidden;
	margin: 0 8px 0 8px;
	display: flex;
	flex-direction: column;
	position: relative;
}
.card-content {
	display: flex;
	flex-direction: row;
	width: 100%;
	padding: 18px 16px 10px 16px;
	box-sizing: border-box;
}
.card-img {
	width: 127px;
	height: 109px;
	border-radius: 12px;
	object-fit: cover;
	margin-right: 14px;
	flex-shrink: 0;
	background: #f2f2f2;
}
.card-main {
	flex: 1;
	display: flex;
	flex-direction: column;
	justify-content: center;
}
.card-title {
	font-size: 18px;
	font-weight: bold;
	color: #222;
	margin-bottom: 20px;
	font-family: 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
}
.card-info {
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	padding-left: 4px;
	border-left: 3px solid #e0e0e0;
	margin-left: -2px;
}
.card-line {
	margin-bottom: 2px;
	line-height: 1.3;
}
.card-org {
	font-size: 15px;
	color: #444;
	font-family: 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
}
.card-meta {
	font-size: 13px;
	color: #bbb;
	font-family: 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
}
.card-bottom {
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 16px 0 16px;
	min-height: 44px;
	box-sizing: border-box;
	background: #fff;
	border-top: 1px solid #f0f0f0;
}
.avatars-group {
	display: flex;
	align-items: center;
}
.avatars {
	display: flex;
	align-items: center;
}
.avatar {
	width: 28px;
	height: 28px;
	border-radius: 50%;
	border: 2px solid #fff;
	margin-right: -8px;
	background: #eee;
	box-shadow: 0 1px 4px rgba(60,197,31,0.08);
}
.join-num {
	font-size: 14px;
	color: #bbb;
	margin-left: 10px;
	font-weight: 500;
	white-space: nowrap;
}
.card-btn {
	width: 100px;
	height: 36px;
	border-radius: 18px;
	font-size: 16px;
	font-weight: 600;
	border: none;
	outline: none;
	box-shadow: 0 2px 8px rgba(60,197,31,0.08);
	background: linear-gradient(90deg, #3cc51f 0%, #00e676 100%);
	color: #fff;
	letter-spacing: 1px;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 0;
	text-align: center;
	margin: 0;
	cursor: pointer;
	transition: filter 0.2s;
}
.card-btn:active {
	filter: brightness(0.95);
}
.btn-full, .btn-disabled {
	background: #eee !important;
	color: #bbb !important;
	border: none;
}
.btn-quit {
	background: #fff !important;
	color: #3cc51f !important;
	border: 1px solid #3cc51f;
}
.card-fab {
	position: absolute;
	right: 18px;
	bottom: 18px;
	width: 44px;
	height: 44px;
	background: linear-gradient(135deg, #3cc51f 0%, #00e676 100%);
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 2px 8px rgba(60,197,31,0.12);
	z-index: 2;
}
.fab-icon {
	width: 26px;
	height: 26px;
	object-fit: contain;
}
.fab-group {
	position: fixed;
	right: 24px;
	bottom: 48px;
	z-index: 9999;
	display: flex;
	flex-direction: column;
	align-items: flex-end;
	gap: 16px;
}
.page-fab {
	width: 56px;
	height: 56px;
	background: linear-gradient(135deg, #3cc51f 0%, #00e676 100%);
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4px 16px rgba(60,197,31,0.18);
	transition: margin-top 0.2s;
}
.fab-icon {
	width: 32px;
	height: 32px;
	object-fit: contain;
}
</style>
