<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">课程报名信息（教师）</span>
        <span style="font-size:12px;color:#999;">查看本人课程的报名名单和缴费状态</span>
      </div>
    </template>

    <div style="margin-bottom:16px; display:flex; align-items:center; gap:12px; flex-wrap:wrap;">
      <el-select v-model="courseId" placeholder="选择课程" clearable style="width:260px;" @change="load">
        <el-option v-for="c in courses" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <span style="font-size:12px;color:#999;">当前报名人数：{{ list.length }} 人</span>
    </div>

    <el-table :data="list" border stripe v-if="courseId">
      <el-table-column prop="studentId" label="学员ID" width="100" />
      <el-table-column prop="studentName" label="学员姓名" width="160" />
      <el-table-column prop="status" label="缴费状态" width="140">
        <template #default="{row}">
          <el-tag :type="row.status === 'PAID' ? 'success' : 'warning'">
            {{ row.status === 'PAID' ? '已缴费' : '待缴费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="报名时间" width="200" />
    </el-table>

    <div v-else style="padding:40px 0; text-align:center; color:#999; font-size:13px;">
      请选择课程后查看报名名单
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const user = JSON.parse(localStorage.getItem('user') || '{}');
const courses = ref([]);
const courseId = ref(null);
const list = ref([]);

const loadCourses = async () => {
  const res = await axios.get('http://localhost:8080/api/teacher/courses', {
    params: { tid: user.id }
  });
  courses.value = res.data.data || [];
};

const load = async () => {
  if (!courseId.value) {
    list.value = [];
    return;
  }
  const res = await axios.get('http://localhost:8080/api/teacher/enrollments', {
    params: { cid: courseId.value }
  });
  list.value = res.data.data || [];
};

onMounted(() => {
  loadCourses();
});
</script>

