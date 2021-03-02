package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.mapper.CheckGroupMapper;
import com.health.pojo.CheckGroup;
import com.health.pojo.CheckItem;
import com.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Override
    public void addGroupItem(CheckGroup checkGroup,Integer[] checkitemIds) {
        //新增检查组，同时检查组关联检查项
        checkGroupMapper.addGroupItem(checkGroup);
        //设置检查组和检查项的多对多的关联关系，添加t_checkgroup_checkitem
        Integer id = checkGroup.getId();
        if (checkitemIds!=null&&checkitemIds.length>0) {
            for (Integer checkitem : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupid", id);
                map.put("checkitemIds", checkitem);
                checkGroupMapper.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {//检查组分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> checkGroups = checkGroupMapper.findByPage(queryPageBean.getQueryString());
        return new PageResult(checkGroups.getTotal(),checkGroups.getResult());
    }

    @Override
    public Integer[] findById(int id) {//获取检查组中包含的检查项的列表
        return checkGroupMapper.findById(id);
    }

    @Override
    public CheckGroup findCheckGroupById(int id) {//编辑检查组时数据回显
        return checkGroupMapper.findCheckGroupById(id);
    }

    @Override
    public void updateCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        /**
         * 跟新检查项列表方法
         * 1、新的检查项列表跟旧的检查项列表对比，跟旧的比较删除新的里面没有的，跟新的对比添加旧的里面没有的
         * 2、删除检查组原来的全部检查项，将新的检查项全部重新添加
         */
        checkGroupMapper.updateCheckGroup(checkGroup);
        Integer[] checkItemIdsOld = checkGroupMapper.findById(checkGroup.getId());

        for (int i=0;i<checkitemIds.length;i++){//找出新的数组里面比旧的多的部分
            boolean isOk =true;
            int temp=checkitemIds[i];
            for (int j=0;j<checkItemIdsOld.length;j++){
                if (temp==checkItemIdsOld[j]){
                    isOk=false;
                    break;
                }
            }
            if (isOk){
                //新的比就得多则将多出来的添加到t_checkgroup_checkitem表
                Map<String,Integer> map=new HashMap<>();
                map.put("checkgroupid",checkGroup.getId());
                map.put("checkitemId",temp);
                checkGroupMapper.addCheckItemToGroup(map);
            }
        }
        for (int i=0;i<checkItemIdsOld.length;i++){
            /**
             * 删除原来的数组列表中比新数组多的，删除
             */
            boolean isOk =true;
            int checkitemId=checkItemIdsOld[i];
            for (int j=0;j<checkitemIds.length;j++){
                if (checkitemId==checkitemIds[j]){
                    isOk=false;
                    break;
                }
            }
            if (isOk){
                Map<String,Integer> map=new HashMap<>();
                map.put("checkitemId",checkitemId);
                map.put("checkgroupId",checkGroup.getId());
                checkGroupMapper.delCheckItemToGroup(map);
            }
        }
    }

    @Override
    public void deleteCheckGroup(int id) {
        checkGroupMapper.delCheckItemByCheckCheckGroup(id);
        checkGroupMapper.deleteCheckGroup(id);
    }


    /**
     * 体检套餐中的检查组列表显示
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }
}
