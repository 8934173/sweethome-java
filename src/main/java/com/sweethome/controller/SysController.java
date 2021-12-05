package com.sweethome.controller;

import cn.hutool.core.util.IdUtil;
import com.sweethome.entities.StudentEntity;
import com.sweethome.entities.SysRole;
import com.sweethome.entities.TeacherEntity;
import com.sweethome.entities.UserEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.SysRoleService;
import com.sweethome.service.impl.UserServiceImpl;
import com.sweethome.utils.DateUtilSweet;
import com.sweethome.utils.PageUtil;
import com.sweethome.utils.R;
import com.sweethome.utils.RandomValidateCodeUtil;
import com.sweethome.valid.AddGroup;
import com.sweethome.vo.PageVo;
import com.sweethome.vo.UserEntityVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys")
public class SysController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("captcha.jpg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            log.info("获取验证码失败{}", e.getMessage());
        }
    }

    @GetMapping("/getUsersByAdmin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_USER')")
    public R getUsersByAdmin(PageVo params) {
        PageUtil usersByAdmin = userService.getUsersByAdmin(params);
        return R.ok().put("data", usersByAdmin);
    }

    @GetMapping("/getAllRole")
    public R getAllRole() {
        List<SysRole> allRole = sysRoleService.getAllRole();
        return R.ok().put("data", allRole);
    }

    /**
     * 老师添加学生功能还未完成
     * @param userEntityVo 用户
     * @return 响应
     */
    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_USER')")
    public R saveUser(@RequestBody UserEntityVo userEntityVo) {
        try {
            String password = userEntityVo.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String s = passwordEncoder.encode(password);
            userEntityVo.setPassword(s);
            String role = userEntityVo.getRole();
            if (role.equals("2")) {
                TeacherEntity teacherEntity = new TeacherEntity();
                BeanUtils.copyProperties(userEntityVo, teacherEntity);
                userService.saveUser(userEntityVo, teacherEntity);
                return R.ok();
            }
            if (role.equals("3")) {
                StudentEntity studentEntity = new StudentEntity();
                BeanUtils.copyProperties(userEntityVo, studentEntity);
                userService.saveUser(userEntityVo, studentEntity);
                return R.ok();
            }
            userService.saveUser(userEntityVo, null);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "保存失败");
        }
    }

    @PostMapping("/lockOrUnlockAccount")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_USER')")
    public R lockOrUnlockAccount(
            @RequestParam("uid") String uid,
            @RequestParam("status") Integer status) {
        userService.lockOrUnlockAccount(uid, status);
        return R.ok();
    }

    @PostMapping("/deleteUserByAdmin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_USER')")
    public R deleteUserByAdmin(@RequestParam("uid") String uid, @RequestParam("role") Integer role) {
        try {
            userService.deleteAndUserByUid(uid, role);
            return R.ok();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败");
        }
    }
}
