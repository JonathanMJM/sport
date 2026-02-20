<template>
  <el-card>
    <template #header>
      <div class="flex justify-between items-center">
        <span class="font-bold">场馆资源管理</span>
        <el-button type="primary" @click="openDialog">新增场馆</el-button>
      </div>
    </template>
    <el-table :data="list" border stripe>
      <el-table-column prop="name" label="场馆名称" width="150" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="capacity" label="容纳人数" width="100" />
      <el-table-column prop="price" label="价格/时" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status==='OPEN'?'success':'info'">{{row.status==='OPEN'?'开放':'关闭'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280">
        <template #default="{row}">
          <el-button size="small" @click="showQr(row)">签到码</el-button>
          <el-button size="small" type="primary" @click="edit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog" :title="form.id ? '编辑场馆' : '新增场馆'" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name"/></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type">
            <el-option label="篮球" value="篮球"/><el-option label="羽毛球" value="羽毛球"/><el-option label="游泳" value="游泳"/>
          </el-select>
        </el-form-item>
        <el-form-item label="位置"><el-input v-model="form.location"/></el-form-item>
        <el-form-item label="容量"><el-input v-model="form.capacity" type="number"/></el-form-item>
        <el-form-item label="价格"><el-input v-model="form.price" type="number"/></el-form-item>
        <el-form-item label="状态">
            <el-switch v-model="form.status" active-value="OPEN" inactive-value="CLOSED" active-text="开放" inactive-text="关闭"/>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="submit" type="primary">确认</el-button></template>
    </el-dialog>

    <!-- QR Code Dialog -->
    <el-dialog v-model="qrDialog" title="场馆签到码" width="300px" append-to-body center>
       <div style="text-align: center;">
          <p style="margin-bottom: 10px; font-weight: bold;">{{ currentVenueName }}</p>
          <el-image :src="qrUrl" style="width: 200px; height: 200px" />
          <p style="font-size: 12px; color: #666; margin: 10px 0;">请扫描此码签到入场</p>
          <el-button size="small" type="primary" @click="downloadQr">下载图片</el-button>
       </div>
    </el-dialog>
  </el-card>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'; import axios from 'axios'; import { ElMessage, ElMessageBox } from 'element-plus';
const list = ref([]); const dialog = ref(false); const qrDialog = ref(false);
const qrUrl = ref(''); const currentVenueName = ref('');
const form = reactive({id:null, name:'', type:'篮球', location:'', capacity:50, price:50, status:'OPEN'});

const load = async () => { const res = await axios.get('http://localhost:8080/api/admin/venues'); list.value = res.data.data; }
const openDialog = () => { Object.assign(form, {id:null, name:'', type:'篮球', location:'', capacity:50, price:50, status:'OPEN'}); dialog.value=true; }
const edit = (row) => { Object.assign(form, row); dialog.value=true; }

const showQr = (row) => {
    // JSON format for the QR code data: {"venueId": 123, "type": "checkin"}
    const data = JSON.stringify({ venueId: row.id, type: 'checkin' });
    currentVenueName.value = row.name;
    // Using a public API to generate QR code to avoid frontend dependencies
    qrUrl.value = `https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=${encodeURIComponent(data)}`;
    qrDialog.value = true;
}

const downloadQr = () => {
    window.open(qrUrl.value);
}

const submit = async () => { await axios.post('http://localhost:8080/api/admin/venue', form); dialog.value=false; load(); }
const del = (id) => { ElMessageBox.confirm('确认删除?').then(async () => { await axios.post('http://localhost:8080/api/admin/venue/delete', {id}); load(); }) }
onMounted(load);
</script>
