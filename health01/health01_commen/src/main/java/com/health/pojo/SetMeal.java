package com.health.pojo;

import java.io.Serializable;
import java.util.List;

public class SetMeal implements Serializable {
    private Integer id;
    private String name;
    private String code;
    private String helpCode;
    private float price;
    private String sex;
    private String age;
    private String remark;
    private String attention;
    private String img;//套餐对应的图片存储路劲
    private List<CheckGroup> checkGroups;

    public SetMeal() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<CheckGroup> getCheckGroups() {
        return checkGroups;
    }

    public void setCheckGroups(List<CheckGroup> checkGroups) {
        this.checkGroups = checkGroups;
    }

    @Override
    public String toString() {
        return "SetMeal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", helpCode='" + helpCode + '\'' +
                ", price=" + price +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", remark='" + remark + '\'' +
                ", attention='" + attention + '\'' +
                ", img='" + img + '\'' +
                ", checkGroups=" + checkGroups +
                '}';
    }
}
