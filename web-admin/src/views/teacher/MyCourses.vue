<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">我的课程</span>
      </div>
    </template>
    <el-table :data="courses" border>
      <el-table-column prop="name" label="课程" />
      <el-table-column prop="timeSlot" label="时间段" />
      <el-table-column prop="classroom" label="教室" />
      <el-table-column prop="status" label="状态">
        <template #default="{row}">
          <el-tag :type="row.status === 'PUBLISHED' ? 'success' : 'info'">
            {{ row.status === 'PUBLISHED' ? '已上架' : '未上架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="toggle(row)">
            {{ row.status === 'PUBLISHED' ? '下架课程' : '上架课程' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const user = JSON.parse(localStorage.getItem('user') || '{}');
const courses = ref([]);

// 使用后端定义的参数：tid，并调用正确的接口路径
const load = async () => {
  const res = await axios.get(`http://localhost:8080/api/teacher/courses`, {
    params: { tid: user.id }
  });
  courses.value = res.data.data || [];
};

const toggle = async (row) => {
  const nextStatus = row.status === 'PUBLISHED' ? 'DRAFT' : 'PUBLISHED';
  await axios.post('http://localhost:8080/api/teacher/status', {
    id: row.id,
    status: nextStatus
  });
  await load();
};

onMounted(load);
</script>