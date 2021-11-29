package com.sweethome.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweethome.entities.StudentEntity;
import com.sweethome.entities.TeacherEntity;
import com.sweethome.entities.UserEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.StudentService;
import com.sweethome.service.TeacherService;
import com.sweethome.utils.JwtUtil;
import com.sweethome.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("GBK");
        try {
            String captcha = request.getParameter("captcha");
            HttpSession session = request.getSession();
            String randomCap = (String) session.getAttribute("RANDOM_VALIDATE_CODE_KEY");
            if (!StringUtils.hasLength(randomCap) || !randomCap.equals(captcha)) {
                response.getWriter().write(JSON.toJSONString(R.error(BizCodeEnum.CAPTCHA_EXCEPTION.getCode(), BizCodeEnum.CAPTCHA_EXCEPTION.getMsg())));
                return;
            }
            UserEntity user = (UserEntity) authentication.getPrincipal();
            String role = user.getRole();
            if ("2".equals(role)) {
                TeacherEntity teacher = teacherService.getTeacherOneByUid(user.getUid());
                user.setSchool(teacher);
            }
            if ("3".equals(role)) {
                StudentEntity student = studentService.getStudentOneByUid(user.getUid());
                user.setSchool(student);
            }
            String s = JSON.toJSONString(user);
            String token = JwtUtil.createToken(s, user.getUsername(), "", "sweethome", new Date().toString());
            user.setPassword(null);
            HashMap<String, Object> map = new HashMap<>();
            map.put("SWEET_HOME_TOKEN", token);
            map.put("userInfo", user);
            out.write(new ObjectMapper().writeValueAsString(R.ok().put("data",map)));
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}
