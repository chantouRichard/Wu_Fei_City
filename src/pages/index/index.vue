<template>
	<view class="container">
		<!-- 顶部问候与插图 -->
		<view class="header-section">
			<view class="know-watermark">KNOW</view>
			<view class="header-content">
				<view class="greeting">
					<text class="main-greeting">Hi~ 上午好</text>
					<text class="sub-greeting">有什么问题都可以问我哦~</text>
				</view>
				<image src="/static/index/people.png" class="character-image" mode="aspectFit"></image>
			</view>
		</view>

		<!-- 搜索框 -->
		<view class="search-section">
			<view class="search-bar">
				<input type="text" placeholder-class="search-placeholder" placeholder="请输入你的疑问" class="search-input" />
				<button class="search-button">搜索</button>
			</view>
		</view>

		<!-- 功能卡片 -->
		<view class="feature-cards">
			<view class="card card-bank" @click="goToMyPage">
				<view class="card-text">
					<text class="card-title">我的绿植银行</text>
					<text class="card-subtitle">查看我的环保贡献</text>
				</view>
				<image src="/static/index/bank.png" class="card-image" mode="aspectFit"></image>
			</view>
			<view class="card card-ai" @click="goToChatPage">
				<view class="card-text">
					<text class="card-title">无废AI助手</text>
					<text class="card-subtitle">解答你的各种疑问</text>
				</view>
				<image src="/static/index/无废AI助手.png" class="card-image ai-image" mode="aspectFit"></image>
			</view>
		</view>

		<!-- 知识广场 -->
		<view class="knowledge-section">
			<text class="section-title">知识广场</text>
			<scroll-view class="tags-scroll" scroll-x="true" show-scrollbar="false">
				<view class="tags-pages">
					<view class="tags-page" v-for="(page, pageIdx) in tagPages" :key="pageIdx">
						<view class="tags-row" v-for="(row, rowIdx) in page" :key="rowIdx">
							<view v-for="(tag, index) in row" :key="index" class="tag" @click="goToKnowledgeDetail(tag)">
								<text class="tag-icon">#</text>
								<text class="tag-text">{{ tag.text }}</text>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 活动组织 -->
		<view class="activity-section">
			<view class="section-header" @click="goToActivitiesPage">
				<text class="section-title">活动组织</text>
				<view class="see-more-button">
					<text class="see-more">></text>
				</view>
			</view>
			<view class="activity-list">
				<view v-for="activity in activities" :key="activity.id" class="activity-card">
					<image :src="activity.image" class="activity-image" mode="aspectFill"></image>
					<view class="activity-details">
						<text class="activity-title">{{ activity.title }}</text>
						<text class="activity-subtitle">{{ activity.subtitle }}</text>
					</view>
					<view class="activity-participants">
						<view class="avatars-stack">
							<image class="avatar" v-for="(avatar, index) in activity.avatars" :key="index" :src="avatar.src"
								:style="{ zIndex: activity.avatars.length - index }"></image>
						</view>
						<view class="participant-count">
							<text class="count-text">+{{ activity.count }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				knowledgeTags: [{
					text: '乱扔电池的危害'
				}, {
					text: '固体废物分类'
				}, {
					text: '厨余垃圾的处理'
				}, {
					text: '危险废物的举报方式'
				}, ],
				activities: [{
					id: 1,
					title: '3月8日社区打扫',
					subtitle: '武汉大学法学院清扫',
					image: '/static/index/活动1.png',
					avatars: [{
						src: '/static/logo.png'
					}, {
						src: '/static/logo.png'
					}, ],
					count: 98
				}, {
					id: 2,
					title: '3月16日绿色知识竞赛',
					subtitle: '第五教学楼"绿途无废"知识竞赛',
					image: '/static/index/活动2.png',
					avatars: [{
						src: '/static/logo.png'
					}, {
						src: '/static/logo.png'
					}, ],
					count: 26
				}, ]
			}
		},
		computed: {
			tagPages() {
				// 每页8个标签，分两行，每行4个
				const perRow = 4;
				const perPage = 8;
				const pages = [];
				for (let i = 0; i < this.knowledgeTags.length; i += perPage) {
					const pageTags = this.knowledgeTags.slice(i, i + perPage);
					const page = [];
					for (let j = 0; j < pageTags.length; j += perRow) {
						page.push(pageTags.slice(j, j + perRow));
					}
					pages.push(page);
				}
				return pages;
			}
		},
		onLoad() {},
		methods: {
			goToMyPage() {
				uni.switchTab({
					url: '/pages/my/my'
				});
			},
			goToChatPage() {
				uni.switchTab({
					url: '/pages/chat/chat'
				});
			},
			goToKnowledgeDetail(tag) {
				uni.navigateTo({
					url: '/pages/knowledge-detail/knowledge-detail?title=' + encodeURIComponent(tag.text)
				});
			},
			goToActivitiesPage() {
				uni.navigateTo({
					url: '/pages/activities/activities'
				});
			}
		}
	}
</script>

