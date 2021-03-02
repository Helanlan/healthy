package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.mapper.UserMapper;
import com.health.pojo.User;
import com.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 查询用户名--》通过用户id查角色--》通过角色id查权限
     * 用户授予权限SpringSecurityUserService.java
     */
    @Override
    public User findByUsername(String username) {
        System.out.println(username);
        User user = userMapper.findByUsername(username);//查询用户
        System.out.println("user:"+user);
        if (user==null){
            return null;
        }
        return user;
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }






    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {//分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<User> byPage = userMapper.findByPage(queryPageBean.getQueryString());
        long total = byPage.getTotal();
        List<User> result = byPage.getResult();
        return new PageResult(total,result);
    }

    /**
     * 暂时导出用户数据的时候使用
     */
    @Override
    public List<User> userShowAll() {
        return userMapper.userShowAll();
    }
    @Override
    public List<User> selByIds(Integer[] ids) {
        return userMapper.selByIds(ids);
    }

    /**
     * add
     * User{id=0, birthday='2021-01-02T16:00:00.000Z', sex='2', userName='q', password='q', remark='q', station='null', telephone='null', roles=[]}
     * 2021-01-02T16:00:00.000Z
     * Sat Jan 02 00:00:00 CST 2021
     * 2021-01-02
     */
    @Override
    public void userAdd(User user,Integer[] roleIds) {
        String birthday = user.getBirthday();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        try {
            if (birthday!=null&&birthday.length()>0) {
                Date date = simpleDateFormat.parse(birthday);
                String format = simpleDateFormat.format(date);
                user.setBirthday(format);
            }
            BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();//密码加密
            String encode = encoder.encode(user.getPassword());
            user.setPassword(encode);
            userMapper.userAdd(user);
            for (Integer roleId:roleIds){
                Map<String,Integer>map =new HashMap<>();
                map.put("userId",user.getId());
                map.put("roleId",roleId);
                userMapper.addRoleListToUserRole(map);
            } } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    /**
     * 编辑
     */
    @Override
    public User findUserById(int id) {//数据回显
        return userMapper.findUserById(id);
    }
    @Override
    public Integer[] findRoleCheckList(int id) {//角色列表回显
        return userMapper.findRoleCheckList(id);
    }
    @Override
    public void userEdit(User user,Integer[] roleIds) {
        try {
            String birthday = user.getBirthday();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
            Date date = simpleDateFormat.parse(birthday);
            String format = simpleDateFormat.format(date);
            user.setBirthday(format);
            userMapper.deleteUserRoleByUserId(user.getId());//先原来的删除角色列表
            if (roleIds!=null&&roleIds.length>0){
                for (Integer roleId:roleIds) {
                    Map<String, Integer> map = new HashMap<>();
                    map.put("userId", user.getId());
                    map.put("roleId", roleId);
                    userMapper.addRoleListToUserRole(map);//后添加新的角色列表
                }
            }
            userMapper.userEdit(user);//最后修改用户信息
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userDelete(int id) {
        userMapper.deleteUserRoleByUserId(id);//先在用户角色关系表中删除这个用户赋予的角色列表
        userMapper.userDelete(id);//删除角色
    }


}
