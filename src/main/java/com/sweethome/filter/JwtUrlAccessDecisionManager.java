package com.sweethome.filter;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.util.Collection;

/**
 * 校验当前用户是否具有访问该路径的角色
 */
@Component
public class JwtUrlAccessDecisionManager implements AccessDecisionManager {

    /**
     *
     * @param authentication 当前用户凭证 -- > JwtTokenFilter中将通过验证的用户保存在Security上下文中, 即传入了这里
     * @param o 当前请求路径
     * @param collection 当前请求路径所需要的角色列表 -- > 从 JwtFilterInvocationSecurityMetadataSource 返回
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute next : collection) {
            //获取当前路径所需要的权限
            String role = next.getAttribute();
            if (!StringUtils.hasLength(role)) {
                return;
            }
            //当前用户的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(role)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
