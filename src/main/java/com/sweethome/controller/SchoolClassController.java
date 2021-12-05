package com.sweethome.controller;

import com.alibaba.fastjson.JSON;
import com.sweethome.entities.ClassEntity;
import com.sweethome.entities.TeacherEntity;
import com.sweethome.entities.UserEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.SchoolClassService;
import com.sweethome.utils.JwtUtil;
import com.sweethome.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/schoolClass")
public class SchoolClassController {

    @Autowired
    private SchoolClassService schoolClassService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_USER')")
    public R saveSchoolClass(@RequestBody ClassEntity classEntity) {
        try {
            schoolClassService.saveSchoolClass(classEntity);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_USER')")
    public R deleteSchoolClass(
            @RequestParam("caId") Long caId,
            @RequestParam("teacherId") Long teaId,
            @RequestParam("majorId") Long maId) {
        try {
            schoolClassService.deleteSchoolClassById(caId, teaId, maId);
            return R.ok();
        } catch (RuntimeException e){
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败");
        }
    }

    @GetMapping("/getClassByTeacher")
    public R getClassByTeacher(HttpServletRequest request) {
        UserEntity user = JwtUtil.getUser(request);
        Object school = user.getSchool();
        TeacherEntity teacherEntity = JSON.parseObject(JSON.toJSONString(school), TeacherEntity.class);
        List<ClassEntity> list = schoolClassService.getClassByTeacher(teacherEntity.getTeaId());
        return R.ok().put("data", list);
    }
}
