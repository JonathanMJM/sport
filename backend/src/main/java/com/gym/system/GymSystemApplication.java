package com.gym.system;

import lombok.Data;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.mybatis.spring.annotation.MapperScan;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 🏟️ 体育馆预约与人流监测系统 - 核心启动类
 * 
 * 基于 Spring Boot + MyBatis 构建
 * 包含：场地预约、器材租赁、私教课程、人流监测
 */
@SpringBootApplication
@MapperScan("com.gym.system")
public class GymSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymSystemApplication.class, args);
    }
}

// =========================================================
// 实体类定义 (Entities)
// =========================================================

@Data
class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String role; // admin, coach, student
    private String phone;
    private BigDecimal balance;
    private String status;
    private Date createTime;
}

@Data
class Venue {
    private Long id;
    private String name;
    private String type; // 篮球, 羽毛球
    private String location;
    private Integer capacity;
    private BigDecimal price;
    private String imageUrl;
    private String status; // OPEN, CLOSED
    private String description;
    private String openTime;
    private String closeTime;
}

@Data
class Equipment {
    private Long id;
    private String name;
    private Integer totalStock;
    private Integer availableStock;
    private BigDecimal price;
    private String imageUrl;
    private String status;
}

@Data
class GymCourse {
    private Long id;
    private String name;
    private Long coachId;
    private String coachName;
    private Long venueId;
    private String timeSlot;
    private BigDecimal price;
    private Integer maxStudents;
    private Integer enrolled;
    private String status;
    private String description;
    private String imageUrl;
}

@Data
class Reservation {
    private Long id;
    private Long userId;
    private String type; // VENUE, COURSE
    private Long targetId;
    private String targetName; // 冗余字段
    private Date startTime;
    private Date endTime;
    private BigDecimal price;
    private String status; // UNPAID, PAID, CANCELLED, COMPLETED
    private Date createTime;
}

@Data
class CheckIn {
    private Long id;
    private Long userId;
    private Long reservationId;
    private Long venueId;
    private Date checkInTime;
    private Date checkOutTime;
    private String status; // CHECKED_IN, LEFT
}

@Data
class Evaluation {
    private Long id;
    private Long userId;
    private Long targetId;
    private String type; // COACH, VENUE
    private Integer score;
    private String comment;
    private Date createTime;
    // 冗余字段用于展示
    private String userName;
    private String targetName;
}

// =========================================================
// 数据访问层 (Mappers)
// =========================================================

@Mapper
interface UserMapper {
    @Select("SELECT * FROM sys_user WHERE username = #{u}")
    User findByUsername(String u);

    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM sys_user ORDER BY id DESC")
    List<User> findAll();

    @Insert("INSERT INTO sys_user(username, password, name, role, status, phone, balance) VALUES(#{username}, '123456', #{name}, #{role}, 'NORMAL', #{phone}, #{balance})")
    void insert(User u);

    @Update("UPDATE sys_user SET name=#{name}, role=#{role}, status=#{status}, phone=#{phone}, balance=#{balance} WHERE id=#{id}")
    void update(User u);

    @Delete("DELETE FROM sys_user WHERE id=#{id}")
    void delete(Long id);

    @Update("UPDATE sys_user SET password=#{pwd} WHERE id=#{id}")
    void updatePwd(@Param("id") Long id, @Param("pwd") String pwd);
}

@Mapper
interface VenueMapper {
    @Select("SELECT * FROM gym_venue ORDER BY id DESC")
    List<Venue> findAll();

    @Select("SELECT * FROM gym_venue WHERE status='OPEN'")
    List<Venue> findOpen();

    @Select("SELECT * FROM gym_venue WHERE id=#{id}")
    Venue findById(Long id);

    @Insert("INSERT INTO gym_venue(name, type, location, capacity, price, image_url, status, description, open_time, close_time) VALUES(#{name}, #{type}, #{location}, #{capacity}, #{price}, #{imageUrl}, 'OPEN', #{description}, #{openTime}, #{closeTime})")
    void insert(Venue v);

    @Update("UPDATE gym_venue SET name=#{name}, type=#{type}, location=#{location}, capacity=#{capacity}, price=#{price}, status=#{status}, description=#{description}, open_time=#{openTime}, close_time=#{closeTime} WHERE id=#{id}")
    void update(Venue v);

    @Delete("DELETE FROM gym_venue WHERE id=#{id}")
    void delete(Long id);
}

