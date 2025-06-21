<template>
	<view class="container">
		<h1 class="title">{{ pageTitle }}</h1>
		<view class="content">
			<text class="paragraph">
				这里是关于"{{ pageTitle }}"的详细科普内容。
			</text>
			<text class="paragraph">
				（此处为占位内容）乱扔电池会对环境造成严重的危害。废旧电池中含有大量的重金属，如汞、镉、铅、锌等，以及酸、碱等电解质溶液。
			</text>
			<text class="paragraph">
								如果将废电池随意丢弃，这些有害物质会慢慢从电池中泄露出来，进入土壤或水源，再通过农作物进入人的食物链。这些有毒物质在人体内会长期积蓄，难以排出，损害人的神经系统、造血功能、肾脏和骨骼，甚至可能致癌。
			</text>
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
		padding: 20px;
		background-color: #fff;
		min-height: 100vh;
	}
	.title {
		font-size: 24px;
		font-weight: bold;
		margin-bottom: 20px;
		color: #333;
	}
	.content {
		font-size: 16px;
		line-height: 1.8;
		color: #555;
	}
	.paragraph {
		display: block;
		margin-bottom: 15px;
	}
</style> 