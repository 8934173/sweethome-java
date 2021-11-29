package com.sweethome.filter;

import com.alibaba.fastjson.JSON;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.utils.R;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = "登陆失败";
        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException) {
            msg = "账户名或者密码输入错误!";
        } else if (exception instanceof LockedException) {
            msg = "账户被锁定，请联系管理员!";
        } else if (exception instanceof CredentialsExpiredException) {
            msg = "密码过期，请联系管理员!";
        } else if (exception instanceof AccountExpiredException) {
            msg = "账户过期，请联系管理员!";
        } else if (exception instanceof DisabledException) {
            msg = "账户被禁用，请联系管理员!";
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(R.error(BizCodeEnum.LOGIN_FAIL.getCode(), msg)));
    }
}