@Mapper
interface EquipmentMapper {
    @Select("SELECT * FROM gym_equipment ORDER BY id DESC")
    List<Equipment> findAll();

    @Select("SELECT * FROM gym_equipment WHERE id=#{id}")
    Equipment findById(Long id);

    @Insert("INSERT INTO gym_equipment(name, total_stock, available_stock, price, image_url, status) VALUES(#{name}, #{totalStock}, #{availableStock}, #{price}, #{imageUrl}, 'NORMAL')")
    void insert(Equipment e);

    @Update("UPDATE gym_equipment SET name=#{name}, available_stock=#{availableStock}, total_stock=#{totalStock}, price=#{price}, status=#{status} WHERE id=#{id}")
    void update(Equipment e);

    @Delete("DELETE FROM gym_equipment WHERE id=#{id}")
    void delete(Long id);

    @Update("UPDATE gym_equipment SET available_stock = available_stock - 1 WHERE id=#{id} AND available_stock > 0")
    int decreaseStock(Long id);

    @Update("UPDATE gym_equipment SET available_stock = available_stock + 1 WHERE id=#{id}")
    void increaseStock(Long id);
}

@Mapper
interface GymCourseMapper {
    @Select("SELECT * FROM gym_course ORDER BY id DESC")
    List<GymCourse> findAll();

    @Select("SELECT * FROM gym_course WHERE teacher_id=#{tid}")
    List<GymCourse> findByCoach(Long tid);

    @Select("SELECT * FROM gym_course WHERE id=#{id}")
    GymCourse findById(Long id);

    @Insert("INSERT INTO gym_course(name, coach_id, coach_name, venue_id, time_slot, price, max_students, enrolled, status, description, image_url) VALUES(#{name}, #{coachId}, #{coachName}, #{venueId}, #{timeSlot}, #{price}, #{maxStudents}, 0, 'PUBLISHED', #{description}, #{imageUrl})")
    void insert(GymCourse c);

    @Update("UPDATE gym_course SET enrolled=enrolled+1 WHERE id=#{id}")
    void incrementEnrolled(Long id);

    @Delete("DELETE FROM gym_course WHERE id=#{id}")
    void delete(Long id);
}

@Mapper
interface ReservationMapper {
    @Insert("INSERT INTO gym_reservation(user_id, type, target_id, target_name, start_time, end_time, price, status) VALUES(#{userId}, #{type}, #{targetId}, #{targetName}, #{startTime}, #{endTime}, #{price}, 'UNPAID')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Reservation r);

    @Update("UPDATE gym_reservation SET status=#{status} WHERE id=#{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Select("SELECT * FROM gym_reservation WHERE user_id=#{uid} ORDER BY create_time DESC")
    List<Reservation> findByUser(Long uid);

    @Select("SELECT * FROM gym_reservation WHERE type='VENUE' AND target_id=#{vid} AND start_time=#{start}")
    List<Reservation> checkConflict(@Param("vid") Long vid, @Param("start") Date start);

    @Select("SELECT r.*, u.name as userName, u.username as userNo FROM gym_reservation r JOIN sys_user u ON r.user_id=u.id WHERE r.type='COURSE' AND r.target_id=#{cid}")
    List<Map<String, Object>> findCourseStudents(Long cid);

    @Select("SELECT * FROM gym_reservation WHERE id=#{id}")
    Reservation findById(Long id);
}

@Mapper
interface CheckInMapper {
    @Select("SELECT count(*) FROM gym_checkin WHERE user_id=#{uid} AND check_out_time IS NULL")
    int countActiveByUser(Long uid);

    @Insert("INSERT INTO gym_checkin(user_id, reservation_id, venue_id, check_in_time, status) VALUES(#{userId}, #{reservationId}, #{venueId}, NOW(), 'CHECKED_IN')")
    void checkIn(CheckIn c);

    @Update("UPDATE gym_checkin SET check_out_time=NOW(), status='LEFT' WHERE user_id=#{uid} AND status='CHECKED_IN'")
    void checkOut(Long uid);

    // 实时在场人数统计
    @Select("SELECT count(*) FROM gym_checkin WHERE venue_id=#{venueId} AND status='CHECKED_IN'")
    int countActiveByVenue(Long venueId);

    @Select("SELECT count(*) FROM gym_checkin WHERE status='CHECKED_IN'")
    int countTotalActive();

    @Select("SELECT c.*, u.name as userName, u.username as userNo, v.name as venueName FROM gym_checkin c LEFT JOIN sys_user u ON c.user_id=u.id LEFT JOIN gym_venue v ON c.venue_id=v.id ORDER BY c.check_in_time DESC")
    List<Map<String, Object>> findAll();

    @Update("UPDATE gym_checkin SET check_out_time=NOW(), status='FORCE_OUT' WHERE id=#{id}")
    void forceCheckOut(Long id);
}

@Mapper
interface EvaluationMapper {
    @Insert("INSERT INTO gym_evaluation(user_id, target_id, type, score, comment, create_time) VALUES(#{userId}, #{targetId}, #{type}, #{score}, #{comment}, NOW())")
    void insert(Evaluation e);

    @Select("SELECT e.*, u.name as userName FROM gym_evaluation e JOIN sys_user u ON e.user_id=u.id WHERE type=#{type} ORDER BY create_time DESC")
    List<Evaluation> findByType(String type);
}

// =========================================================
// 控制器 (Controller)
// =========================================================

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ApiController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    VenueMapper venueMapper;
    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    GymCourseMapper courseMapper;
    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    CheckInMapper checkInMapper;
    @Autowired
    EvaluationMapper evaluationMapper;

    // --- 登录 ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> params) {
        User user = userMapper.findByUsername(params.get("username"));
        if (user != null && user.getPassword().equals(params.get("password"))) {
            return ResponseEntity.ok(Map.of("code", 200, "data", user));
        }
        return ResponseEntity.status(401).body("Login failed");
    }

