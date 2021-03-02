package com.health.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {
    private int id;
    private String birthday;
    private String sex;
    private String userName;
    private String password;
    private String remark;
    private String station;//工作单位
    private String telephone;
    private Set<Role> roles =new HashSet<>();//一个用户有角色role的集合

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", birthday='" + birthday + '\'' +
                ", sex='" + sex + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", remark='" + remark + '\'' +
                ", station='" + station + '\'' +
                ", telephone='" + telephone + '\'' +
                ", roles=" + roles +
                '}';
    }
}
