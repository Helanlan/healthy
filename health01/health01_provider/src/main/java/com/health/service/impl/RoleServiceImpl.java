package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.mapper.MenuMapper;
import com.health.mapper.RoleMapper;
import com.health.pojo.Role;
import com.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Role> page = roleMapper.findPage(queryPageBean.getQueryString());
        List<Role> result = page.getResult();
        long total = page.getTotal();
        return new PageResult(total,result);
    }

    @Override
    public void add(Role role,Integer[] permissions,Integer[] menus) {
        roleMapper.add(role);
        int roleId = role.getId();
        //添加权限
        if (permissions!=null&&permissions.length>0){
            for (int permissionId:permissions){
                Map<String,Integer> pMap=new HashMap<>();
                pMap.put("roleId",roleId);
                pMap.put("permissionId",permissionId);
                roleMapper.setRolePermission(pMap);
            }
        }
        //添加菜单
        if (menus!=null&&menus.length>0){
            for (int menuId:menus){
                Map<String,Integer> map=new HashMap<>();
                map.put("roleId",roleId);
                map.put("menuId",menuId);
                roleMapper.setRoleMenu(map);
            }
        }
        System.out.println(roleId);
        System.out.println("pes"+permissions);
        System.out.println("menus"+menus);
    }

    /**
     * 弹出编辑窗口时需要数据回显
     */
    @Override
    public Role findRoleById(int id) {
        return roleMapper.findRoleById(id);
    }

    @Override
    public void edit(Role role, Integer[] permissions, Integer[] menus) {
        int id = role.getId();

        System.out.println(id);
        System.out.println("pes"+permissions);
        System.out.println("menus"+menus);

        roleMapper.delRolePermissionByRoleId(id);//关系表中删除role.getId()下的权限
        for (Integer permissionId:permissions){
            Map<String,Integer> map=new HashMap<>();
            map.put("roleId",id);
            map.put("permissionId",permissionId);
            roleMapper.editRolePermissionByRoleId(map);
        }
        roleMapper.delRoleMenuByRoleId(role.getId());//关系表中删除role.getId()下的菜单
        for (Integer menuId:menus){
            Map<String,Integer> map=new HashMap<>();
            map.put("roleId",id);
            map.put("menuId",menuId);
            roleMapper.editRoleMenuByRoleId(map);
        }
        roleMapper.edit(role);
    }



    @Override
    public void deleteRole(int id) {
        roleMapper.delRolePermissionByRoleId(id);//添加/编辑角色时给角色添加了权限，删除角色则需要删除角色权限关系表中role_id=这个角色id的数据
        roleMapper.delRoleMenuByRoleId(id);//添加/编辑角色时给角色添加了菜单，删除角色则需要删除角色菜单关系表中role_id=这个角色id的数据
        roleMapper.deleteUserRoleByRoleId(id);//添加/编辑用户时给用户添加了角色，删除角色则需要删除用户角色关系表中role_id=这个角色id的数据
        roleMapper.deleteRole(id);
    }

    @Override
    public Integer[] findPermissionCheckedList(int id) {
        return roleMapper.findPermissionCheckedList(id);
    }

    @Override
    public Integer[] findMenuCheckedList(int id) {
        return roleMapper.findMenuCheckedList(id);
    }


    /**
     * 暂时在添加用户时使用
     * 用户添加/编辑页面要显示的角色信息
     */
    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }
}
