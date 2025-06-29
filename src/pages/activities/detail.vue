<template>
	<view class="container">
		<!-- 顶部导航 -->
		<view class="header">
			<view class="back-btn" @click="goBack">
				<text class="back-icon">‹</text>
			</view>
			<view class="title">发起活动</view>
			<view class="more-btn">
				<text class="more-icon">⋯</text>
			</view>
			<view class="record-btn">
				<view class="record-icon"></view>
			</view>
		</view>

		<!-- Tab 切换 -->
		<view class="tab-container">
			<view class="tab-item" :class="{active: activeTab === 'basic'}" @click="switchTab('basic')">
				基本资料
			</view>
			<view class="tab-item" :class="{active: activeTab === 'detail'}" @click="switchTab('detail')">
				详细介绍
			</view>
		</view>

		<!-- 详细介绍内容 -->
		<view class="content">
			<!-- 活动介绍输入框 -->
			<view class="intro-section">
				<textarea 
					v-model="activityIntro" 
					class="intro-textarea" 
					placeholder="详细活动介绍..."
					placeholder-class="placeholder"
					maxlength="500"
				></textarea>
			</view>

			<!-- 图片上传区域 -->
			<view class="image-section">
				<view class="image-upload" @click="chooseImage">
					<view class="upload-icon">+</view>
				</view>
			</view>
		</view>

		<!-- 底部发布按钮 -->
		<view class="bottom-section">
			<button class="publish-btn" :class="{published: isPublished}" @click="publishActivity" :disabled="isPublished">
				{{ isPublished ? '已发布' : '发布' }}
			</button>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			activeTab: 'detail',
			activityIntro: '',
			activityData: null,
			isPublished: false
		};
	},
	onLoad(options) {
		// 接收从发布页面传递的活动数据
		if (options.activityData) {
			try {
				this.activityData = JSON.parse(decodeURIComponent(options.activityData));
			} catch (e) {
				console.error('解析活动数据失败:', e);
			}
		}
	},
	methods: {
		goBack() {
			uni.navigateBack();
		},
		switchTab(tab) {
			if (tab === 'basic') {
				// 返回基本资料页面
				uni.navigateBack();
			} else {
				this.activeTab = tab;
			}
		},
		chooseImage() {
			uni.chooseImage({
				count: 1,
				sizeType: ['original', 'compressed'],
				sourceType: ['album', 'camera'],
				success: (res) => {
					console.log('选择图片成功:', res);
					// 这里可以处理图片上传逻辑
				}
			});
		},
		publishActivity() {
			if (this.isPublished) {
				return;
			}

			// 合并基本信息和详细介绍
			const completeActivity = {
				...this.activityData,
				introduction: this.activityIntro,
				createdAt: new Date().toISOString()
			};

			// 保存完整的活动信息
			let savedActivities = uni.getStorageSync('publishedActivities') || [];
			savedActivities.push(completeActivity);
			uni.setStorageSync('publishedActivities', savedActivities);

			// 更新发布状态
			this.isPublished = true;

			// 显示成功提示
			uni.showToast({
				title: '活动发布成功',
				icon: 'success'
			});
		}
	}
};
</script>

<style scoped>
.container {
	background-color: #f5f5f5;
	min-height: 100vh;
}

/* 顶部导航 */
.header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 10px 15px;
	background-color: #fff;
	border-bottom: 1px solid #e5e5e5;
}

.back-btn {
	width: 40px;
	height: 40px;
	display: flex;
	align-items: center;
	justify-content: center;
}

.back-icon {
	font-size: 24px;
	color: #333;
	font-weight: bold;
}

.title {
	font-size: 18px;
	font-weight: 600;
	color: #333;
}

.more-btn {
	width: 40px;
	height: 40px;
	display: flex;
	align-items: center;
	justify-content: center;
}

.more-icon {
	font-size: 20px;
	color: #666;
}

.record-btn {
	width: 40px;
	height: 40px;
	display: flex;
	align-items: center;
	justify-content: center;
}

.record-icon {
	width: 20px;
	height: 20px;
	border: 2px solid #333;
	border-radius: 50%;
	position: relative;
}

.record-icon::after {
	content: '';
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 8px;
	height: 8px;
	background-color: #333;
	border-radius: 50%;
}

/* Tab 切换 */
.tab-container {
	display: flex;
	background-color: #fff;
	border-bottom: 1px solid #e5e5e5;
}

.tab-item {
	flex: 1;
	padding: 15px 0;
	text-align: center;
	font-size: 16px;
	color: #666;
	position: relative;
}

.tab-item.active {
	color: #333;
	font-weight: 600;
}

.tab-item.active::after {
	content: '';
	position: absolute;
	bottom: 0;
	left: 50%;
	transform: translateX(-50%);
	width: 30px;
	height: 2px;
	background-color: #007aff;
}

/* 内容区域 */
.content {
	padding: 20px;
	flex: 1;
}

.intro-section {
	margin-bottom: 20px;
}

.intro-textarea {
	width: 100%;
	height: 200px;
	padding: 15px;
	background-color: #fff;
	border-radius: 8px;
	border: 1px solid #e5e5e5;
	font-size: 16px;
	line-height: 1.5;
	box-sizing: border-box;
	text-align: right;
}

.placeholder {
	color: #999;
	text-align: right;
}

.image-section {
	margin-bottom: 20px;
}

.image-upload {
	width: 121px;
	height: 121px;
	top: 305px;
	left: 32px;
	background-color: #f0f0f0;
	border: 2px dashed #ccc;
	border-radius: 8px;
	display: flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
}

.upload-icon {
	font-size: 40px;
	color: #999;
	font-weight: 300;
}

/* 底部按钮 */
.bottom-section {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	padding: 20px;
	background-color: #f5f5f5;
}

.publish-btn {
	width: 100%;
	height: 50px;
	background: linear-gradient(135deg, #b7ef68, #03e996);
	color: #fff;
	border: none;
	border-radius: 25px;
	font-size: 18px;
	font-weight: 600;
	box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.publish-btn:active {
	transform: translateY(1px);
	box-shadow: 0 2px 10px rgba(102, 126, 234, 0.3);
}

.publish-btn.published {
	background: #fff;
	color: #764ba2;
	border: 2px solid #764ba2;
	box-shadow: none;
}

.publish-btn.published:active {
	transform: none;
	box-shadow: none;
}
</style>