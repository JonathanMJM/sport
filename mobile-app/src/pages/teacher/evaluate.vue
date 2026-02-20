<template>
	<view class="content">
		<view class="header">学员评价</view>
		<!-- 1. Select Course -->
        <view class="label">选择课程:</view>
		<picker :range="courses" range-key="name" @change="onCourseChange">
			<view class="picker">{{ selectedCourse ? selectedCourse.name : '点击选择课程' }}</view>
		</picker>
        
        <!-- 2. Select Student -->
        <view class="label" v-if="selectedCourse">选择学员:</view>
        <picker v-if="selectedCourse" :range="students" range-key="studentName" @change="onStudentChange">
			<view class="picker">{{ selectedStudent ? selectedStudent.studentName : '点击选择学员' }}</view>
		</picker>

		<view class="form-item" v-if="selectedStudent">
			<text>评分 (1-100)</text>
			<input type="number" v-model="score" class="input" />
		</view>
		<view class="form-item" v-if="selectedStudent">
			<textarea v-model="comment" placeholder="请输入给学员的评语..." class="textarea" />
		</view>
		<button class="btn" @click="submit" v-if="selectedStudent">提交评价</button>
	</view>
</template>
<script>
import { BASE_URL } from '../../common/config.js';
export default {
	data() { return { courses: [], students: [], selectedCourse: null, selectedStudent: null, score: 90, comment: '' } },
	onShow() {
		const u = uni.getStorageSync('user');
		uni.request({ 
            url: BASE_URL + '/teacher/courses?tid=' + u.id, 
            success: (res) => { this.courses = res.data.data; }
        });
	},
	methods: {
		onCourseChange(e) {
            this.selectedCourse = this.courses[e.detail.value];
            this.selectedStudent = null;
            uni.request({
                url: BASE_URL + '/teacher/students?cid=' + this.selectedCourse.id,
                success: (res) => { this.students = res.data.data; }
            });
        },
        onStudentChange(e) { this.selectedStudent = this.students[e.detail.value]; },
		submit() {
			const u = uni.getStorageSync('user');
			uni.request({
				url: BASE_URL + '/teacher/evaluate',
				method: 'POST',
				data: { 
                    courseId: this.selectedCourse.id, 
                    courseName: this.selectedCourse.name,
                    studentId: this.selectedStudent.student_id, 
                    studentName: this.selectedStudent.studentName,
                    teacherId: u.id,
                    teacherName: u.name,
                    score: this.score, 
                    comment: this.comment 
                },
				success: (res) => { uni.showToast({title: '评价成功'}); this.comment = ''; }
			});
		}
	}
}
</script>
<style>
.content { padding: 20px; }
.header { font-size: 20px; font-weight: bold; margin-bottom: 20px; text-align: center; }
.label { margin: 10px 0 5px 0; font-size: 14px; color: #666; }
.picker { background: #fff; padding: 12px; border-radius: 6px; margin-bottom: 10px; border: 1px solid #ddd; }
.form-item { margin-top: 20px; background: #fff; padding: 15px; border-radius: 8px; }
.input { border: 1px solid #eee; padding: 5px; margin-top: 5px; }
.textarea { width: 100%; height: 80px; margin-top: 10px; border: 1px solid #eee; padding: 5px; }
.btn { background: #007aff; color: white; margin-top: 20px; }
</style>