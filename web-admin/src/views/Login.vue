<template>
  <div class="login-wrapper">
    <!-- 左侧品牌区 -->
    <div class="brand-side">
      <div class="brand-content">
        <div class="logo-circle">
          <span class="logo-emoji">🏟️</span>
        </div>
        <h1>GymPro <span class="highlight">Max</span></h1>
        <p class="tagline">Next Gen Gym Management System</p>
        <div class="feature-pills">
          <span class="pill">✨ Smart Booking</span>
          <span class="pill">📊 AI Analytics</span>
          <span class="pill">⚡ Real-time</span>
        </div>
      </div>
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
    </div>

    <!-- 右侧表单区 -->
    <div class="form-side">
      <div class="form-container">
        <div class="welcome-text">
          <h2>Welcome Back!</h2>
          <p>Please enter your details.</p>
        </div>

        <div class="inputs">
          <div class="input-item">
            <label>Username</label>
            <input v-model="form.username" type="text" placeholder="admin / coach" />
          </div>
          <div class="input-item">
            <label>Password</label>
            <input v-model="form.password" type="password" placeholder="••••••" />
          </div>
        </div>

        <button class="primary-btn" @click="handleLogin" :disabled="loading">
          {{ loading ? 'Signing in...' : 'Sign in' }}
        </button>

        <div class="divider">or quick access</div>

        <div class="quick-roles">
          <button class="role-btn admin" @click="fill('admin')">Admin</button>
          <button class="role-btn coach" @click="fill('coach1')">Coach</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const form = reactive({ username: '', password: '' });
const loading = ref(false);

const fill = (u) => { form.username = u; form.password = '123456'; }

const handleLogin = async () => {
  if(!form.username) return;
  loading.value = true;
  try {
    const res = await axios.post('http://localhost:8080/api/login', form);
    setTimeout(async () => { // Fake delay for UX
       if (res.data.code === 200) {
        localStorage.setItem('user', JSON.stringify(res.data.data));
        router.push(res.data.data.role === 'admin' ? '/admin/venues' : '/coach/courses');
      } else {
        alert('Credentials Invalid');
      }
      loading.value = false;
    }, 600)
   
  } catch(e) {
    alert('Server Unreachable');
    loading.value = false;
  }
}
</script>

<style scoped>
.login-wrapper {
  display: flex; height: 100vh; width: 100vw;
  background: white;
}

/* Left Brand Side */
.brand-side {
  flex: 1; background: #F8FAFC;
  position: relative; overflow: hidden;
  display: flex; align-items: center; justify-content: center;
}
.brand-content { position: relative; z-index: 10; text-align: center; }
.logo-circle {
  width: 80px; height: 80px; background: white; border-radius: 24px;
  display: flex; align-items: center; justify-content: center; margin: 0 auto 24px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.05);
  font-size: 40px;
}
h1 { font-size: 48px; font-weight: 800; color: #0F172A; margin: 0; letter-spacing: -1px; }
.highlight { color: #2563EB; }
.tagline { font-size: 18px; color: #64748B; margin: 16px 0 32px; font-weight: 500; }
.feature-pills { display: flex; gap: 12px; justify-content: center; }
.pill {
  background: rgba(255,255,255,0.6); padding: 8px 16px; border-radius: 20px;
  font-size: 13px; font-weight: 600; color: #475569;
  border: 1px solid rgba(0,0,0,0.05);
}

/* Abstract Shapes */
.bg-shape { position: absolute; border-radius: 50%; filter: blur(60px); opacity: 0.6; }
.shape-1 { width: 500px; height: 500px; background: #DBEAFE; top: -100px; left: -100px; }
.shape-2 { width: 400px; height: 400px; background: #FAE8FF; bottom: -50px; right: -50px; }

/* Right Form Side */
.form-side {
  flex: 1; 
  display: flex; align-items: center; justify-content: center;
  background: white;
}
.form-container { width: 100%; max-width: 400px; padding: 40px; }

.welcome-text { margin-bottom: 40px; }
.welcome-text h2 { font-size: 32px; font-weight: 800; color: #0F172A; margin: 0 0 8px; letter-spacing: -0.5px; }
.welcome-text p { color: #64748B; margin: 0; }

.inputs { display: flex; flex-direction: column; gap: 20px; margin-bottom: 32px; }
.input-item label { display: block; font-size: 13px; font-weight: 600; color: #334155; margin-bottom: 8px; }
.input-item input {
  width: 100%; height: 48px; padding: 0 16px; border-radius: 12px;
  border: 1px solid #E2E8F0; background: #F8FAFC;
  font-size: 15px; color: #1E293B; outline: none; transition: all 0.2s;
  box-sizing: border-box;
}
.input-item input:focus { border-color: #2563EB; background: white; box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1); }

.primary-btn {
  width: 100%; height: 52px; background: #0F172A; color: white;
  border: none; border-radius: 14px;
  font-size: 16px; font-weight: 700; cursor: pointer;
  transition: transform 0.1s, background 0.2s;
}
.primary-btn:hover { background: #1E293B; transform: translateY(-1px); }
.primary-btn:active { transform: translateY(0); }

.divider { text-align: center; color: #94A3B8; font-size: 12px; margin: 24px 0; font-weight: 500; }

.quick-roles { display: flex; gap: 16px; }
.role-btn {
  flex: 1; height: 44px; border-radius: 10px; border: 1px solid #E2E8F0;
  background: white; cursor: pointer; font-weight: 600; color: #475569; transition: all 0.2s;
}
.role-btn:hover { border-color: #CBD5E1; background: #F8FAFC; color: #0F172A; }
.role-btn.admin:hover { border-color: #BFDBFE; background: #EFF6FF; color: #1D4ED8; }
</style>