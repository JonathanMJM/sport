"use strict";
const common_vendor = require("../../common/vendor.js");
const common_config = require("../../common/config.js");
const _sfc_main = {
  data() {
    return { courses: [], selectedCourse: null, score: 5, comment: "" };
  },
  onShow() {
    const u = common_vendor.index.getStorageSync("user");
    common_vendor.index.request({
      url: common_config.BASE_URL + "/student/my?sid=" + u.id,
      success: (res) => {
        if (res.data.code === 200) {
          this.courses = res.data.data.filter((c) => c.status === "PAID");
        }
      }
    });
  },
  methods: {
    onCourseChange(e) {
      this.selectedCourse = this.courses[e.detail.value];
    },
    onScoreChange(e) {
      this.score = e.detail.value;
    },
    submit() {
      const u = common_vendor.index.getStorageSync("user");
      common_vendor.index.request({
        url: common_config.BASE_URL + "/student/evaluate",
        method: "POST",
        data: {
          courseId: this.selectedCourse.cid,
          courseName: this.selectedCourse.courseName,
          studentId: u.id,
          studentName: u.name,
          // FIX: Include teacherId
          teacherId: this.selectedCourse.teacherId,
          teacherName: this.selectedCourse.teacherName,
          score: this.score,
          comment: this.comment
        },
        success: (res) => {
          common_vendor.index.showToast({ title: "评价成功" });
          setTimeout(() => common_vendor.index.navigateBack(), 1e3);
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.t($data.selectedCourse ? $data.selectedCourse.courseName : "请选择要评价的课程"),
    b: $data.courses,
    c: common_vendor.o((...args) => $options.onCourseChange && $options.onCourseChange(...args)),
    d: $data.selectedCourse
  }, $data.selectedCourse ? {
    e: $data.score,
    f: common_vendor.o((...args) => $options.onScoreChange && $options.onScoreChange(...args))
  } : {}, {
    g: $data.selectedCourse
  }, $data.selectedCourse ? {
    h: $data.comment,
    i: common_vendor.o(($event) => $data.comment = $event.detail.value)
  } : {}, {
    j: $data.selectedCourse
  }, $data.selectedCourse ? {
    k: common_vendor.o((...args) => $options.submit && $options.submit(...args))
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/Jonathan Mu/OneDrive/Desktop/elderly-university/mobile-app/src/pages/student/evaluate.vue"]]);
wx.createPage(MiniProgramPage);
