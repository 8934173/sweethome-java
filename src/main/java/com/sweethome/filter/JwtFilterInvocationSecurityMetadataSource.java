package com.sweethome.filter;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取有权访问当前url的角色
 *
 */
//TODO
@Component
public class JwtFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Map<String,String> urlRoleMap = new HashMap<String,String>(){{
        put("/clockIn/**","ROLE_student");
    }};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        System.out.println(requestUrl);
        for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
            if(antPathMatcher.match(entry.getKey(),requestUrl)){
                return SecurityConfig.createList(entry.getValue());
            }
        }
        return SecurityConfig.createList("");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
