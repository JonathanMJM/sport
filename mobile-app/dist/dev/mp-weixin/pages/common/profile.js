"use strict";
const common_vendor = require("../../common/vendor.js");
const common_config = require("../../common/config.js");
const _sfc_main = {
  data() {
    return {
      user: {},
      form: { name: "", id: null, role: "" },
      pwd: ""
    };
  },
  onShow() {
    const u = common_vendor.index.getStorageSync("user");
    if (u) {
      this.user = u;
      this.form.name = u.name;
      this.form.id = u.id;
      this.form.role = u.role;
    } else {
      common_vendor.index.redirectTo({ url: "../login/login" });
    }
  },
  computed: {
    roleName() {
      const map = { "student": "学员", "teacher": "教师", "admin": "管理员" };
      return map[this.user.role] || this.user.role;
    }
  },
  methods: {
    updateInfo() {
      common_vendor.index.request({
        url: common_config.BASE_URL + "/common/profile",
        method: "POST",
        data: { id: this.user.id, name: this.form.name, role: this.user.role, status: this.user.status, balance: this.user.balance },
        success: (res) => {
          if (res.data.code === 200) {
            common_vendor.index.setStorageSync("user", res.data.data);
            this.user = res.data.data;
            common_vendor.index.showToast({ title: "信息已更新" });
          }
        }
      });
    },
    updatePwd() {
      if (!this.pwd)
        return common_vendor.index.showToast({ title: "请输入新密码", icon: "none" });
      common_vendor.index.request({
        url: common_config.BASE_URL + "/common/password",
        method: "POST",
        data: { id: this.user.id, password: this.pwd },
        success: (res) => {
          if (res.data.code === 200)
            common_vendor.index.showToast({ title: "密码已修改" });
        }
      });
    },
    logout() {
      common_vendor.index.removeStorageSync("user");
      common_vendor.index.reLaunch({ url: "../index/login" });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.t($data.user.name ? $data.user.name[0] : "U"),
    b: common_vendor.t($data.user.name),
    c: common_vendor.t($options.roleName),
    d: $data.form.name,
    e: common_vendor.o(($event) => $data.form.name = $event.detail.value),
    f: $data.user.username,
    g: common_vendor.o(($event) => $data.user.username = $event.detail.value),
    h: common_vendor.o((...args) => $options.updateInfo && $options.updateInfo(...args)),
    i: $data.pwd,
    j: common_vendor.o(($event) => $data.pwd = $event.detail.value),
    k: common_vendor.o((...args) => $options.updatePwd && $options.updatePwd(...args)),
    l: common_vendor.o((...args) => $options.logout && $options.logout(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/Jonathan Mu/OneDrive/Desktop/elderly-university/mobile-app/src/pages/common/profile.vue"]]);
wx.createPage(MiniProgramPage);
