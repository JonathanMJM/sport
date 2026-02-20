<template>
  <view class="page-container">
    <view class="mesh-background"><view class="blob blob-1"></view><view class="blob blob-2"></view></view>

    <view class="content-wrapper">
        <!-- Header -->
        <view class="glass-card header-card" @click="goProfile">
            <view>
                <text class="welcome">Hi, {{user.name}}</text>
                <text class="sub">会员中心</text>
            </view>
            <view class="avatar">{{user.name ? user.name[0] : 'U'}}</view>
        </view>

        <!-- Navigation -->
        <view class="glass-card nav-card">
            <view class="nav-item" @click="tab='book'" :class="{active: tab==='book'}">
                <text class="nav-text" style="font-size: 16px; margin-bottom: 4px;">Book</text>
                <text class="nav-text">场馆预约</text>
            </view>
            <view class="nav-item" @click="loadMy" :class="{active: tab==='my'}">
                <text class="nav-text" style="font-size: 16px; margin-bottom: 4px;">My</text>
                <text class="nav-text">我的预约</text>
            </view>
            <view class="nav-item" @click="scanCode">
                <text class="nav-text" style="font-size: 16px; margin-bottom: 4px;">Scan</text>
                <text class="nav-text">扫码签到</text>
            </view>
        </view>
        
        <!-- Debug/Demo Toolbar -->
        <view style="margin-bottom: 12px; display: flex; justify-content: flex-end;">
            <button size="mini" type="primary" plain @click="demoSuccess" style="font-size: 12px;">模拟签到成功 (Demo)</button>
        </view>

        <!-- Venues List -->
        <view v-if="tab==='book'" class="list-area">
            <view class="section-title">开放场馆</view>
            <view v-for="v in list" :key="v.id" class="glass-card course-item">
                <view class="course-header">
                    <text class="course-name">{{v.name}}</text>
                    <text class="course-price">¥{{v.price}}/时</text>
                </view>
                <view class="course-body">
                    <text class="info-row">类型: {{v.type}} | 容量: {{v.capacity}}人</text>
                    <text class="info-row">位置: {{v.location}}</text>
                    <text class="info-row">开放: {{v.openTime}} - {{v.closeTime}}</text>
                </view>
                <!-- 简化流程，点击直接预订 -->
                <button class="action-btn btn-blue" @click="book(v)">立即预订</button>
            </view>
        </view>
        
        <!-- My Reservations -->
        <view v-if="tab==='my'" class="list-area">
            <view class="section-title">我的记录</view>
            <view v-for="r in bookings" :key="r.id" class="glass-card course-item">
                <view class="course-header">
                    <text class="course-name">{{r.targetName}}</text>
                    <text class="status-tag" :class="r.status">{{r.status}}</text>
                </view>
                <view class="course-body">
                    <text class="info-row" style="color:#666; font-size:12px;">时间: {{r.createTime}}</text>
                    <text class="info-row">类型: {{r.type==='VENUE'?'场地':'课程'}}</text>
                </view>
                <view class="action-area" v-if="r.status==='PAID'">
                    <button class="action-btn btn-green" @click="scanCode">到场签到</button>
                </view>
            </view>
            <view v-if="bookings.length===0" class="empty-tip">暂无记录</view>
        </view>
    </view>
  </view>
</template>

