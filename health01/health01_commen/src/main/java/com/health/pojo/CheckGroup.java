package com.health.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 检查组
 */
public class CheckGroup implements Serializable{


    private Integer id;
    private String code;//检查组编码
    private String name;//检查组名称
    private String helpCode;//助记码
    private String sex;//适用性别，0不限，1男，其他 女
    private String attention;
    private String remark;//说明
    private List<CheckItem> checkItems;




    public CheckGroup() {
    }

    public Integer getId() {
        return id;
    }

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "CheckGroup{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", helpCode='" + helpCode + '\'' +
                ", sex='" + sex + '\'' +
                ", attention='" + attention + '\'' +
                ", remark='" + remark + '\'' +
                ", checkItems=" + checkItems +
                '}';
    }
}
