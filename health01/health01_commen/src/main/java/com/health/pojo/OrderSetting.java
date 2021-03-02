package com.health.pojo;

import java.io.Serializable;

public class OrderSetting implements Serializable {
    private int id;
    private String orderDate;
    private int number;
    private int reservations;

    public OrderSetting(String orderDate, int number) {
        this.orderDate = orderDate;
        this.number = number;
    }

    public OrderSetting() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getReservations() {
        return reservations;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "OrderSetting{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", number=" + number +
                ", reservations=" + reservations +
                '}';
    }
}
