<template>
	<view style="padding:20px;">
		<view style="font-size:24px;font-weight:bold;margin-bottom:20px;">📱 教师工作台</view>
		<view style="background:#fff;padding:20px;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.1);">
			<view style="font-size:18px;margin-bottom:10px;">今日课程：书法基础</view>
            <view style="color:#666;">状态：进行中</view>
			<button type="primary" style="margin-top:20px;width:100%;">发起签到</button>
		</view>
        <button @click="logout" type="warn" style="margin-top:40px;">退出登录</button>
	</view>
</template>
<script>
export default {
    methods: { logout() { uni.reLaunch({url:'/pages/index/login'}) } }
}
</script>