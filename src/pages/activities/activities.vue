<template>
	<view class="container">
		<!-- 浮动发布按钮 -->
		<button class="publish-btn" @click="goToPublish">
			<image class="publish-icon" src="/static/icons/publish.png" mode="aspectFit"/>
		</button>
		
		<!-- 顶部分段控制器 -->
		<view class="tabs">
			<view
				class="tab"
				:class="{ active: currentTab === 0 }"
				@click="switchTab(0)"
			>
				可报名
			</view>
			<view
				class="tab"
				:class="{ active: currentTab === 1 }"
				@click="switchTab(1)"
			>
				已报名
			</view>
			<view
				class="tab"
				:class="{ active: currentTab === 2 }"
				@click="switchTab(2)"
			>
				历史活动
			</view>
		</view>

		<!-- 内容区域 -->
		<view class="content">
			<!-- 可报名活动 -->
			<view v-if="currentTab === 0" class="activity-list">
				<view class="activity-item" v-for="item in availableActivities" :key="item.id">
					<view class="item-info">
						<text class="item-title">{{ item.title }}</text>
						<text class="item-desc">参与人数: {{ item.joined }}/{{ item.limit }}</text>
					</view>
					<button 
						class="action-button join" 
						:disabled="item.joined >= item.limit"
					>
						{{ item.joined >= item.limit ? '已满员' : '参加活动' }}
					</button>
				</view>
			</view>

			<!-- 已报名活动 -->
			<view v-if="currentTab === 1" class="activity-list">
				<view class="activity-item" v-for="item in registeredActivities" :key="item.id">
					<view class="item-info">
						<text class="item-title">{{ item.title }}</text>
						<text class="item-desc">您已报名</text>
					</view>
					<button class="action-button quit">退出活动</button>
				</view>
			</view>

			<!-- 历史活动 -->
			<view v-if="currentTab === 2" class="activity-list">
				<view class="activity-item" v-for="item in historyActivities" :key="item.id">
					<view class="item-info">
						<text class="item-title">{{ item.title }}</text>
						<text class="item-desc">活动已结束</text>
					</view>
					<button class="action-button disabled">已结束</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentTab: 0,
				availableActivities: [
					{ id: 1, title: '4月1日海滩清洁', joined: 15, limit: 20 },
					{ id: 2, title: '4月15日植树节活动', joined: 50, limit: 50 },
				],
				registeredActivities: [
					{ id: 3, title: '3月16日绿色知识竞赛' },
				],
				historyActivities: [
					{ id: 4, title: '3月8日社区打扫' },
				]
			};
		},
		methods: {
			switchTab(index) {
				this.currentTab = index;
			},
			goToPublish() {
				uni.navigateTo({
					url: '/pages/activities/publish'
				});
			}
		}
	}
</script>

<style scoped>
	.container {
		display: flex;
		flex-direction: column;
		position: relative;
		min-height: 100vh;
	}
	
	/* 顶部标题栏样式 */
	.header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 15px 20px;
		background-color: #fff;
		border-bottom: 1px solid #f0f0f0;
	}
	
	.page-title {
		font-size: 18px;
		font-weight: 600;
		color: #333;
	}
	
	.publish-btn {
		position: fixed;
		width: 70px;
		height: 70px;
		top: 572px;
		left: 269px;
		background: linear-gradient(to right, #B7EF68, #03E996);
		color: #fff;
		border: none;
		border-radius: 35px;
		font-size: 16px;
		font-weight: 500;
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
		z-index: 999;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.publish-btn:active {
		opacity: 0.8;
		transform: scale(0.95);
	}
	.tabs {
		display: flex;
		background-color: #fff;
		border-bottom: 1px solid #f0f0f0;
	}
	.tab {
		flex: 1;
		padding: 12px 0;
		text-align: center;
		font-size: 15px;
		color: #666;
		position: relative;
	}
	.tab.active {
		color: #3cc51f;
		font-weight: bold;
	}
	.tab.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 30%;
		height: 3px;
		background-color: #3cc51f;
		border-radius: 2px;
	}
	.content {
		padding: 15px;
	}
	.activity-list {
		display: flex;
		flex-direction: column;
		gap: 12px;
	}
	.activity-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		background-color: #fff;
		padding: 15px;
		border-radius: 8px;
		box-shadow: 0 2px 8px rgba(0,0,0,0.05);
	}
	.item-info {
		display: flex;
		flex-direction: column;
		gap: 5px;
	}
	.item-title {
		font-size: 16px;
		font-weight: 500;
		color: #333;
	}
	.item-desc {
		font-size: 13px;
		color: #888;
	}
	.action-button {
		font-size: 13px;
		height: 32px;
		line-height: 32px;
		padding: 0 15px;
		margin: 0;
	}
	.join {
		background-color: #3cc51f;
		color: #fff;
	}
	.join[disabled] {
		background-color: #ccc;
		color: #666;
	}
	.quit {
		background-color: #f44336;
		color: #fff;
	}
	.disabled {
		background-color: #e0e0e0;
		color: #999;
	}
</style>
