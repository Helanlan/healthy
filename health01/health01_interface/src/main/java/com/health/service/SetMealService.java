package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.SetMeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    /**
     * 添加套餐信息
     */
    void add(SetMeal setMeal, Integer[] checkgroupIds);
    /**
     * 分页查询
     */
    PageResult findByPage(QueryPageBean queryPageBean);

    /**
     * 修改套餐信息
     */
    SetMeal findById(int id);//数据回显
    Integer[] findCheckGroupById(int id);
    void update(SetMeal setMeal,Integer[] checkgroupIds);

    /**
     * 删除
     */
    void delete(int id);

    /*移动端*/
    List<SetMeal> findAll();
    SetMeal detailFindById(int id);

    Map<String,Object> countSetmeal(Map<String,Object> map);
}
