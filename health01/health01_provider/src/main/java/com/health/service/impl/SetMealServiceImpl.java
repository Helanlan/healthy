package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.constant.RedisContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.mapper.OrderMapper;
import com.health.mapper.SetMealMapper;
import com.health.pojo.SetMeal;
import com.health.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private JedisPool jedisPool;//使用redis存储图片名称
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;//从属性文件中读取要生成的html对应的目录

    @Override
    public void add(SetMeal setMeal, Integer[] checkgroupIds) {
        setMealMapper.add(setMeal);
        Integer id = setMeal.getId();
        System.out.println(id);
        System.out.println(checkgroupIds.toString());
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupid : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", id);
                map.put("checkgroupId", checkgroupid);
                setMealMapper.setSetMealAndCheckGroup(map);
            }
        }
        //将存到数据库的图片名称保存到redis
        jedisPool.getResource().sadd(RedisContant.SETMEAL_PIC_DATABASE_RESOURCE, setMeal.getImg());

        //添加套餐后需要重新生成html静态页面
        generateMobileSetmealHtml();
    }


    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        /**
         * 分页查询
         */
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<SetMeal> byPage = setMealMapper.findByPage(queryPageBean.getQueryString());
        return new PageResult(byPage.getTotal(), byPage.getResult());
    }


    String imgNameOld = null;

    @Override
    public SetMeal findById(int id) {
        SetMeal byId = setMealMapper.findById(id);
        imgNameOld = byId.getImg();
        return byId;
    }

    @Override
    public Integer[] findCheckGroupById(int id) {
        return setMealMapper.findCheckGroupById(id);
    }

    @Override
    public void update(SetMeal setMeal, Integer[] checkgroupIds) {
        Integer id = setMeal.getId();
        setMealMapper.deleteSetMealCheckGroupBySetmealId(id);
        setMealMapper.update(setMeal);
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", id);
                map.put("checkgroupId", checkgroupId);
                setMealMapper.setSetMealAndCheckGroup(map);
            }
        }
        if (imgNameOld != null) {
            jedisPool.getResource().srem(RedisContant.SETMEAL_PIC_DATABASE_RESOURCE, imgNameOld);
        }

        jedisPool.getResource().sadd(RedisContant.SETMEAL_PIC_DATABASE_RESOURCE, setMeal.getImg());
    }

    @Override
    public void delete(int id) {
        setMealMapper.deleteToSetmealCheckgroupById(id);//删除关系表中的数据
        SetMeal byId = setMealMapper.findById(id);
        String img = byId.getImg();
        if (img != null) {
            jedisPool.getResource().srem(RedisContant.SETMEAL_PIC_DATABASE_RESOURCE, img);//从redis删除
        }
        setMealMapper.delete(id);

    }


    /**
     * 移动端
     *
     * @return
     */
    @Override
    public List<SetMeal> findAll() {
        return setMealMapper.findAll();
    }

    @Override
    public SetMeal detailFindById(int id) {//详情页需要显示检查组、检查项和套餐本身的详情
        return setMealMapper.detailFindById(id);
    }

    @Override
    public Map<String, Object> countSetmeal(Map<String, Object> map) {
        /*List<SetMeal> all = setMealMapper.findAll();
        List<String> list = new ArrayList<>();//套餐名列表
        List countList = new ArrayList();
        for (SetMeal setMeal : all) {
            Map<String, Object> setMealCountMap = new HashMap();//预约数量，对应套餐的名称
            list.add(setMeal.getName());
            int count = setMealMapper.countOrderBySetmealId(setMeal.getId());
            if (count != 0) {
                setMealCountMap.put("value", count);
                setMealCountMap.put("name", setMeal.getName());
            }
            countList.add(setMealCountMap);
        }

        map.put("setmealName", list);
        map.put("setmealCount", countList);*/
        List<String> listName = new ArrayList<>();//套餐名列表
        List<Map<String, Object>> lists = setMealMapper.countOrderBySetmealId();
        for (Map map1:lists){
            listName.add(map1.get("name").toString());
        }
        map.put("setmealName", listName);
        map.put("setmealCount", lists);
        return map;
    }


    public void generateHtml(String templateName, String htmlPageName, Map map) {
        /**
         * 用于用ftl的模板文件去生成html静态页面,通用
         */
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer writer = null;
        try {
            Template template = configuration.getTemplate(templateName);
            writer = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            template.process(map, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    //生成当前方法所需要的静态页面，包括套餐列表静态页面和套餐详情静态页面
    public void generateMobileSetmealHtml() {
        List<SetMeal> list = setMealMapper.findAll();
        Map map = new HashMap();
        map.put("setmealList", list);
        //套餐列表静态页，可以分别封装
        generateHtml("mobile_setmeal.ftl", "mobile_setmeal.html", map);

        //套餐详情页静态页面,可以分别封装
        for (SetMeal setMeal : list) {
            Map map1 = new HashMap();
            map1.put("setmeal", setMealMapper.detailFindById(setMeal.getId()));
            System.out.println(map1);
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setMeal.getId() + ".html", map1);
        }
    }
}
