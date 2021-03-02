package com.health.pojo;

import java.io.Serializable;
import java.util.Set;

public class Permission  implements Serializable {
    private int id;
    private String name;
    private String keyWord;
    private String description;
    private String isEnable;//是否启用

    public Permission() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", description='" + description + '\'' +
                ", isEnable='" + isEnable + '\'' +
                '}';
    }
}
