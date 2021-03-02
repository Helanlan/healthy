package com.health.mapper;

import com.github.pagehelper.Page;
import com.health.pojo.SetMeal;

import java.util.List;
import java.util.Map;

public interface SetMealMapper {
    /**
     * 添加套餐信息
     */
    void add(SetMeal setMeal);
    void setSetMealAndCheckGroup(Map<String,Integer> map);
    Page<SetMeal> findByPage(String queryString);//分页查询

    /**
     * 修改套餐信息
     */
    SetMeal findById(int id);
    Integer[] findCheckGroupById(int setmealId);
    void update(SetMeal setMeal);
    void deleteSetMealCheckGroupBySetmealId(int setmealId);

    /**
     * 删除
     */
    void delete(int id);
    void deleteToSetmealCheckgroupById(int setmealId);


    /**
     * 统计每个套餐预约占比
     */
    List<Map<String,Object>> countOrderBySetmealId();

//    SetMeal selByid(int setmealId,String setmealNaame);





    /**
     * 移动端的
     */
    List<SetMeal> findAll();
    SetMeal detailFindById(int id);




}
