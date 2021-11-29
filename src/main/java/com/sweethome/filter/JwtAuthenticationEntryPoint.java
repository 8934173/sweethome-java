package com.sweethome.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweethome.utils.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = httpServletResponse.getWriter();
        try {
            writer.write(new ObjectMapper().writeValueAsString(R.error(403, "您无权限操作")));
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
