<template>
  <el-card style="max-width: 600px; margin: 20px auto;">
    <template #header><div class="font-bold">个人中心</div></template>
    <el-form label-width="100px">
        <el-form-item label="账号"><el-input v-model="user.username" disabled /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="user.name" /></el-form-item>
        <el-form-item label="角色"><el-tag>{{user.role}}</el-tag></el-form-item>
        <el-form-item><el-button type="primary" @click="updateInfo">更新信息</el-button></el-form-item>
    </el-form>
    <el-divider />
    <div class="font-bold mb-4">修改密码</div>
    <el-form label-width="100px">
        <el-form-item label="新密码"><el-input v-model="pwd" type="password" /></el-form-item>
        <el-form-item><el-button type="warning" @click="changePwd">修改密码</el-button></el-form-item>
    </el-form>
  </el-card>
</template>
<script setup>
import { ref, reactive } from 'vue'; import axios from 'axios'; import { ElMessage } from 'element-plus';
const user = reactive(JSON.parse(localStorage.getItem('user') || '{}'));
const pwd = ref('');
const updateInfo = async () => {
    const res = await axios.post('http://localhost:8080/api/common/profile', user);
    localStorage.setItem('user', JSON.stringify(res.data.data));
    ElMessage.success('信息已更新');
}
const changePwd = async () => {
    if(!pwd.value) return ElMessage.warning('请输入新密码');
    await axios.post('http://localhost:8080/api/common/password', {id: user.id, password: pwd.value});
    ElMessage.success('密码修改成功');
}
</script>