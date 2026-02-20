<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">器材库存管理</span>
        <el-button type="primary" @click="openDialog">新增器材</el-button>
      </div>
    </template>
    <el-table :data="list" border stripe>
      <el-table-column prop="name" label="器材名称" />
      <el-table-column prop="totalStock" label="总库存" width="100" />
      <el-table-column prop="availableStock" label="可用库存" width="100" />
      <el-table-column prop="price" label="租赁价格" width="100" />
      <el-table-column label="操作" width="200">
        <template #default="{row}">
          <el-button size="small" type="primary" @click="edit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="form.id ? '编辑器材' : '新增器材'" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name"/></el-form-item>
        <el-form-item label="总库存"><el-input v-model="form.totalStock" type="number"/></el-form-item>
        <el-form-item label="可用库存"><el-input v-model="form.availableStock" type="number"/></el-form-item>
        <el-form-item label="价格"><el-input v-model="form.price" type="number"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="submit" type="primary">确认</el-button></template>
    </el-dialog>
  </el-card>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import axios from 'axios'; import { ElMessage, ElMessageBox } from 'element-plus';
const list = ref([]); const dialog = ref(false);
const form = reactive({id:null, name:'', totalStock:10, availableStock:10, price:5});

const load = async () => { const res = await axios.get('http://localhost:8080/api/admin/equipments'); list.value = res.data.data; }
const openDialog = () => { Object.assign(form, {id:null, name:'', totalStock:10, availableStock:10, price:5}); dialog.value=true; }
const edit = (row) => { Object.assign(form, row); dialog.value=true; }
const submit = async () => { await axios.post('http://localhost:8080/api/admin/equipment', form); dialog.value=false; load(); }
const del = (id) => { ElMessageBox.confirm('确认删除?').then(async () => { await axios.post('http://localhost:8080/api/admin/equipment/delete', {id}); load(); }) }
onMounted(load);
</script>