    // --- 管理员: 场馆管理 ---
    @GetMapping("/admin/venues")
    public Object listVenues() {
        return Map.of("code", 200, "data", venueMapper.findAll());
    }

    @PostMapping("/admin/venue")
    public Object saveVenue(@RequestBody Venue v) {
        if (v.getId() == null)
            venueMapper.insert(v);
        else
            venueMapper.update(v);
        return Map.of("code", 200, "msg", "保存成功");
    }

    @PostMapping("/admin/venue/delete")
    public Object delVenue(@RequestBody Map<String, Long> params) {
        venueMapper.delete(params.get("id"));
        return Map.of("code", 200, "msg", "删除成功");
    }

    // --- 管理员: 器材管理 ---
    @GetMapping("/admin/equipments")
    public Object listEquipments() {
        return Map.of("code", 200, "data", equipmentMapper.findAll());
    }

    @PostMapping("/admin/equipment")
    public Object saveEquipment(@RequestBody Equipment e) {
        if (e.getId() == null)
            equipmentMapper.insert(e);
        else
            equipmentMapper.update(e);
        return Map.of("code", 200, "msg", "保存成功");
    }

    @PostMapping("/admin/equipment/delete")
    public Object delEquipment(@RequestBody Map<String, Long> params) {
        equipmentMapper.delete(params.get("id"));
        return Map.of("code", 200, "msg", "删除成功");
    }

    // --- 管理员: 用户管理 ---
    @GetMapping("/admin/users")
    public Object listUsers() {
        return Map.of("code", 200, "data", userMapper.findAll());
    }

    @PostMapping("/admin/user")
    public Object saveUser(@RequestBody User u) {
        if (u.getId() == null) {
            u.setPassword("123456");
            userMapper.insert(u);
        } else
            userMapper.update(u);
        return Map.of("code", 200, "msg", "保存成功");
    }

    @PostMapping("/admin/user/delete")
    public Object delUser(@RequestBody Map<String, Long> params) {
        userMapper.delete(params.get("id"));
        return Map.of("code", 200, "msg", "删除成功");
    }

    // --- 人流监测 Dashboard ---
    @GetMapping("/admin/dashboard/crowd")
    public Object getCrowdData() {
        int totalActive = checkInMapper.countTotalActive();
        List<Venue> venues = venueMapper.findAll();
        // 此处简单构造一个每个场馆的人数列表 (实际需要 checkInMapper 按 venue group by)
        // 简化：遍历查询
        List<Map<String, Object>> venueStats = venues.stream().map(v -> {
            int count = checkInMapper.countActiveByVenue(v.getId());
            String alertLevel = "NORMAL";
            if (count >= v.getCapacity())
                alertLevel = "CRITICAL";
            else if (count >= v.getCapacity() * 0.8)
                alertLevel = "WARNING";

            return Map.<String, Object>of(
                    "venueName", v.getName(),
                    "count", count,
                    "capacity", v.getCapacity(),
                    "status", alertLevel);
        }).collect(java.util.stream.Collectors.toList());

        return Map.of("code", 200, "data", Map.of("totalActive", totalActive, "venues", venueStats));
    }

