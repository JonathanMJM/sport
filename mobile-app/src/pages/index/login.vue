<template>
	<view class="container" style="padding-top:80px; text-align:center;">
		<view style="font-size:32px; font-weight:900; color:#1890FF; margin-bottom:10px;">🏟️ 体育馆助手</view>
        <view style="font-size:20px; color:#999; margin-bottom:60px;">智能预约 · 快乐运动</view>

		<view class="card">
            <view style="text-align:left; font-size:18px; font-weight:bold; margin-bottom:10px;">会员登录</view>
			<input v-model="u" placeholder="请输入手机号/账号" style="font-size:18px; height:50px; border-bottom:1px solid #eee; margin-bottom:20px;"/>
			<input v-model="p" password placeholder="请输入密码" style="font-size:18px; height:50px; border-bottom:1px solid #eee; margin-bottom:30px;"/>
			<button class="btn btn-primary" @click="login">立即登录</button>
		</view>

        <view style="margin-top:30px; color:#666; font-size:16px;">
            <text @click="quick('student1')" style="margin-right:30px; text-decoration:underline;">我是会员(student1)</text>
            <text @click="quick('coach1')" style="text-decoration:underline;">我是教练(coach1)</text>
        </view>
	</view>
</template>
<script>
import { BASE_URL } from '@/common/config.js';
export default {
	data() { return {u:'', p:''} },
	methods: {
        quick(n) { this.u=n; this.p='123456'; },
		login() {
			uni.request({
				url: BASE_URL + '/login', method: 'POST', data: {username:this.u, password:this.p},
				success: (res) => {
					if(res.data.code === 200) {
						uni.setStorageSync('user', res.data.data);
						uni.reLaunch({ url: res.data.data.role==='coach' ? '/pages/teacher/mobile' : '/pages/student/home' });
					} else { uni.showToast({title:'账号错误',icon:'none'}); }
				},
                fail: () => uni.showToast({title:'网络错误',icon:'none'})
			})
		}
	}
}
</script>
<style>
.container{padding: 20px;}
.card{background:white; padding:20px; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.05);}
.btn-primary{background:#1890FF; color:white; border-radius:25px;}
</style>