package com.health.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约订单
 */
public class Order implements Serializable {
    public static final String ORDERTYPE_TELEPHONE="电话预约";
    public static final String ORDERTYPE_WEIXIN="微信预约";
    public static final String ORDERSTATE_YES="已到诊";
    public static final String ORDERSTATE_NO="未到诊";
    private int id;
    private int memberId;
    private String orderDate;
    private String orderType;
    private String orderState;
    private int setmealId;
    private Member member;
    private SetMeal setMeal;

    public Order() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public SetMeal getSetMeal() {
        return setMeal;
    }

    public void setSetMeal(SetMeal setMeal) {
        this.setMeal = setMeal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public int getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(int setmealId) {
        this.setmealId = setmealId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", orderDate=" + orderDate +
                ", orderType='" + orderType + '\'' +
                ", orderState='" + orderState + '\'' +
                ", setmealId=" + setmealId +
                '}';
    }
}