<script>
import { BASE_URL } from '../../common/config.js';
export default {
  data() { return { user:{}, list:[], bookings:[], tab:'book' } },
  onShow() {
    this.user = uni.getStorageSync('user');
    this.loadVenues();
  },
  methods: {
    loadVenues() { uni.request({ url: BASE_URL + '/student/venues', success: (res) => { this.list = res.data.data; } }); },
    loadMy() { this.tab = 'my'; uni.request({ url: BASE_URL + '/student/my?uid='+this.user.id, success: (res) => { this.bookings = res.data.data; } }); },
    
    // 预订场馆
    book(venue) { 
        // 简化：直接发起请求，实际应弹窗选择时间
        uni.showModal({
            title: '确认预订',
            content: `预订 ${venue.name}?`,
            success: (res) => {
                if (res.confirm) {
                    uni.request({ 
                        url: BASE_URL + '/student/book', 
                        method:'POST', 
                        data: {
                            userId: this.user.id, 
                            type: 'VENUE',
                            targetId: venue.id,
                            targetName: venue.name,
                            price: venue.price
                        }, 
                        success: (res) => { 
                            uni.showToast({title:res.data.msg, icon:'none'}); 
                        } 
                    });
                }
            }
        });
    },
    
    // 扫码签到
    scanCode() {
        uni.scanCode({
            success: (res) => {
                this.handleCheckIn(res.result);
            },
            fail: () => {
                this.showManualInput();
            }
        });
    },

    // 统一处理签到逻辑
    handleCheckIn(content) {
        let venueId = content;
        // 尝试解析JSON
        try {
            const data = JSON.parse(content);
            if(data.type === 'checkin' && data.venueId) {
                venueId = data.venueId;
            }
        } catch(e) {
            // 不是JSON，假设是纯数字ID
        }

        uni.showLoading({title:'签到中...'});
        uni.request({
            url: BASE_URL + '/student/checkin',
            method: 'POST',
            data: { uid: this.user.id, venueId: venueId },
            success: (apiRes) => {
                uni.hideLoading();
                if(apiRes.data.code === 200) {
                     uni.showToast({ title: '签到成功!', icon: 'success' });
                     // 震动反馈
                     uni.vibrateShort();
                } else {
                     uni.showToast({ title: apiRes.data.msg || '签到失败', icon: 'none' });
                }
            },
            fail: () => {
                uni.hideLoading();
                uni.showToast({ title: '网络请求失败', icon: 'none' });
            }
        });
    },

    showManualInput() {
        uni.showModal({
            title: '模拟扫码 (调试用)',
            editable: true,
            placeholderText: '输入场馆ID(如 1) 或 {"venueId":1}',
            success: (res) => {
                if(res.confirm && res.content) {
                    this.handleCheckIn(res.content);
                }
            }
        });
    },

    // 仅用于演示的一键成功按钮
    demoSuccess() {
        // 假设签到ID为1的篮球馆
        this.handleCheckIn('1');
    },

    goProfile() { uni.navigateTo({ url: '/pages/common/profile' }); },
  }
}
</script>

<style>
/* Same styles as before, adjusted slightly */
.page-container { min-height: 100vh; position: relative; overflow-y: auto; background-color: #f0f2f5; }
.mesh-background { position: fixed; top: 0; left: 0; width: 100%; height: 100%; z-index: 0; pointer-events: none; background-image: radial-gradient(at 0% 0%, rgba(255, 166, 158, 0.4) 0px, transparent 50%), radial-gradient(at 98% 1%, rgba(134, 239, 172, 0.4) 0px, transparent 50%); }
.blob { position: absolute; border-radius: 50%; filter: blur(60px); opacity: 0.6; }
.blob-1 { width: 300px; height: 300px; background: #e0e7ff; top: -50px; left: -50px; }
.blob-2 { width: 250px; height: 250px; background: #fce7f3; bottom: 0; right: 0; }
.content-wrapper { position: relative; z-index: 10; padding: 20px; }
.glass-card { background: rgba(255, 255, 255, 0.75); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 0.6); border-radius: 12px; box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05); margin-bottom: 16px; padding: 20px; }
.header-card { display: flex; justify-content: space-between; align-items: center; padding: 24px; }
.welcome { font-size: 20px; font-weight: bold; color: #1d1d1f; display: block; margin-bottom: 4px; }
.sub { font-size: 14px; color: #666; }
.avatar { width: 48px; height: 48px; background: #007aff; color: white; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; font-weight: bold; }
.nav-card { display: flex; justify-content: space-around; padding: 16px 0; }
.nav-item { display: flex; flex-direction: column; align-items: center; opacity: 0.7; transition: all 0.3s; }
.nav-item.active { opacity: 1; font-weight: bold; color: #007aff; }
.icon { font-size: 24px; margin-bottom: 4px; }
.nav-text { font-size: 13px; }
.section-title { font-size: 16px; font-weight: 700; color: #333; margin-bottom: 12px; margin-left: 4px; }
.course-item { padding: 16px; }
.course-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.course-name { font-size: 17px; font-weight: bold; color: #1d1d1f; }
.course-price { font-size: 16px; font-weight: bold; color: #ff6b00; }
.info-row { display: block; font-size: 13px; color: #666; margin-bottom: 4px; }
.action-btn { margin-top: 12px; border-radius: 8px; font-size: 14px; font-weight: 600; height: 40px; line-height: 40px; border: none; color: white; }
.btn-blue { background: #007aff; }
.btn-green { background: #34c759; }
.status-tag { font-size: 12px; padding: 2px 6px; border-radius: 4px; font-weight: bold; }
.status-tag.PAID { background: #ecfdf5; color: #34c759; }
.empty-tip { text-align: center; color: #999; margin-top: 40px; font-size: 14px; }
</style>