<template>
  <div class="layout-container">
    <!-- 浅色多彩背景 -->
    <div class="mesh-bg">
      <div class="mesh-blob blob-1"></div>
      <div class="mesh-blob blob-2"></div>
      <div class="mesh-blob blob-3"></div>
      <div class="glass-overlay"></div>
    </div>

    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="logo-area">
        <div class="logo-icon-box">
          <el-icon><School /></el-icon>
        </div>
        <span class="logo-text">GymPro</span>
      </div>
      
      <div class="menu-wrap">
        <div v-if="role === 'admin'" class="menu-section">
          <div class="section-title">综合管理</div>
          <router-link to="/admin/venues" class="nav-link" active-class="active">
            <el-icon><OfficeBuilding /></el-icon> <span>场馆管理</span>
          </router-link>
          <router-link to="/admin/checkins" class="nav-link" active-class="active">
            <el-icon><Ticket /></el-icon> <span>签到管理</span>
          </router-link>
          <router-link to="/admin/equipments" class="nav-link" active-class="active">
            <el-icon><Bicycle /></el-icon> <span>器材管理</span>
          </router-link>
          
          <div class="section-title">运营中心</div>
          <router-link to="/admin/reservations" class="nav-link" active-class="active">
            <el-icon><Reading /></el-icon> <span>预约订单</span>
          </router-link>
          <router-link to="/admin/crowd" class="nav-link" active-class="active">
            <el-icon><DataLine /></el-icon> <span>人流监控</span>
          </router-link>
          <router-link to="/admin/users" class="nav-link" active-class="active">
            <el-icon><User /></el-icon> <span>用户管理</span>
          </router-link>
          
          <div class="section-title">系统设置</div>
          <router-link to="/admin/evaluations" class="nav-link" active-class="active">
            <el-icon><ChatLineRound /></el-icon> <span>评价反馈</span>
          </router-link>
        </div>

        <div v-if="role === 'coach'" class="menu-section">
          <div class="section-title">工作台</div>
          <router-link to="/coach/courses" class="nav-link" active-class="active">
            <el-icon><Notebook /></el-icon> <span>我的排课</span>
          </router-link>
        </div>
      </div>

      <div class="user-card">
        <el-avatar :size="40" class="user-avatar">{{ user.name ? user.name[0] : 'U' }}</el-avatar>
        <div class="user-meta">
          <div class="user-name">{{ user.name || '用户' }}</div>
          <div class="user-role">{{ roleText }}</div>
        </div>
        <button class="logout-btn" @click="logout" title="退出登录">
          <el-icon><SwitchButton /></el-icon>
        </button>
      </div>
    </aside>

    <!-- 主内容 -->
    <main class="main-wrapper">
      <header class="top-bar">
        <h2 class="page-heading">{{ routeName }}</h2>
        <div class="top-actions">
           <div class="date-badge">{{ new Date().toLocaleDateString() }}</div>
        </div>
      </header>
      
      <div class="content-area">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { 
  School, User, Reading, Notebook, SwitchButton, 
  OfficeBuilding, ChatLineRound, Bicycle, DataLine, Ticket
} from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();
const user = JSON.parse(localStorage.getItem('user') || '{}');
const role = user.role;

const roleText = computed(() => {
    switch(role) {
        case 'admin': return '系统管理员';
        case 'coach': return '教练';
        case 'teacher': return '教师';
        case 'student': return '学员';
        default: return '访客';
    }
});

const routeName = computed(() => { 
    const map = { 
        '/admin/users':'用户管理', 
        '/admin/venues':'场馆控制', 
        '/admin/equipments':'器材库存', 
        '/admin/reservations':'预约记录', 
        '/admin/crowd':'实时流量',
        '/admin/checkins':'签到管理', 
        '/admin/evaluations':'评价反馈', 
        '/coach/courses':'我的课程', 
        '/profile':'个人中心'
    };
    return map[route.path] || '控制台';
});

const logout = () => { localStorage.removeItem('user'); router.push('/login'); }
</script>

<style scoped>
.layout-container {
  display: flex; height: 100vh; width: 100vw;
  position: relative; overflow: hidden;
  background: #F8FAFC;
}

/* 动感背景 - 移除 */
.mesh-bg { display: none; }

/* Sidebar */
.sidebar {
  width: 260px; height: 100vh;
  background: rgba(255, 255, 255, 0.8); /* 稍微增加不透明度 */
  backdrop-filter: blur(20px);
  border-right: 1px solid #fff;
  display: flex; flex-direction: column;
  padding: 24px 20px; /* 减小上下 Padding 防止溢出 */
  box-shadow: 20px 0 40px -20px rgba(0,0,0,0.05);
  position: relative; 
  z-index: 10;
  box-sizing: border-box; /* 关键修复：确保 padding 包含在 height 100vh 内 */
}

.logo-area { display: flex; align-items: center; gap: 12px; margin-bottom: 32px; padding-left: 12px; }
.logo-icon-box {
  width: 36px; height: 36px; background: linear-gradient(135deg, #2563EB, #4F46E5);
  border-radius: 10px; color: white; display: flex; align-items: center; justify-content: center; font-size: 20px;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
}
.logo-text { font-size: 20px; font-weight: 800; color: #1E293B; letter-spacing: -0.5px; }

.menu-wrap { flex: 1; overflow-y: auto; overflow-x: hidden; padding-bottom: 20px; }
.section-title {
  font-size: 11px; font-weight: 700; color: #94A3B8;
  margin: 24px 0 12px 12px; letter-spacing: 0.5px;
}

.nav-link {
  display: flex; align-items: center; gap: 12px;
  height: 44px; padding: 0 16px; margin-bottom: 4px;
  border-radius: 12px;
  text-decoration: none; color: #64748B;
  font-weight: 500; font-size: 14px;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}
.nav-link:hover { background: rgba(255,255,255,0.8); color: #1E293B; }
.nav-link.active {
  background: white; color: #2563EB;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05); /* Soft float */
  font-weight: 600;
}

.user-card {
  margin-top: auto; background: white;
  padding: 12px; border-radius: 16px;
  display: flex; align-items: center; gap: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.03);
  border: 1px solid #F1F5F9;
  flex-shrink: 0; /* 防止被挤压 */
}
.user-avatar { background: #EEF2FF; color: #4F46E5; font-weight: 700; }
.user-meta { flex: 1; overflow: hidden; }
.user-name { font-size: 14px; font-weight: 700; color: #1E293B; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.user-role { font-size: 11px; color: #64748B; text-transform: uppercase; font-weight: 600; }
.logout-btn {
  background: transparent; border: none; cursor: pointer; color: #94A3B8;
  width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center;
  transition: all 0.2s;
}
.logout-btn:hover { background: #FEF2F2; color: #EF4444; }

/* Main Content */
.main-wrapper { flex: 1; display: flex; flex-direction: column; overflow: hidden; position: relative; z-index: 1; }

.top-bar {
  height: 72px; padding: 0 40px;
  display: flex; align-items: center; justify-content: space-between;
}
.page-heading { font-size: 24px; font-weight: 800; color: #1E293B; letter-spacing: -0.5px; }

.date-badge {
  background: rgba(255,255,255,0.6); padding: 6px 16px; border-radius: 20px;
  font-size: 13px; font-weight: 600; color: #64748B;
  border: 1px solid #fff;
}

.content-area { flex: 1; overflow-y: auto; padding: 0 40px 40px 40px; }

/* Transitions */
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s ease, transform 0.2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; transform: translateY(10px); }
</style>