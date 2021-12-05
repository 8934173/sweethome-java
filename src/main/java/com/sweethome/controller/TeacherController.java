package com.sweethome.controller;

import com.sweethome.entities.TeacherEntity;
import com.sweethome.service.TeacherService;
import com.sweethome.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @PreAuthorize("hasAuthority('ROLE_ADMIN_USER')")
    @GetMapping("/getAll")
    public R getAllTeacher() {
        List<TeacherEntity> allTeacher = teacherService.getAllTeacher();
        return R.ok().put("data", allTeacher);
    }
}
