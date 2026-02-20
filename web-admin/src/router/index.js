import { createRouter, createWebHistory } from 'vue-router'
const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/', component: () => import('../layout/Layout.vue'), redirect: '/login',
    children: [
      { path: 'admin/users', component: () => import('../views/admin/UserMgmt.vue') },
      { path: 'admin/venues', component: () => import('../views/admin/VenueMgmt.vue') },
      { path: 'admin/crowd', component: () => import('../views/admin/CrowdMonitor.vue') },
      { path: 'admin/reservations', component: () => import('../views/admin/ReservationMgmt.vue') },
      { path: 'admin/equipments', component: () => import('../views/admin/EquipmentMgmt.vue') },
      { path: 'admin/checkins', component: () => import('../views/admin/CheckInMgmt.vue') },
      { path: 'admin/evaluations', component: () => import('../views/admin/EvaluationMgmt.vue') },
      // Keep Teacher/Coach routes
      { path: 'coach/courses', component: () => import('../views/teacher/MyCourses.vue') },
      { path: 'profile', component: () => import('../views/Profile.vue') }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]
const router = createRouter({ history: createWebHistory(), routes });
export default router