package com.itheima.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//  订单
public class Orders {
    private String oid;     //  订单号
    private Date ordertime; //  下单时间
    private double total;   //  订单总金额
    private int state;      //  订单状态，1表示已付款，0表示未付款
    private String address; //  订单地址
    private String name;    //  收货人姓名
    private int telephone;  //  收货人电话
    private User user;      //  下单的用户
    
    //  该订单中有哪些订单项
    List<OrderItem> orderItems = new ArrayList<OrderItem>();
    
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    public String getOid() {
        return oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public Date getOrdertime() {
        return ordertime;
    }
    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTelephone() {
        return telephone;
    }
    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Orders [oid=" + oid + ", ordertime=" + ordertime + ", total=" + total + ", state=" + state
                + ", address=" + address + ", name=" + name + ", telephone=" + telephone + ", user=" + user
                + ", orderItems=" + orderItems + "]";
    }
    
    
}
