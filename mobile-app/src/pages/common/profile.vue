<template>
	<view class="content">
		<view class="header">
			<view class="avatar">{{ user.name ? user.name[0] : 'U' }}</view>
			<view class="name">{{ user.name }}</view>
			<view class="role">{{ roleName }}</view>
		</view>

		<view class="card">
			<view class="card-title">基本信息修改</view>
			<view class="form-item">
				<text class="label">姓名</text>
				<input class="input" v-model="form.name" />
			</view>
			<view class="form-item">
				<text class="label">账号</text>
				<input class="input" v-model="user.username" disabled />
			</view>
			<button class="btn-primary" @click="updateInfo">保存信息</button>
		</view>

		<view class="card">
			<view class="card-title">安全设置</view>
			<view class="form-item">
				<text class="label">新密码</text>
				<input class="input" v-model="pwd" password placeholder="如果不修改请留空" />
			</view>
			<button class="btn-warn" @click="updatePwd">修改密码</button>
		</view>

		<button class="btn-logout" @click="logout">退出登录</button>
	</view>
</template>

<script>
    // 修复：使用解构导入，因为 config.js 是 export const BASE_URL
	import { BASE_URL } from '../../common/config.js';

	export default {
		data() {
			return {
				user: {},
				form: { name: '', id: null, role: '' },
				pwd: ''
			}
		},
		onShow() {
			const u = uni.getStorageSync('user');
			if (u) {
				this.user = u;
				this.form.name = u.name;
				this.form.id = u.id;
				this.form.role = u.role;
			} else {
				uni.redirectTo({ url: '../login/login' });
			}
		},
		computed: {
			roleName() {
				const map = { 'student': '学员', 'teacher': '教师', 'admin': '管理员' };
				return map[this.user.role] || this.user.role;
			}
		},
		methods: {
			updateInfo() {
				uni.request({
					url: BASE_URL + '/common/profile',
					method: 'POST',
					data: { id: this.user.id, name: this.form.name, role: this.user.role, status: this.user.status, balance: this.user.balance },
					success: (res) => {
						if (res.data.code === 200) {
							uni.setStorageSync('user', res.data.data);
							this.user = res.data.data;
							uni.showToast({ title: '信息已更新' });
						}
					}
				});
			},
			updatePwd() {
				if (!this.pwd) return uni.showToast({ title: '请输入新密码', icon: 'none' });
				uni.request({
					url: BASE_URL + '/common/password',
					method: 'POST',
					data: { id: this.user.id, password: this.pwd },
					success: (res) => {
						if (res.data.code === 200) uni.showToast({ title: '密码已修改' });
					}
				});
			},
			logout() {
				uni.removeStorageSync('user');
				uni.reLaunch({ url: '../index/login' });
			}
		}
	}
</script>

<style>
	.content { padding: 20px; background-color: #f8f9fa; min-height: 100vh; }
	.header { display: flex; flex-direction: column; align-items: center; margin-bottom: 30px; }
	.avatar { width: 80px; height: 80px; border-radius: 40px; background-color: #007aff; color: #fff; font-size: 32px; display: flex; align-items: center; justify-content: center; margin-bottom: 10px; box-shadow: 0 4px 10px rgba(0,122,255,0.3); }
	.name { font-size: 18px; font-weight: bold; margin-bottom: 5px; }
	.role { font-size: 14px; color: #666; background: #e0e0e0; padding: 2px 8px; border-radius: 4px; }
	.card { background: #fff; border-radius: 12px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
	.card-title { font-size: 16px; font-weight: bold; margin-bottom: 15px; border-left: 4px solid #007aff; padding-left: 10px; }
	.form-item { margin-bottom: 15px; display: flex; align-items: center; }
	.label { width: 60px; font-size: 14px; color: #333; }
	.input { flex: 1; border: 1px solid #eee; padding: 8px; border-radius: 6px; font-size: 14px; }
	.btn-primary { background-color: #007aff; color: #fff; font-size: 14px; margin-top: 10px; }
	.btn-warn { background-color: #ff9500; color: #fff; font-size: 14px; margin-top: 10px; }
	.btn-logout { background-color: #ff3b30; color: #fff; margin-top: 30px; }
</style>