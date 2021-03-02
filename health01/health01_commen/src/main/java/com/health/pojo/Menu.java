package com.health.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Menu implements Serializable {
    private int id;
    private String name;
    private String linkUrl;//跳转页面链接
    private String path;
    private int priority;//菜单排序排序凭证
    private String description;
    private int parentMenuId;
    private String icon;
    private int level;//菜单等级
    private List<Menu> childMenus;//子菜单
    private Set<Role> roles;//某个菜单能被多个角色显示出来

    public Menu() {
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(int parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Menu> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<Menu> childMenus) {
        this.childMenus = childMenus;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", path='" + path + '\'' +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", parentMenuId=" + parentMenuId +
                ", icon='" + icon + '\'' +
                ", level=" + level +
                ", childMenus=" + childMenus +
                ", roles=" + roles +
                '}';
    }
}
