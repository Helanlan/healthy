package com.health.jobs;

import com.health.constant.RedisContant;
import com.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJobs {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //集合差值
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisContant.SETMEAL_PIC_RESOURCE, RedisContant.SETMEAL_PIC_DATABASE_RESOURCE);
        if (sdiff!=null){
            for (String imgName:sdiff){
                QiNiuUtils.delFileToQiNiu(imgName);//从云端删除
                jedisPool.getResource().srem(RedisContant.SETMEAL_PIC_RESOURCE,imgName);//从redis删除
                System.out.println("清理了图片："+imgName);
            }
        }
    }
}
