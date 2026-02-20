<template>
  <el-card>
    <template #header><div class="font-bold">🗣️ 评价反馈管理</div></template>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{row}">
            <el-tag v-if="row.type==='S2T'" type="success">学生评教</el-tag>
            <el-tag v-else-if="row.type==='T2S'" type="warning">教师评学</el-tag>
            <el-tag v-else type="info">未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="courseName" label="课程" />
      <el-table-column prop="studentName" label="学生姓名" />
      <el-table-column prop="score" label="评分" width="80">
        <template #default="{row}"><el-rate v-model="row.score" disabled /></template>
      </el-table-column>
      <el-table-column prop="comment" label="评语" />
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="120">
        <template #default="{row}">
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'; import axios from 'axios'; import { ElMessage, ElMessageBox } from 'element-plus';
const list = ref([]);
const load = async () => { const res = await axios.get('http://localhost:8080/api/admin/evaluations'); list.value = res.data.data; }
const del = (id) => {
    ElMessageBox.confirm('确定删除这条评价吗？').then(async () => {
        await axios.post('http://localhost:8080/api/admin/evaluation/delete', {id});
        ElMessage.success('已删除');
        load();
    });
}
onMounted(load);
</script>