"use strict";
const common_vendor = require("../../common/vendor.js");
const common_config = require("../../common/config.js");
const _sfc_main = {
  data() {
    return { user: {}, list: [], bookings: [], certs: [], tab: "book" };
  },
  onShow() {
    this.user = common_vendor.index.getStorageSync("user");
    this.loadCourses();
  },
  methods: {
    loadCourses() {
      common_vendor.index.request({ url: common_config.BASE_URL + "/student/courses", success: (res) => {
        this.list = res.data.data;
      } });
    },
    loadMy() {
      this.tab = "my";
      common_vendor.index.request({ url: common_config.BASE_URL + "/student/my?sid=" + this.user.id, success: (res) => {
        this.bookings = res.data.data;
      } });
    },
    getCert() {
      this.tab = "cert";
      common_vendor.index.request({ url: common_config.BASE_URL + "/student/certs?sid=" + this.user.id, success: (res) => {
        this.certs = res.data.data;
      } });
    },
    book(cid) {
      common_vendor.index.request({ url: common_config.BASE_URL + "/student/book", method: "POST", data: { sid: this.user.id, cid }, success: (res) => {
        common_vendor.index.showToast({ title: res.data.msg, icon: "none" });
      } });
    },
    pay(id) {
      common_vendor.index.request({ url: common_config.BASE_URL + "/student/pay", method: "POST", data: { id }, success: (res) => {
        common_vendor.index.showToast({ title: "缴费成功" });
        this.loadMy();
      } });
    },
    checkIn(cid) {
      common_vendor.index.request({ url: common_config.BASE_URL + "/student/checkin", method: "POST", data: { sid: this.user.id, cid }, success: (res) => {
        common_vendor.index.showToast({ title: res.data.msg, icon: res.data.code === 200 ? "success" : "none" });
      } });
    },
    goEval() {
      common_vendor.index.navigateTo({ url: "/pages/student/evaluate" });
    },
    goProfile() {
      common_vendor.index.navigateTo({ url: "/pages/common/profile" });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.t($data.user.name),
    b: common_vendor.t($data.user.balance),
    c: common_vendor.t($data.user.name ? $data.user.name[0] : "U"),
    d: common_vendor.o((...args) => $options.goProfile && $options.goProfile(...args)),
    e: common_vendor.o(($event) => $data.tab = "book"),
    f: $data.tab === "book" ? 1 : "",
    g: common_vendor.o((...args) => $options.loadMy && $options.loadMy(...args)),
    h: $data.tab === "my" ? 1 : "",
    i: common_vendor.o((...args) => $options.getCert && $options.getCert(...args)),
    j: $data.tab === "cert" ? 1 : "",
    k: common_vendor.o((...args) => $options.goEval && $options.goEval(...args)),
    l: $data.tab === "book"
  }, $data.tab === "book" ? {
    m: common_vendor.f($data.list, (c, k0, i0) => {
      return {
        a: common_vendor.t(c.name),
        b: common_vendor.t(c.price),
        c: common_vendor.t(c.campusName),
        d: common_vendor.t(c.classroom),
        e: common_vendor.t(c.teacherName),
        f: common_vendor.t(c.timeSlot),
        g: common_vendor.o(($event) => $options.book(c.id), c.id),
        h: c.id
      };
    })
  } : {}, {
    n: $data.tab === "my"
  }, $data.tab === "my" ? common_vendor.e({
    o: common_vendor.f($data.bookings, (b, k0, i0) => {
      return common_vendor.e({
        a: common_vendor.t(b.courseName),
        b: common_vendor.t(b.status === "UNPAID" ? "待缴费" : "已报名"),
        c: common_vendor.n(b.status),
        d: b.status === "UNPAID"
      }, b.status === "UNPAID" ? {
        e: common_vendor.o(($event) => $options.pay(b.id), b.id)
      } : {}, {
        f: b.status === "PAID"
      }, b.status === "PAID" ? {
        g: common_vendor.o(($event) => $options.checkIn(b.cid), b.id)
      } : {}, {
        h: b.id
      });
    }),
    p: $data.bookings.length === 0
  }, $data.bookings.length === 0 ? {} : {}) : {}, {
    q: $data.tab === "cert"
  }, $data.tab === "cert" ? common_vendor.e({
    r: common_vendor.f($data.certs, (cer, k0, i0) => {
      return {
        a: common_vendor.t(cer.courseName),
        b: common_vendor.t(cer.code),
        c: common_vendor.t(cer.issueDate),
        d: cer.id
      };
    }),
    s: $data.certs.length === 0
  }, $data.certs.length === 0 ? {} : {}) : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/Jonathan Mu/OneDrive/Desktop/elderly-university/mobile-app/src/pages/student/home.vue"]]);
wx.createPage(MiniProgramPage);
