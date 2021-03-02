package com.health.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    /**
     * 需要用dubbo通过网络远程调用服务提供方获取数据库中的用户信息
     * 查询用户名--》通过用户id查角色--》通过角色id查权限
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        User user = userService.findByUsername(username);
        System.out.println(user);
        if (user == null) {
            return null;
        }
        Set<Role> roles = user.getRoles();
        List<GrantedAuthority> list = new ArrayList<>();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority(role.getKeyWord()));//添加角色
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyWord()));//添加权限
            }
        }
        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return securityUser;
    }
}
