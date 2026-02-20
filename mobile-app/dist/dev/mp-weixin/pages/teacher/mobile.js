"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  methods: { logout() {
    common_vendor.index.reLaunch({ url: "/pages/index/login" });
  } }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o((...args) => $options.logout && $options.logout(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/Jonathan Mu/OneDrive/Desktop/elderly-university/mobile-app/src/pages/teacher/mobile.vue"]]);
wx.createPage(MiniProgramPage);
