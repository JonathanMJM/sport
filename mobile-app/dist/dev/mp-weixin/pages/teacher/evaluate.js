"use strict";
const common_vendor = require("../../common/vendor.js");
const common_config = require("../../common/config.js");
const _sfc_main = {
  data() {
    return { courses: [], students: [], selectedCourse: null, selectedStudent: null, score: 90, comment: "" };
  },
  onShow() {
    const u = common_vendor.index.getStorageSync("user");
    common_vendor.index.request({
      url: common_config.BASE_URL + "/teacher/courses?tid=" + u.id,
      success: (res) => {
        this.courses = res.data.data;
      }
    });
  },
  methods: {
    onCourseChange(e) {
      this.selectedCourse = this.courses[e.detail.value];
      this.selectedStudent = null;
      common_vendor.index.request({
        url: common_config.BASE_URL + "/teacher/students?cid=" + this.selectedCourse.id,
        success: (res) => {
          this.students = res.data.data;
        }
      });
    },
    onStudentChange(e) {
      this.selectedStudent = this.students[e.detail.value];
    },
    submit() {
      const u = common_vendor.index.getStorageSync("user");
      common_vendor.index.request({
        url: common_config.BASE_URL + "/teacher/evaluate",
        method: "POST",
        data: {
          courseId: this.selectedCourse.id,
          courseName: this.selectedCourse.name,
          studentId: this.selectedStudent.student_id,
          studentName: this.selectedStudent.studentName,
          teacherId: u.id,
          teacherName: u.name,
          score: this.score,
          comment: this.comment
        },
        success: (res) => {
          common_vendor.index.showToast({ title: "评价成功" });
          this.comment = "";
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.t($data.selectedCourse ? $data.selectedCourse.name : "点击选择课程"),
    b: $data.courses,
    c: common_vendor.o((...args) => $options.onCourseChange && $options.onCourseChange(...args)),
    d: $data.selectedCourse
  }, $data.selectedCourse ? {} : {}, {
    e: $data.selectedCourse
  }, $data.selectedCourse ? {
    f: common_vendor.t($data.selectedStudent ? $data.selectedStudent.studentName : "点击选择学员"),
    g: $data.students,
    h: common_vendor.o((...args) => $options.onStudentChange && $options.onStudentChange(...args))
  } : {}, {
    i: $data.selectedStudent
  }, $data.selectedStudent ? {
    j: $data.score,
    k: common_vendor.o(($event) => $data.score = $event.detail.value)
  } : {}, {
    l: $data.selectedStudent
  }, $data.selectedStudent ? {
    m: $data.comment,
    n: common_vendor.o(($event) => $data.comment = $event.detail.value)
  } : {}, {
    o: $data.selectedStudent
  }, $data.selectedStudent ? {
    p: common_vendor.o((...args) => $options.submit && $options.submit(...args))
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/Jonathan Mu/OneDrive/Desktop/elderly-university/mobile-app/src/pages/teacher/evaluate.vue"]]);
wx.createPage(MiniProgramPage);
