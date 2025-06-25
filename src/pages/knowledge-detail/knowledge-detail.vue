<template>
	<view class="container">
		
		<!-- 页面标题 -->
		<view class="title-container">
			<text class="title">{{ pageTitle }}</text>
		</view>
		
		<!-- 内容区域 -->
		<view class="content-box">
			<view class="content-card">
				<text class="paragraph">
					这里是关于"{{ pageTitle }}"的详细科普内容。
				</text>
				<view class="divider"></view>
				<text class="paragraph">
					乱扔电池会对环境造成严重的危害。废旧电池中含有大量的重金属，如汞、镉、铅、锌等，以及酸、碱等电解质溶液。
				</text>
				<view class="divider"></view>
				<text class="paragraph">
					如果将废电池随意丢弃，这些有害物质会慢慢从电池中泄露出来，进入土壤或水源，再通过农作物进入人的食物链。这些有毒物质在人体内会长期积蓄，难以排出，损害人的神经系统、造血功能、肾脏和骨骼，甚至可能致癌。
				</text>
			</view>
			
			<!-- 环保小贴士 -->
			<view class="tip-card">
				<view class="tip-header">
					<text class="tip-title">环保小贴士</text>
				</view>
				<text class="tip-content">
					废旧电池应投放到专门的回收箱，许多小区和超市都设有回收点。让我们一起保护环境！
				</text>
			</view>
		</view>
		
		<!-- 底部装饰 -->
		<view class="footer">
			<text class="footer-text">绿色生活 · 从我做起</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				pageTitle: ''
			}
		},
		onLoad(options) {
			if (options.title) {
				this.pageTitle = decodeURIComponent(options.title);
				uni.setNavigationBarTitle({
					title: this.pageTitle
				});
			}
		}
	}
</script>

<style scoped>
	.container {
		padding: 0;
		background-color: #f5faf7;
		min-height: 100vh;
		display: flex;
		flex-direction: column;
	}
	
	.title-container {
		display: flex;
		align-items: center;
		padding: 30rpx;
		background-color: #fff;
		margin: 20rpx;
		border-radius: 16rpx;
		box-shadow: 0 4rpx 12rpx rgba(76, 175, 80, 0.1);
	}
	
	.title {
		font-size: 36rpx;
		font-weight: bold;
		color: #2E7D32;
	}
	
	.content-box {
		flex: 1;
		padding: 0 30rpx;
	}
	
	.content-card {
		background-color: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 4rpx 12rpx rgba(76, 175, 80, 0.1);
	}
	
	.paragraph {
		display: block;
		font-size: 30rpx;
		line-height: 1.8;
		color: #455A64;
		margin-bottom: 30rpx;
	}
	
	.divider {
		height: 2rpx;
		background-color: #E8F5E9;
		margin: 30rpx 0;
	}
	
	.tip-card {
		background-color: #E8F5E9;
		border-radius: 16rpx;
		padding: 30rpx;
		border-left: 8rpx solid #4CAF50;
	}
	
	.tip-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
	}
	
	.tip-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #2E7D32;
	}
	
	.tip-content {
		font-size: 28rpx;
		line-height: 1.6;
		color: #455A64;
	}
	
	.footer {
		padding: 30rpx;
		text-align: center;
		background-color: #4CAF50;
		margin-top: 40rpx;
	}
	
	.footer-text {
		color: #fff;
		font-size: 28rpx;
	}
</style>