<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">考勤管理（教师）</span>
        <span style="font-size:12px;color:#999;">按课程查看并编辑本人课程的签到</span>
      </div>
    </template>

    <!-- 先选课程，再查看可签到成员及状态 -->
    <div style="margin-bottom:16px; display:flex; align-items:center; gap:12px; flex-wrap:wrap;">
      <el-select v-model="courseId" placeholder="选择课程" clearable style="width:260px;" @change="loadCourseMembers">
        <el-option v-for="c in courses" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <span style="font-size:12px;color:#999;">当前成员：{{ list.length }} 人</span>
    </div>

    <el-table :data="list" border stripe v-if="courseId">
      <el-table-column prop="studentId" label="学员ID" width="100" />
      <el-table-column prop="studentName" label="学员姓名" width="160" />
      <el-table-column prop="status" label="签到状态" width="140">
        <template #default="{row}">
          <el-tag :type="row.status === 'SIGNED' ? 'success' : 'info'">
            {{ row.status === 'SIGNED' ? '已签到' : '未签到' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="checkInTime" label="最近签到时间" width="200" />
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button 
            v-if="row.status === 'UNSIGNED'" 
            size="small" type="primary" 
            @click="setStatus(row, 'SIGNED')"
          >签到</el-button>
          <el-button 
            v-else 
            size="small" type="danger" 
            @click="setStatus(row, 'UNSIGNED')"
          >取消签到</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-else style="padding:40px 0; text-align:center; color:#999; font-size:13px;">
      请选择课程后查看可签到学员列表
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

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

const loadCourseMembers = async () => {
  if (!courseId.value) {
    list.value = [];
    return;
  }
  const res = await axios.get('http://localhost:8080/api/admin/attendance', {
    params: { courseId: courseId.value }
  });
  list.value = res.data.data || [];
};

const setStatus = async (row, status) => {
  const payload = { cid: courseId.value, sid: row.studentId, status };
  const res = await axios.post('http://localhost:8080/api/admin/attendance/setStatus', payload);
  if (res.data.code === 200) {
    ElMessage.success(res.data.msg || '操作成功');
    loadCourseMembers();
  } else {
    ElMessage.error(res.data.msg || '操作失败');
  }
};

onMounted(() => {
  loadCourses();
});
</script>

