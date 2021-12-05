package com.sweethome.controller;

import com.alibaba.fastjson.JSON;
import com.sweethome.entities.*;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.RevertService;
import com.sweethome.utils.JwtUtil;
import com.sweethome.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/revert")
public class RevertController {

    @Autowired
    private RevertService revertService;

    @PostMapping("/applyForRevert")
    @PreAuthorize("hasAuthority('ROLE_student')")
    public R applyForLeaving(@RequestBody RevertEntity revertEntity, HttpServletRequest request) {
        UserEntity userEntity = JwtUtil.getUser(request);
        Object school = userEntity.getSchool();
        StudentEntity student = JSON.parseObject(JSON.toJSONString(school), StudentEntity.class);
        revertEntity.setClassId(student.getCaId());
        try {
            revertService.applyForRevert(revertEntity);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "申请出错！");
        }
    }

    @GetMapping("/getRevertEntitiesByStuId")
    @PreAuthorize("hasAuthority('ROLE_student')")
    public R getLeaveEntitiesByStuId(
            HttpServletRequest request,
            @PathParam("auditStatus") Integer auditStatus,
            @PathParam("uname") String uname) {
        UserEntity userEntity = JwtUtil.getUser(request);
        Object school = userEntity.getSchool();
        StudentEntity student = JSON.parseObject(JSON.toJSONString(school), StudentEntity.class);
        try {
            List<RevertEntity> revertEntities = revertService.getLeaveEntityByStuId(student.getStuId(), auditStatus, uname);
            return R.ok().put("data", revertEntities);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/deleteOne/{outId}")
    @PreAuthorize("hasAuthority('ROLE_student')")
    public R deleteRecordByOutId(@PathVariable("outId") Long outId) {
        try {
            revertService.deleteRecordByOutId(outId);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败");
        }
    }

    @GetMapping("/getRevertEntitiesByClassId/{classId}")
    public R getLeaveEntitiesByClassId(@PathVariable("classId") Long classId,
                                       @PathParam("uname") String uname,
                                       @PathParam("auditStatus") Integer auditStatus) {
        List<RevertEntity> entities = revertService.getRevertEntitiesByClassId(classId, uname, auditStatus);
        return R.ok().put("list", entities);
    }

    @PostMapping("/updateRevert")
    @PreAuthorize("hasAuthority('ROLE_teacher')")
    public R updateLeaveById(@RequestBody RevertEntity revertEntity, HttpServletRequest request) {
        UserEntity userEntity = JwtUtil.getUser(request);
        Object school = userEntity.getSchool();
        TeacherEntity teacherEntity = JSON.parseObject(JSON.toJSONString(school), TeacherEntity.class);
        revertEntity.setTeaId(teacherEntity.getTeaId());
        revertEntity.setTeaName(teacherEntity.getTeaName());
        revertService.updateRevertById(revertEntity);
        return R.ok();
    }
}
