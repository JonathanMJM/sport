<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">签到与入场管理</span>
        <el-button type="primary" @click="openDialog">人工代签/补录</el-button>
      </div>
    </template>
    
    <div class="mb-4">
       <!-- 简单搜索栏 -->
       <el-input v-model="search" placeholder="搜索用户姓名/场馆" style="width: 250px; margin-right: 12px;"/>
       <el-button type="primary" icon="Search">筛选</el-button>
    </div>

    <el-table :data="filteredList" border stripe>
      <el-table-column prop="id" label="记录ID" width="80" />
      <el-table-column prop="userNo" label="用户账号" width="120" />
      <el-table-column prop="userName" label="姓名" width="120" />
      <el-table-column prop="venueName" label="所在场馆" width="150" />
      <el-table-column prop="checkInTime" label="签到时间" width="180">
         <template #default="{row}">{{ formatDate(row.checkInTime) }}</template>
      </el-table-column>
      <el-table-column prop="checkOutTime" label="签退时间" width="180">
         <template #default="{row}">{{ row.checkOutTime ? formatDate(row.checkOutTime) : '-' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
         <template #default="{row}">
            <el-tag v-if="row.status==='CHECKED_IN'" type="success" effect="dark">在场中</el-tag>
            <el-tag v-else-if="row.status==='LEFT'" type="info">已离场</el-tag>
            <el-tag v-else-if="row.status==='FORCE_OUT'" type="warning">强制签退</el-tag>
         </template>
      </el-table-column>
      <el-table-column label="操作" min-width="150">
        <template #default="{row}">
          <el-button 
            v-if="row.status==='CHECKED_IN'" 
            size="small" 
            type="danger" 
            plain 
            @click="forceOut(row.id)">
            强制签退
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 代签弹窗 -->
    <el-dialog v-model="dialog" title="人工代签入场" width="500px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-form-item label="选择用户">
           <el-select v-model="form.userId" filterable placeholder="输入姓名或账号搜索" style="width: 100%">
              <el-option v-for="u in users" :key="u.id" :label="u.name + ' (' + u.username + ')'" :value="u.id"/>
           </el-select>
        </el-form-item>
        <el-form-item label="选择场馆">
           <el-select v-model="form.venueId" placeholder="选择进入的场馆" style="width: 100%">
              <el-option v-for="v in venues" :key="v.id" :label="v.name" :value="v.id"/>
           </el-select>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="submit" type="primary">确认入场</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';

const list = ref([]);
const users = ref([]);
const venues = ref([]);
const search = ref('');
const dialog = ref(false);
const form = reactive({ userId: null, venueId: null });

const filteredList = computed(() => {
    if(!search.value) return list.value;
    const k = search.value.toLowerCase();
    return list.value.filter(i => 
        (i.userName && i.userName.toLowerCase().includes(k)) || 
        (i.venueName && i.venueName.toLowerCase().includes(k)) ||
        (i.userNo && i.userNo.toLowerCase().includes(k))
    );
});

const formatDate = (str) => {
    if(!str) return '';
    return new Date(str).toLocaleString();
}

const loadData = async () => {
    // 获取签到记录
    const res = await axios.get('http://localhost:8080/api/admin/checkins');
    list.value = res.data.data || [];
}

const loadOptions = async () => {
    // 获取用户和场馆列表供选择
    const uRes = await axios.get('http://localhost:8080/api/admin/users');
    users.value = uRes.data.data;
    const vRes = await axios.get('http://localhost:8080/api/admin/venues');
    venues.value = vRes.data.data;
}

const openDialog = () => {
    form.userId = null;
    form.venueId = null;
    loadOptions(); // 懒加载选项
    dialog.value = true;
}

const submit = async () => {
    if(!form.userId || !form.venueId) {
        ElMessage.warning('请选择用户和场馆');
        return;
    }
    try {
        const res = await axios.post('http://localhost:8080/api/admin/checkin', form);
        if(res.data.code === 200) {
            ElMessage.success('签到成功');
            dialog.value = false;
            loadData();
        } else {
            ElMessage.error(res.data.msg);
        }
    } catch(e) {
        ElMessage.error('操作失败');
    }
}

const forceOut = (id) => {
    ElMessageBox.confirm('确定要强制该用户离场吗？这将释放场馆名额。', '警告', {
        type: 'warning'
    }).then(async () => {
        await axios.post('http://localhost:8080/api/admin/checkout/force', { id });
        ElMessage.success('已强制签退');
        loadData();
    });
}

onMounted(loadData);
</script>
