<template>
	<view class="content">
		<view class="header">课程评价</view>
		<picker :range="courses" range-key="courseName" @change="onCourseChange">
			<view class="picker">{{ selectedCourse ? selectedCourse.courseName : '请选择要评价的课程' }}</view>
		</picker>
		<view class="form-item" v-if="selectedCourse">
			<text>评分</text>
            <!-- Fixed: Use event binding for slider compatibility -->
			<slider :value="score" @change="onScoreChange" min="1" max="5" show-value />
		</view>
		<view class="form-item" v-if="selectedCourse">
			<textarea v-model="comment" placeholder="请输入您的评价..." class="textarea" />
		</view>
		<button class="btn" @click="submit" v-if="selectedCourse">提交评价</button>
	</view>
</template>
<script>
import { BASE_URL } from '../../common/config.js';
export default {
	data() { return { courses: [], selectedCourse: null, score: 5, comment: '' } },
	onShow() {
		const u = uni.getStorageSync('user');
		uni.request({ 
            url: BASE_URL + '/student/my?sid=' + u.id, 
            success: (res) => { 
                if(res.data.code === 200) {
                    // Filter PAID courses
                    this.courses = res.data.data.filter(c => c.status === 'PAID'); 
                }
            }
        });
	},
	methods: {
		onCourseChange(e) { this.selectedCourse = this.courses[e.detail.value]; },
        onScoreChange(e) { this.score = e.detail.value; },
		submit() {
			const u = uni.getStorageSync('user');
			uni.request({
				url: BASE_URL + '/student/evaluate',
				method: 'POST',
				data: { 
                    courseId: this.selectedCourse.cid, 
                    courseName: this.selectedCourse.courseName,
                    studentId: u.id, 
                    studentName: u.name,
                    // FIX: Include teacherId
                    teacherId: this.selectedCourse.teacherId,
                    teacherName: this.selectedCourse.teacherName,
                    score: this.score, 
                    comment: this.comment 
                },
				success: (res) => { uni.showToast({title: '评价成功'}); setTimeout(()=>uni.navigateBack(), 1000); }
			});
		}
	}
}
</script>
<style>
.content { padding: 20px; }
.header { font-size: 20px; font-weight: bold; margin-bottom: 20px; text-align: center; }
.picker { background: #fff; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: center; border: 1px solid #ddd; }
.form-item { margin-bottom: 20px; background: #fff; padding: 15px; border-radius: 8px; }
.textarea { width: 100%; height: 100px; margin-top: 10px; }
.btn { background: #007aff; color: white; }
</style>