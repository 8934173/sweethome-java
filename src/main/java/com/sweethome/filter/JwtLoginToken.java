package com.sweethome.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 保存当前用户的认证信息,如认证状态,用户名密码,拥有的权限等
 */
public class JwtLoginToken extends AbstractAuthenticationToken {

    /**登录用户信息*/
    private final Object principal;
    /**密码*/
    private final Object credentials;

    public JwtLoginToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    /**创建一个已认证的授权令牌,如注释中说的那样,这个方法应该由AuthenticationProvider来调用
     * 也就是我们写的JwtAuthenticationProvider,有它完成认证后再调用这个方法,
     * 这时传入的principal为从userService中查出的UserDetails
     * @param principal
     * @param credentials
     * @param authorities
     */
    public JwtLoginToken(Object principal, Object credentials,
                         Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }
}
