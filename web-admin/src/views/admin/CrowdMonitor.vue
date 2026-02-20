<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">📊 实时人流监测</span>
        <el-button type="success" @click="load">刷新数据</el-button>
      </div>
    </template>
    
    <div class="mb-6 flex gap-4">
      <el-card class="flex-1 bg-blue-50">
        <div class="text-gray-500">当前总在场人数</div>
        <div class="text-3xl font-bold text-blue-600">{{ totalActive }}</div>
      </el-card>
    </div>

    <el-table :data="venues" border stripe>
      <el-table-column prop="venueName" label="场馆" />
      <el-table-column prop="capacity" label="最大容量" width="120" />
      <el-table-column prop="count" label="当前人数" width="120" />
      <el-table-column prop="status" label="拥挤度" width="120">
        <template #default="{row}">
          <el-tag :type="row.status==='CRITICAL'?'danger':(row.status==='WARNING'?'warning':'success')">
             {{ row.status === 'CRITICAL' ? '爆满' : (row.status === 'WARNING' ? '拥挤' : '舒适') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="负荷率">
        <template #default="{row}">
          <el-progress :percentage="Math.min(Math.round(row.count/row.capacity*100), 100)" 
             :status="row.status==='CRITICAL'?'exception':(row.status==='WARNING'?'warning':'success')" />
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'; import axios from 'axios';
const totalActive = ref(0);
const venues = ref([]);

const load = async () => { 
  try {
      const res = await axios.get('http://localhost:8080/api/admin/dashboard/crowd');
      totalActive.value = res.data.data.totalActive;
      venues.value = res.data.data.venues;
  } catch (e) {
      console.error(e);
  }
}
onMounted(load);
</script>
