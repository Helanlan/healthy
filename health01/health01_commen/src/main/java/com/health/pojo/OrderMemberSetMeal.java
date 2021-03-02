package com.health.pojo;

import java.io.Serializable;

public class OrderMemberSetMeal implements Serializable {
    private Order order;
    private Member member;
    private SetMeal setMeal;

    public OrderMemberSetMeal() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    @Override
    public String toString() {
        return "OrderMemberSetMeal{" +
                "order=" + order +
                ", member=" + member +
                ", setMeal=" + setMeal +
                '}';
    }
}
