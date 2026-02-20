<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">用户管理</span>
        <el-button type="primary" @click="openAdd">新增用户</el-button>
      </div>
    </template>

    <div class="mb-4">
        <el-input v-model="search" placeholder="请输入用户名搜索" style="width: 200px; margin-right: 10px;" />
        <el-button type="primary" icon="Search">搜索</el-button>
    </div>

    <el-table :data="filteredList" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="账号" width="120" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{row}">
            <el-tag v-if="row.role==='admin'" type="danger">管理员</el-tag>
            <el-tag v-if="row.role==='teacher'" type="warning">教师</el-tag>
            <el-tag v-if="row.role==='student'" type="success">学员</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
            <el-tag v-if="row.status==='NORMAL'" type="success">在职/读</el-tag>
            <el-tag v-if="row.status==='GRADUATED'" type="info">毕业</el-tag>
            <el-tag v-if="row.status==='RETIRED'" type="info">离职</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="250">
        <template #default="{row}">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="warning" @click="resetPwd(row)">重置密码</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="form.id ? '编辑用户' : '新增用户'" width="600px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-form-item label="账号"><el-input v-model="form.username" :disabled="!!form.id"/></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.name"/></el-form-item>
        <el-form-item label="角色">
            <el-select v-model="form.role">
                <el-option label="管理员" value="admin" /><el-option label="教师" value="teacher" /><el-option label="学员" value="student" />
            </el-select>
        </el-form-item>
        <el-form-item label="状态">
            <el-select v-model="form.status">
                <el-option label="正常/在读" value="NORMAL" />
                <el-option label="毕业" value="GRADUATED" />
                <el-option label="离职/退休" value="RETIRED" />
            </el-select>
        </el-form-item>

      </el-form>
      <template #footer><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>
  </el-card>
</template>
<script setup>
import { ref, reactive, computed, onMounted } from 'vue'; import axios from 'axios'; import { ElMessage, ElMessageBox } from 'element-plus';
const list = ref([]); const search = ref(''); const dialog = ref(false);
const form = reactive({ id: null, username: '', name: '', role: 'student', status:'NORMAL' });
const filteredList = computed(() => list.value.filter(u => u.username.includes(search.value) || u.name.includes(search.value)));
const load = async () => { const res = await axios.get('http://localhost:8080/api/admin/users'); list.value = res.data.data; }
const openAdd = () => { Object.assign(form, {id:null, username:'', name:'', role:'student', status:'NORMAL'}); dialog.value = true; }
const openEdit = (row) => { 
  Object.assign(form, {
    id: row.id,
    username: row.username || '',
    name: row.name || '',
    role: row.role || 'student',
    status: row.status || 'NORMAL'
  }); 
  dialog.value = true; 
}
const submit = async () => { 
  console.log('开始保存用户，表单数据:', JSON.stringify(form));
  try {
    // 确保必填字段存在
    if (!form.username || form.username.trim() === '') {
      ElMessage.warning('请输入账号');
      return;
    }
    if (!form.name || form.name.trim() === '') {
      ElMessage.warning('请输入姓名');
      return;
    }
    if (!form.role) {
      ElMessage.warning('请选择角色');
      return;
    }
    if (!form.status) {
      ElMessage.warning('请选择状态');
      return;
    }

    console.log('发送请求，数据:', JSON.stringify(form));
    const res = await axios.post('http://localhost:8080/api/admin/user', form);
    console.log('响应结果:', res.data);
    if (res.data && res.data.code === 200) {
      ElMessage.success(res.data.msg || '保存成功');
      dialog.value = false; 
      load();
    } else {
      ElMessage.error(res.data?.msg || '保存失败，服务器返回异常');
    }
  } catch (error) {
    console.error('保存用户异常详情:', error);
    console.error('错误响应:', error.response);
    let errorMsg = '保存失败';
    if (error.response) {
      if (error.response.data) {
        if (typeof error.response.data === 'string') {
          errorMsg = error.response.data;
        } else if (error.response.data.msg) {
          errorMsg = error.response.data.msg;
        } else if (error.response.data.message) {
          errorMsg = error.response.data.message;
        } else {
          errorMsg = JSON.stringify(error.response.data);
        }
      } else {
        errorMsg = `请求失败: ${error.response.status} ${error.response.statusText}`;
      }
    } else if (error.message) {
      errorMsg = error.message;
    }
    ElMessage.error(errorMsg);
  }
}
const del = (id) => { 
  ElMessageBox.confirm('确认删除该用户？', '提示', { 
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then(async () => { 
    await axios.post('http://localhost:8080/api/admin/user/delete', {id}); 
    ElMessage.success('删除成功');
    load(); 
  }).catch(() => {});
}
const resetPwd = (row) => {
    ElMessageBox.confirm(`确定重置 ${row.username} 的密码为 123456?`, '提示', {
      type: 'warning',
      confirmButtonText: '确认',
      cancelButtonText: '取消'
    }).then(async () => {
        await axios.post('http://localhost:8080/api/common/password', {id: row.id, password: '123456'});
        ElMessage.success('重置成功');
    }).catch(() => {});
}
onMounted(load);
</script>