package com.sweethome.controller;

import com.alibaba.fastjson.JSON;
import com.sweethome.entities.ClassEntity;
import com.sweethome.entities.ClockInEntity;
import com.sweethome.entities.TeacherEntity;
import com.sweethome.entities.UserEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.ClockInService;
import com.sweethome.service.SchoolClassService;
import com.sweethome.utils.*;
import com.sweethome.valid.AddGroup;
import com.sweethome.valid.UpdateGroup;
import com.sweethome.vo.PageVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/clockIn")
public class ClockInController {

    @Autowired
    private ClockInService clockInService;

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private RedisT redisT;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_student')")
    public R saveClockIn(@Validated({AddGroup.class}) @RequestBody ClockInEntity clock) {
        clockInService.saveClockIn(clock);
        return R.ok();
    }

    @GetMapping("/getClockInListByStudent/{uid}")
    @PreAuthorize("hasAnyAuthority('ROLE_student, ROLE_teacher')")
    public R getClockInListByStudent(PageVo params,
                                     @PathVariable("uid") String uid) {
        PageUtil clockIns = clockInService.getClockIns(params, uid);
        return R.ok().put("data", clockIns);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_student')")
    public R updateClockInById(@Validated({UpdateGroup.class}) @RequestBody ClockInEntity clockInEntity) {
        boolean update = clockInService.updateClockInByCid(clockInEntity);
        return update ? R.ok() : R.error();
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_student')")
    public R deleteClockInByCid(@RequestParam String cid) {
        boolean delete = clockInService.deleteClockInByCid(cid);
        return R.ok().put("data", delete);
    }

    @GetMapping("/getClockInByTeacher")
    @PreAuthorize("hasAuthority('ROLE_teacher')")
    public R getClockInByTeacher(HttpServletRequest request) {
        String header = request.getHeader(Constant.TOKEN);
        String userDetails = (String)JwtUtil.parseToken(header).getBody().get("userDetails");
        UserEntity userEntity = JSON.parseObject(userDetails, UserEntity.class);
        Object school = userEntity.getSchool();

        TeacherEntity teacher = JSON.parseObject(JSON.toJSONString(school), TeacherEntity.class);
        List<ClassEntity> classByTeacher = schoolClassService.getClassByTeacher(teacher.getTeaId());
        return R.ok().put("data", classByTeacher);
    }

    @GetMapping("/receive/{uid}")
    @PreAuthorize("hasAuthority('ROLE_student')")
    public R receiveReminder(@PathVariable("uid") String uid) {
        try {
            redisT.del(uid);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "操作失败");
        }
    }
}
