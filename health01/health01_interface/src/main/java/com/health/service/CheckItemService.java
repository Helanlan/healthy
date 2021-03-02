package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    void add(CheckItem checkItem);
    PageResult selAll(QueryPageBean queryPageBean);
    void deleteCheckItem(int id);//删除检查项
    CheckItem findById(Integer id);//编辑的时候的数据回显，
    void updateCheckItem(CheckItem checkItem);

    List<CheckItem> findAll();
}
