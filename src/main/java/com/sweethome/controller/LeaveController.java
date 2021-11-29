package com.sweethome.controller;

import com.alibaba.fastjson.JSON;
import com.sweethome.entities.LeaveEntity;
import com.sweethome.entities.StudentEntity;
import com.sweethome.entities.TeacherEntity;
import com.sweethome.entities.UserEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.LeaveService;
import com.sweethome.service.SchoolClassService;
import com.sweethome.utils.Constant;
import com.sweethome.utils.JwtUtil;
import com.sweethome.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    private SchoolClassService schoolClassService;

    @PostMapping("/applyForLeaving")
    public R applyForLeaving(@RequestBody LeaveEntity leaveEntity, HttpServletRequest request) {
        UserEntity userEntity = JwtUtil.getUser(request);
        Object school = userEntity.getSchool();
        StudentEntity student = JSON.parseObject(JSON.toJSONString(school), StudentEntity.class);
        leaveEntity.setClassId(student.getCaId());
        try {
            leaveService.applyForLeaving(leaveEntity);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "申请出错！");
        }
    }

    @GetMapping("/getLeaveEntitiesByStuId")
    public R getLeaveEntitiesByStuId(
            HttpServletRequest request,
            @PathParam("auditStatus") Integer auditStatus,
            @PathParam("uname") String uname) {
        String header = request.getHeader(Constant.TOKEN);
        String userDetails = (String) JwtUtil.parseToken(header).getBody().get("userDetails");
        UserEntity userEntity = JSON.parseObject(userDetails, UserEntity.class);
        Object school = userEntity.getSchool();
        StudentEntity student = JSON.parseObject(JSON.toJSONString(school), StudentEntity.class);
        try {
            List<LeaveEntity> leaveEntityList = leaveService.getLeaveEntitiesByStuId(student.getStuId(), auditStatus, uname);
            return R.ok().put("data", leaveEntityList);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/deleteOne/{outId}")
    public R deleteRecordByOutId(@PathVariable("outId") Long outId) {
        try {
            leaveService.deleteRecordByOutId(outId);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败");
        }
    }

    @GetMapping("/getLeaveEntitiesByClassId/{classId}")
    public R getLeaveEntitiesByClassId(@PathVariable("classId") Long classId,
                                       @PathParam("uname") String uname,
                                       @PathParam("auditStatus") Integer auditStatus) {
        List<LeaveEntity> entities = leaveService.getLeaveEntitiesByClassId(classId, uname, auditStatus);
        return R.ok().put("list", entities);
    }

    @PostMapping("/updateLeave")
    public R updateLeaveById(@RequestBody LeaveEntity leaveEntity, HttpServletRequest request) {
        UserEntity userEntity = JwtUtil.getUser(request);
        Object school = userEntity.getSchool();
        TeacherEntity teacherEntity = JSON.parseObject(JSON.toJSONString(school), TeacherEntity.class);
        leaveEntity.setTeaId(teacherEntity.getTeaId());
        leaveEntity.setTeaName(teacherEntity.getTeaName());
        leaveService.updateLeaveById(leaveEntity);
        return R.ok();
    }
}