    // --- 学生: 预约 ---
    @GetMapping("/student/venues")
    public Object stuVenues() {
        return Map.of("code", 200, "data", venueMapper.findOpen());
    }

    @GetMapping("/student/equipments")
    public Object stuEquipments() {
        return Map.of("code", 200, "data", equipmentMapper.findAll());
    }

    @PostMapping("/student/book")
    @Transactional
    public Object book(@RequestBody Reservation r) {
        // 简单冲突检测
        if ("VENUE".equals(r.getType())) {
            // 需要更复杂的逻辑，这里简化
        }

        if ("COURSE".equals(r.getType())) {
            GymCourse c = courseMapper.findById(r.getTargetId());
            if (c != null && c.getEnrolled() >= c.getMaxStudents()) {
                return Map.of("code", 500, "msg", "课程已满");
            }
            courseMapper.incrementEnrolled(c.getId());
            r.setTargetName(c.getName());
            r.setPrice(c.getPrice());
        }

        r.setStatus("PAID"); // 模拟直接支付成功
        reservationMapper.insert(r);
        return Map.of("code", 200, "msg", "预约成功");
    }

    @GetMapping("/student/my")
    public Object myReservations(Long uid) {
        return Map.of("code", 200, "data", reservationMapper.findByUser(uid));
    }

    // --- 签到与离场 (扫码) ---
    @PostMapping("/student/checkin")
    public Object checkIn(@RequestBody Map<String, Long> params) {
        Long uid = params.get("uid");
        Long venueId = params.get("venueId"); // 扫码得到的场馆ID

        // 检查是否已签到
        if (checkInMapper.countActiveByUser(uid) > 0) {
            return Map.of("code", 500, "msg", "您已在场馆内，请先签退"); // 或者允许签退?
        }

        // 检查人流阈值
        Venue v = venueMapper.findById(venueId);
        int currentCount = checkInMapper.countActiveByVenue(venueId);
        if (currentCount >= v.getCapacity()) {
            return Map.of("code", 500, "msg", "场馆人数已满，请稍后入场");
        }

        CheckIn c = new CheckIn();
        c.setUserId(uid);
        c.setVenueId(venueId);
        // c.setReservationId(...) //可选关联
        checkInMapper.checkIn(c);

        return Map.of("code", 200, "msg", "签到成功");
    }

    @PostMapping("/student/checkout")
    public Object checkOut(@RequestBody Map<String, Long> params) {
        Long uid = params.get("uid");
        checkInMapper.checkOut(uid);
        return Map.of("code", 200, "msg", "离场成功");
    }

    // --- 管理员: 签到管理 ---
    @GetMapping("/admin/checkins")
    public Object listCheckIns() {
        return Map.of("code", 200, "data", checkInMapper.findAll());
    }

    @PostMapping("/admin/checkin")
    public Object adminCheckIn(@RequestBody Map<String, Object> params) {
        Long uid = Long.valueOf(params.get("userId").toString());
        Long vid = Long.valueOf(params.get("venueId").toString());

        if (checkInMapper.countActiveByUser(uid) > 0) {
            return Map.of("code", 500, "msg", "该用户已在场内");
        }

        CheckIn c = new CheckIn();
        c.setUserId(uid);
        c.setVenueId(vid);
        checkInMapper.checkIn(c);
        return Map.of("code", 200, "msg", "手动签到成功");
    }

    @PostMapping("/admin/checkout/force")
    public Object forceCheckOut(@RequestBody Map<String, Long> params) {
        Long id = params.get("id"); // checkIn record id, not userId
        checkInMapper.forceCheckOut(id);
        return Map.of("code", 200, "msg", "强制签退成功");
    }

    // --- 通用与课程 ---
    // 教练发布课程
    @PostMapping("/coach/course")
    public Object saveCourse(@RequestBody GymCourse c) {
        courseMapper.insert(c);
        return Map.of("code", 200, "msg", "发布成功");
    }

    @GetMapping("/coach/courses")
    public Object coachCourses(Long tid) {
        return Map.of("code", 200, "data", courseMapper.findByCoach(tid));
    }
}
