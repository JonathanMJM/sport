<template>
	<view class="content">
		<view class="header">
			<view class="avatar">{{ user.name ? user.name[0] : 'U' }}</view>
			<view class="name">{{ user.name }}</view>
		</view>
		<view class="card">
			<view class="form-item"><text>姓名</text><input v-model="form.name" class="input"/></view>
			<button class="btn" @click="updateInfo">保存信息</button>
		</view>
		<view class="card">
			<view class="form-item"><text>新密码</text><input v-model="pwd" password class="input"/></view>
			<button class="btn warn" @click="updatePwd">修改密码</button>
		</view>
		<button class="btn logout" @click="logout">退出登录</button>
	</view>
</template>
<script>
import { BASE_URL } from '@/common/config.js';
export default {
	data() { return { user: {}, form: {name:''}, pwd: '' } },
	onShow() {
		this.user = uni.getStorageSync('user');
		this.form = { ...this.user };
	},
	methods: {
		updateInfo() {
			uni.request({ url: BASE_URL + '/common/profile', method: 'POST', data: this.form, success: (res) => { uni.setStorageSync('user', res.data.data); uni.showToast({title:'已保存'}); } });
		},
		updatePwd() {
			uni.request({ url: BASE_URL + '/common/password', method: 'POST', data: {id:this.user.id, password:this.pwd}, success: () => uni.showToast({title:'密码已修改'}) });
		},
		logout() { uni.removeStorageSync('user'); uni.reLaunch({url:'/pages/index/login'}); }
	}
}
</script>
<style>
.content { padding: 20px; }
.header { text-align: center; margin-bottom: 30px; }
.avatar { width: 80px; height: 80px; border-radius: 50%; background: #007aff; color: white; line-height: 80px; margin: 0 auto; font-size: 30px; }
.card { background: white; padding: 20px; border-radius: 10px; margin-bottom: 20px; }
.form-item { display: flex; align-items: center; margin-bottom: 15px; border-bottom: 1px solid #eee; padding-bottom: 10px; }
.input { flex: 1; text-align: right; }
.btn { background: #007aff; color: white; margin-top: 10px; }
.btn.warn { background: #ff9500; }
.btn.logout { background: #ff3b30; }
</style>