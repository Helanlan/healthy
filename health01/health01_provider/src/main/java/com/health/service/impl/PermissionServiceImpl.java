package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.mapper.PermissionMapper;
import com.health.pojo.Permission;
import com.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public PageResult showAll(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Permission> permissions = permissionMapper.showAll(queryPageBean.getQueryString());
        long total = permissions.getTotal();
        List<Permission> result = permissions.getResult();
        return new PageResult(total,result);
    }

    @Override
    public void add(Permission permission) {
        permissionMapper.add(permission);
    }


    /**
     * 修改
     * @param id
     * @return
     */
    @Override
    public Permission findPermissionById(int id) {//数据回显
        return permissionMapper.findPermissionById(id);
    }
    @Override
    public void edit(Permission permission) {//修改
        permissionMapper.edit(permission);
    }

    /**
     * 删除前先检查这个权限是否在某个角色中
     * @param id
     */
    @Override
    public void deletePermission(int id) {
        long count = permissionMapper.selCountRolePermissionByPermissionId(id);
        if (count<=0){
            permissionMapper.deletePermission(id);
        }else {
            throw new RuntimeException("请先在角色中删除");
        }
    }

    @Override
    public List<Permission> findAllList() {
        return permissionMapper.findAllList();
    }
}