<style scoped>
	.container {
		display: flex;
		flex-direction: column;
		background-color: #fdfdfd;
		padding: 0 16px 20px 16px;
	}

	.header-section {
		position: relative;
		width: calc(100% + 32px);
		margin-left: -16px;
		margin-right: -16px;
		height: 191px;
		background: linear-gradient(180deg, rgba(200, 255, 122, 0.7) 0%, rgba(205, 246, 148, 0) 100%);
		display: flex;
		align-items: center;
		padding: 0 24px;
		box-sizing: border-box;
		margin-bottom: -21px;
	}

	.header-content {
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 100%;
		z-index: 2;
	}

	.know-watermark {
		position: absolute;
		left: 50%;
		top: 55%;
		transform: translate(-50%, -50%);
		width: 100%;
		text-align: center;
		font-size: 110px;
		font-weight: 900;
		color: rgba(255, 255, 255, 0.6);
		z-index: 1;
		pointer-events: none;
	}

	.greeting {
		display: flex;
		flex-direction: column;
	}

	.main-greeting {
		font-size: 28px;
		font-weight: 600;
		color: rgba(27, 27, 27, 1);
		line-height: 40px;
	}

	.sub-greeting {
		font-size: 14px;
		font-weight: 400;
		color: rgba(27, 27, 27, 0.8);
		line-height: 20px;
		margin-top: 4px;
	}

	.character-image {
		width: 130px;
		height: 130px;
		margin-top: -20px;
		flex-shrink: 0;
	}

	.search-section {
		position: relative;
		z-index: 3;
	}

	.search-bar {
		display: flex;
		align-items: center;
		background-color: #fff;
		border-radius: 21px;
		box-shadow: 0px 8px 24px rgba(0, 0, 0, 0.05);
		height: 42px;
		padding: 0 4px 0 20px;
	}

	.search-input {
		flex: 1;
		font-size: 14px;
		color: #333;
	}

	.search-placeholder {
		color: #b2b2b2;
	}

	.search-button {
		display: flex;
		justify-content: center;
		align-items: center;
		background-color: #1f2329;
		color: #fff;
		border-radius: 17px;
		font-size: 14px;
		width: 65px;
		height: 34px;
		margin: 0;
		padding: 0;
		line-height: 1;
	}

	.feature-cards {
		display: flex;
		margin-top: 24px;
		gap: 12px;
	}

	.card {
		flex: 1;
		height: 92px;
		padding: 12px;
		border-radius: 12px;
		box-sizing: border-box;
		overflow: hidden;
		position: relative;
	}

	.card-bank {
		background: linear-gradient(131.85deg, rgba(252, 208, 187, 1) 0%, rgba(252, 208, 187, 0.17) 100%);
	}

	.card-ai {
		background: linear-gradient(131.67deg, rgba(195, 235, 138, 1) 0%, rgba(245, 255, 232, 1) 100%);
	}

	.card-text {
		display: flex;
		flex-direction: column;
		gap: 4px;
		position: relative;
		z-index: 2;
	}

	.card-title {
		font-size: 12px;
		font-weight: 600;
		color: #666;
	}

	.card-subtitle {
		font-size: 12px;
		color: #666;
	}

	.card-image {
		width: 80px;
		height: 80px;
		position: absolute;
		right: -10px;
		bottom: 10px;
		z-index: 1;
	}

	.ai-image {
		width: 80px;
		height: 80px;
		
		right: -10px;
		bottom: 10px;
	}

	.knowledge-section,
	.activity-section {
		margin-top: 30px;
	}

	.section-title {
		font-size: 18px;
		font-weight: 600;
		color: #333;
	}

	.tags-scroll {
		width: 100%;
		white-space: nowrap;
		margin-top: 16px;
		padding-bottom: 2px;
	}

	.tags-pages {
		display: flex;
		flex-direction: row;
	}

	.tags-page {
		min-width: 340px;
		margin-right: 16px;
		display: flex;
		flex-direction: column;
		gap: 8px;
	}

	.tags-row {
		display: flex;
		flex-direction: row;
		gap: 10px;
	}

	.tag {
		display: flex;
		align-items: center;
		background-color: #fff;
		border-radius: 18px;
		padding: 8px 16px;
		height: 36px;
		border: 1px solid #f7f7f7;
		box-sizing: border-box;
	}

	.tag-icon {
		color: #52c41a;
		font-weight: bold;
		margin-right: 6px;
	}
	
	.tag-text {
		font-size: 14px;
		color: #333;
	}

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16px;
	}

	.see-more-button {
		width: 24px;
		height: 24px;
		background-color: #f0f0f0;
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.see-more {
		font-size: 12px;
		color: #333;
		font-weight: bold;
	}

	.activity-list {
		display: flex;
		flex-direction: column;
		gap: 12px;
	}

	.activity-card {
		background: rgba(242, 249, 247, 1);
		border-radius: 12px;
		padding: 12px;
		display: flex;
		align-items: center;
		gap: 12px;
		height: 76px;
		box-sizing: border-box;
	}

	.activity-image {
		width: 52px;
		height: 52px;
		border-radius: 8px;
		flex-shrink: 0;
	}

	.activity-details {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
		gap: 4px;
	}

	.activity-title {
		font-size: 15px;
		font-weight: 600;
		color: #333;
	}

	.activity-subtitle {
		font-size: 12px;
		color: #999;
	}

	.activity-participants {
		display: flex;
		align-items: center;
		flex-shrink: 0;
	}

	.avatars-stack {
		display: flex;
	}

	.avatar {
		width: 24px;
		height: 24px;
		border-radius: 50%;
		border: 1px solid #fff;
		margin-left: -10px;
	}

	.avatar:first-child {
		margin-left: 0;
	}

	.participant-count {
		height: 24px;
		min-width: 24px;
		padding: 0 4px;
		border-radius: 12px;
		background-color: #1f2329;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-left: -10px;
		z-index: 10;
	}

	.count-text {
		font-size: 11px;
		color: #fff;
		font-weight: 500;
	}
</style>
