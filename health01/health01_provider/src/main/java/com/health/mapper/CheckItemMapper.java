package com.health.mapper;

import com.github.pagehelper.Page;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;
import org.apache.zookeeper.Op;

import java.util.List;

public interface CheckItemMapper {
    void add(CheckItem checkItem);

    Page<CheckItem> selAll(String selectString);

    long findCountByCheckItemId(Integer id);/*查询检查项是否在检查组里面(数量大于1就说明在检查组里面)，要是再的话就不能做删除操作*/

    void deleteCheckItem(int id);

    CheckItem findById(Integer id);//编辑的时候的数据回显，

    void updateCheckItem(CheckItem checkItem);

    List<CheckItem> findAll();
}
