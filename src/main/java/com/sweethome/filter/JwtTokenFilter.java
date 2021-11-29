package com.sweethome.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweethome.entities.UserEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.utils.JwtUtil;
import com.sweethome.utils.R;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * token 有效性拦截器
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authentication");
        response.setContentType("application/json;charset=UTF-8");

        try {
            if (!StringUtils.hasLength(token)) {
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(R.error(BizCodeEnum.NOT_LOGIN.getCode(), BizCodeEnum.NOT_LOGIN.getMsg())));
                return;
            }
            Claims claims = JwtUtil.parseToken(token).getBody();
            if (JwtUtil.isTokenExpired(claims)) {
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(R.error(BizCodeEnum.LOGIN_FAIL.getCode(), BizCodeEnum.LOGIN_FAIL.getMsg())));
                return;
            }
            UserEntity userDetails = JSON.parseObject(claims.get("userDetails", String.class), UserEntity.class);
            JwtLoginToken jwtLoginToken = new JwtLoginToken(userDetails, "", userDetails.getAuthorities());
            jwtLoginToken.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(jwtLoginToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(R.error(BizCodeEnum.LOGIN_FAIL.getCode(), BizCodeEnum.LOGIN_FAIL.getMsg())));
            } else {
                e.printStackTrace();
            }
            //throw new BadCredentialsException("登录凭证失败，请重新登录");
        }
    }
}
