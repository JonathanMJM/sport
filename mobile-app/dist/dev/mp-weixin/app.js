"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/index/login.js";
  "./pages/student/home.js";
  "./pages/teacher/mobile.js";
  "./pages/common/profile.js";
  "./pages/student/evaluate.js";
  "./pages/teacher/evaluate.js";
}
const _sfc_main = { onLaunch: function() {
  console.log("App Launch");
} };
const App = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__file", "C:/Users/Jonathan Mu/OneDrive/Desktop/elderly-university/mobile-app/src/App.vue"]]);
function createApp() {
  const app = common_vendor.createSSRApp(App);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
