package com.sweethome.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 没有权限处理的类
 */
@Slf4j
public class RestAuthAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e)
            throws IOException {
        PrintWriter writer = httpServletResponse.getWriter();

        try {
            Map<String, String> map = new HashMap<>(2);
            map.put("code", "403");
            map.put("msg", "您无权操作");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            writer.write(new ObjectMapper().writeValueAsString(map));
            log.info("没有权限!");
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
