package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sweethome.valid.AddGroup;
import com.sweethome.valid.UpdateGroup;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ToString
@TableName("sys_user")
public class UserEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "uid", type = IdType.ASSIGN_ID)
    @NotNull(message = "不能指定用户id", groups = AddGroup.class)
    @Null(message = "用户ID不能为空", groups = UpdateGroup.class)
    private String uid;

    /**
     * 用户名称
     */
    @TableField(value = "u_name")
    @NotNull(message = "不能指定用户id", groups = {AddGroup.class, UpdateGroup.class})
    private String uname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户所在班级 []
     */
    private String classId;

    /**
     * 用户名(账号)
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "账号不能为空")
    private String password;

    /**
     * 手机
     */
    private String phone;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 是否过时
     */
    @Getter(value = AccessLevel.NONE)
    @TableField(fill = FieldFill.INSERT)
    private Integer accountNonExpired;

    /**
     * 是否锁定
     */
    @Getter(value = AccessLevel.NONE)
    @TableField(fill = FieldFill.INSERT)
    private Integer accountNonLocked;

    /**
     * 是否可用
     */
    @Getter(value = AccessLevel.NONE)
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "1", delval = "0")
    private Integer enabled;

    /**
     * 角色
     */
    private String role;

    /**
     * 权限
     */
    private String authority;

    @Getter(value = AccessLevel.NONE)
    @TableField(fill = FieldFill.INSERT)
    private Integer credentialsNonExpired;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysRole> roles;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object school;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysAuthor> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return roles == null ? new ArrayList<SimpleGrantedAuthority>() : roles.stream().map(r ->new SimpleGrantedAuthority(r.getRName())).collect(Collectors.toList());
        StringBuffer buffer = new StringBuffer();
        if (roles != null) {
            List<String> role = roles.stream().map(SysRole::getRName).collect(Collectors.toList());
            role.forEach((it) -> {
                buffer.append(it).append(",");
            });
            if (authorities != null) {
                List<String> author = authorities.stream().map(SysAuthor::getAuName).collect(Collectors.toList());
                author.forEach(au -> {
                    buffer.append(au).append(",");
                });
            }
            return AuthorityUtils.commaSeparatedStringToAuthorityList(buffer.substring(0, buffer.length() - 1));
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired == 1;
    }

    @Override
    public boolean isEnabled() {
        return enabled == 1;
    }
}
