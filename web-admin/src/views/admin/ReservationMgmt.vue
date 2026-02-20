<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">📅 预约与订单管理</span>
        <el-input v-model="search" placeholder="搜索用户或订单号" style="width: 200px" />
      </div>
    </template>
    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="订单号" width="80" />
      <el-table-column prop="userName" label="用户" width="100" />
      <el-table-column prop="type" label="类型" width="100">
         <template #default="{row}">
           <el-tag>{{row.type==='VENUE'?'场地':'课程'}}</el-tag>
         </template>
      </el-table-column>
      <el-table-column prop="targetName" label="预约项目" />
      <el-table-column label="时间段" width="200">
        <template #default="{row}">
           {{ formatTime(row.startTime) }} - {{ formatTime(row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="price" label="金额" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status==='PAID'?'success':'warning'">{{row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{row}">
          <el-button size="small" v-if="row.status==='UNPAID'" type="primary">确认支付</el-button>
          <el-button size="small" type="danger">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'; import axios from 'axios';
const list = ref([]); const search = ref('');

// 模拟数据接口，实际应调用 fetch
// const load = async () => { const res = await axios.get('http://localhost:8080/api/admin/reservations'); list.value = res.data.data; }
// 由于后端 admin 接口未完全覆盖所有 reservation 查询，这里暂时用 enrollment 接口模拟或留空
const load = async () => {
    // 暂时不做
}
const formatTime = (t) => { return t ? t.replace('T', ' ') : '' }
onMounted(load);
</script>
