package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.health.constant.MessageContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.mapper.CheckItemMapper;
import com.health.pojo.CheckItem;
import com.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemMapper;

    public void add(CheckItem checkItem) {
        checkItemMapper.add(checkItem);
    }

    @Override
    public PageResult selAll(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemMapper.selAll(queryString);

        long total = page.getTotal();
        List<CheckItem> result = page.getResult();
        return new PageResult(total,result);
    }

    @Override
    public void deleteCheckItem(int id) {
        long countByCheckItemId = checkItemMapper.findCountByCheckItemId(id);
        if (countByCheckItemId<=0) {
            checkItemMapper.deleteCheckItem(id);
        }else {
           throw new RuntimeException("请先在检查组中删除！！！");
        }
    }

    @Override
    public CheckItem findById(Integer id) {//编辑的时候的数据回显，
        return checkItemMapper.findById(id);
    }

    @Override
    public void updateCheckItem(CheckItem checkItem) {
        checkItemMapper.updateCheckItem(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> all = checkItemMapper.findAll();
        return all;
    }


}
