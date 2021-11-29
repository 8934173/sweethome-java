package com.sweethome.filter;

import com.alibaba.fastjson.JSON;
import com.sweethome.entities.SysAuthor;
import com.sweethome.entities.SysRole;
import com.sweethome.entities.UserEntity;
import com.sweethome.service.SysAuthorService;
import com.sweethome.service.SysRoleService;
import com.sweethome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 返回 user 对象，里面有用户密码和操作权限
 */
@Service
public class SecurityUserDetailServiceImp implements UserDetailsService {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysAuthorService authorService;

    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity user = userService.queryByUserName(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        SysRole role = sysRoleService.selectRoleByUserId(user.getRole());
        System.out.println(role);
        List<String> authors = JSON.parseArray(user.getAuthority(), String.class);
        List<SysAuthor> authorities = authorService.getAuthorByIds(authors);
        user.setAuthorities(authorities);

        ArrayList<SysRole> roles = new ArrayList<>();
        roles.add(role);
        // 用户权限
        // List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(role.getRName());
        user.setRoles(roles);
        //ArrayList<SysRole> sysRoles = new ArrayList<>();
        //sysRoles.add(role);
        //user.setRoles(sysRoles);
        // 返回用户的信息，包括用户权限
        //return new User(user.getUsername(), user.getPassword(), authorityList);
        return user;
    }
}
