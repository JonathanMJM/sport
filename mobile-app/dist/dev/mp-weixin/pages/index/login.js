"use strict";
const common_vendor = require("../../common/vendor.js");
const common_config = require("../../common/config.js");
const _sfc_main = {
  data() {
    return { u: "", p: "" };
  },
  methods: {
    quick(n) {
      this.u = n;
      this.p = "123456";
    },
    login() {
      common_vendor.index.request({
        url: common_config.BASE_URL + "/login",
        method: "POST",
        data: { username: this.u, password: this.p },
        success: (res) => {
          if (res.data.code === 200) {
            common_vendor.index.setStorageSync("user", res.data.data);
            common_vendor.index.reLaunch({ url: res.data.data.role === "student" ? "/pages/student/home" : "/pages/teacher/mobile" });
          } else {
            common_vendor.index.showToast({ title: "账号错误", icon: "none" });
          }
        },
        fail: () => common_vendor.index.showToast({ title: "网络错误", icon: "none" })
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.u,
    b: common_vendor.o(($event) => $data.u = $event.detail.value),
    c: $data.p,
    d: common_vendor.o(($event) => $data.p = $event.detail.value),
    e: common_vendor.o((...args) => $options.login && $options.login(...args)),
    f: common_vendor.o(($event) => $options.quick("s1")),
    g: common_vendor.o(($event) => $options.quick("t1"))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/Jonathan Mu/OneDrive/Desktop/elderly-university/mobile-app/src/pages/index/login.vue"]]);
wx.createPage(MiniProgramPage);
