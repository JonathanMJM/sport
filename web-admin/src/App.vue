<template>
  <router-view />
</template>

<style>
:root {
  /* UI UX Pro Max - Light Theme Variables (Frosted Daylight) */
  --bg-deep: #F0F2F5; /* Soft light gray base */
  --bg-glass: rgba(255, 255, 255, 0.75); /* Highly transparent white */
  --bg-glass-lighter: rgba(255, 255, 255, 0.6);
  
  --primary: #2563EB; /* Royal Blue - Crisp and Professional */
  --primary-glow: rgba(37, 99, 235, 0.3);
  --secondary: #7C3AED; /* Violet */
  --accent: #DB2777; /* Pink */
  
  --text-main: #1E293B; /* Slate 800 - High contrast dark gray */
  --text-muted: #64748B; /* Slate 500 */
  
  --border-glass: rgba(255, 255, 255, 0.6); /* White borders for frosted effect */
  --border-glass-hover: #FFFFFF;
  
  /* Shadow System - Diffuse & Airy */
  --shadow-card: 0 10px 30px -10px rgba(0, 0, 0, 0.05), 0 4px 6px -4px rgba(0, 0, 0, 0.02);
  --shadow-float: 0 20px 40px -12px rgba(0, 0, 0, 0.1);
  
  /* Element Plus Overrides for Light Glass */
  --el-color-primary: var(--primary) !important;
  --el-text-color-primary: var(--text-main) !important;
  --el-text-color-regular: var(--text-muted) !important;
  --el-bg-color: #ffffff !important;
  --el-bg-color-overlay: #ffffff !important;
  --el-border-color-light: #E2E8F0 !important;
  --el-fill-color-blank: transparent !important;
}

html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
  background-color: var(--bg-deep);
  color: var(--text-main);
  -webkit-font-smoothing: antialiased;
}

#app { width: 100%; height: 100%; }

/* Scrollbar */
::-webkit-scrollbar { width: 8px; height: 8px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: #CBD5E1; border-radius: 4px; border: 2px solid transparent; background-clip: content-box; }
::-webkit-scrollbar-thumb:hover { background-color: #94A3B8; }

/* Global Glass Card */
.glass-card {
  background: var(--bg-glass);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid var(--border-glass);
  box-shadow: var(--shadow-card);
  border-radius: 20px;
}

/* Element Plus Component Overrides */
.el-card {
  background: rgba(255, 255, 255, 0.7) !important;
  border: 1px solid #fff !important;
  color: var(--text-main) !important;
  backdrop-filter: blur(20px);
  border-radius: 16px !important;
  box-shadow: var(--shadow-card) !important;
}

/* Table - Clean & Airy */
.el-table {
  --el-table-bg-color: transparent !important;
  --el-table-tr-bg-color: transparent !important;
  --el-table-header-bg-color: transparent !important;
  --el-table-row-hover-bg-color: rgba(37, 99, 235, 0.04) !important;
  --el-table-border-color: #E2E8F0 !important;
  color: var(--text-main) !important;
}

.el-table th.el-table__cell {
  background-color: rgba(255, 255, 255, 0.5) !important;
  color: var(--text-muted) !important;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 11px;
  letter-spacing: 0.5px;
  border-bottom: 1px solid #E2E8F0 !important;
}

.el-table td.el-table__cell {
  border-bottom: 1px solid #F1F5F9 !important;
}

/* Buttons - Modern & Soft */
.el-button {
  background: white;
  border: 1px solid #E2E8F0;
  color: var(--text-main);
  border-radius: 10px !important;
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}
.el-button:hover {
  background: #F8FAFC;
  border-color: #CBD5E1;
  color: var(--primary);
  transform: translateY(-1px);
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);
}

.el-button--primary {
  background: var(--primary) !important;
  border: none !important;
  color: white !important;
  box-shadow: 0 4px 12px var(--primary-glow) !important;
}
.el-button--primary:hover {
  background: #1D4ED8 !important; /* Darker Blue */
  transform: translateY(-1px);
  box-shadow: 0 6px 16px var(--primary-glow) !important;
}

.el-button--danger {
  background: #FEF2F2 !important;
  border-color: #FECACA !important;
  color: #DC2626 !important;
  box-shadow: none !important;
}
.el-button--danger:hover {
  background: #FEE2E2 !important;
  border-color: #FCA5A5 !important;
}

/* Tags - Soft Pills */
.el-tag {
  background-color: #EFF6FF !important; /* Blue-50 */
  border-color: #BFDBFE !important; /* Blue-200 */
  color: #2563EB !important; /* Blue-600 */
  border-radius: 20px !important; /* Pill shape */
  font-weight: 500;
}
.el-tag--success {
  background-color: #ECFDF5 !important;
  border-color: #A7F3D0 !important;
  color: #059669 !important;
}
.el-tag--warning {
  background-color: #FFFBEB !important;
  border-color: #FDE68A !important;
  color: #D97706 !important;
}
.el-tag--danger {
  background-color: #FEF2F2 !important;
  border-color: #FECACA !important;
  color: #DC2626 !important;
}

/* Dialogs */
.el-dialog, .el-message-box {
  border-radius: 24px !important;
  background: rgba(255, 255, 255, 0.95) !important; /* increased opacity */
  backdrop-filter: blur(24px) !important;
  box-shadow: var(--shadow-float) !important;
  border: 1px solid white !important;
  /* Ensure dialog itself is not clipped */
  margin-top: 15vh !important; 
}

/* CRITICAL FIX: Overlay Clipping Issue */
.el-overlay {
  position: fixed !important;
  top: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  left: 0 !important;
  height: 100vh !important;
  width: 100vw !important;
  z-index: 2000 !important; /* Ensure it's above sidebars */
  background-color: rgba(0, 0, 0, 0.5) !important; /* Make sure it's visible */
  overflow: auto !important; /* Allow scrolling if dialog is tall */
}

/* Fix Card Content Clipping */
.el-card {
  background: rgba(255, 255, 255, 0.8) !important;
  border: 1px solid #fff !important;
  color: var(--text-main) !important;
  backdrop-filter: blur(20px);
  border-radius: 16px !important;
  box-shadow: var(--shadow-card) !important;
  overflow: visible !important; /* Allow popups/tooltips inside card to show */
}
.el-card__body {
   padding: 20px !important;
   overflow: auto !important; /* Ensure content inside card is scrollable if needed */
}

.el-dialog__title { font-weight: 700; color: #0F172A; }
.el-message-box__title { font-weight: 700; }

/* Inputs */
.el-input__wrapper {
  background-color: white !important;
  box-shadow: 0 0 0 1px #E2E8F0 inset !important;
  border-radius: 10px !important;
}
.el-input__wrapper.is-focus {
  box-shadow: 0 0 0 2px var(--primary-glow), 0 0 0 1px var(--primary) inset !important;
}
.el-input__inner { color: #1E293B !important; }

/* Global Scrollbar Reset to prevent layout shifts */
body {
    overflow: hidden; /* App handles scrolling */
}
#app {
    overflow: hidden;
}
</style>