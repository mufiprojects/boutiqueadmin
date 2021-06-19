package com.example.acer.boutiqueadmin;

public class designerOrderDetail {
    public int noOfOrders;
    public int orderAmount;

    public designerOrderDetail(int noOfOrders, int orderAmount) {
        this.noOfOrders = noOfOrders;
        this.orderAmount = orderAmount;
    }

    public designerOrderDetail() {

    }

    public int getNoOfOrders() {
        return noOfOrders;
    }

    public void setNoOfOrders(int noOfOrders) {
        this.noOfOrders = noOfOrders;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Override
    public String toString() {
        return "designerOrderDetail{" +
                "noOfOrders=" + noOfOrders +
                ", orderAmount=" + orderAmount +
                '}';
    }
}
