package com.health.pojo;

import java.io.Serializable;

public class Member implements Serializable {
    private int id;
    private String fileNumber;
    private String name;
    private String sex;
    private String idCard;
    private String phoneNumber;
    private String regTime;
    private String password;
    private String email;
    private String birthday;
    private String remark;

    public Member() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", fileNumber='" + fileNumber + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", regTime='" + regTime + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
