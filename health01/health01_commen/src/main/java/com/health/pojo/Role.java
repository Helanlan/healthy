package com.health.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Role implements Serializable {
    private int id;
    private String name;
    private String keyWord;
    private String description;
    private String isEnable;
    private Set<Permission> permissions = new HashSet<>();//一个角色有多个权限，即角色有权限的集合
    private Set<User> users;//同一个角色有多个用户，即角色有用户的集合
    private Set<Menu> menus;//一个角色下有不同的多个菜单，即角色有菜单的集合


    public Role() {
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }


    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", description='" + description + '\'' +
                ", isEnable='" + isEnable + '\'' +
                ", permissions=" + permissions +
                ", users=" + users +
                ", menus=" + menus +
                '}';
    }
}
