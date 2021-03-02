package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;

import java.util.List;


public interface CheckGroupService {
    void addGroupItem(CheckGroup checkGroup,Integer[] checkitemIds);
    PageResult findByPage(QueryPageBean queryPageBean);
    Integer[] findById(int id);//获取检查组中包含的检查项的列表
    CheckGroup findCheckGroupById(int id);//编辑检查组时数据回显
    void updateCheckGroup(CheckGroup checkGroup,Integer[] checkitemIds);
    void deleteCheckGroup(int id);

    /**
     * 体检套餐中的检查组列表显示
     */
    List<CheckGroup> findAll();
}
