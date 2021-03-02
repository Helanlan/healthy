package com.health.mapper;

import com.github.pagehelper.Page;
import com.health.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupMapper {
    /**
     * 添加检查组
     */
    void addGroupItem(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map<String,Integer> map);

    /**
     * 分页查询
     */
    Page<CheckGroup> findByPage(String findString);//检查组分页查询



    /**
     * 跟新检查组
     * @param
     */
    Integer[] findById(int id);//获取检查组中包含的检查项的列表
    CheckGroup findCheckGroupById(int id);//编辑检查组时数据回显
    void updateCheckGroup(CheckGroup checkGroup);
    void delCheckItemToGroup(Map<String,Integer> map);
    void addCheckItemToGroup(Map<String,Integer> map);

    /**
     * 删除检查组
     * @param id
     */
    void deleteCheckGroup(int id);
    void delCheckItemByCheckCheckGroup(int id);//删除检查组时要先删除在关系表中此检查组id的列

    /**
     * 体检套餐中的检查组列表显示
     */
    List<CheckGroup> findAll();
}
