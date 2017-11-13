package com.itheima.domain;

public class OrderItem {
    private String itemid;      //  订单项的id
    private int count;          //  订单项的数量
    private double subtotal;    //  单个订单项的小计金额
    private Product product;    //  订单项包含的商品
    private Orders orders;      //  该订单项属于哪个订单
    public String getItemid() {
        return itemid;
    }
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Orders getOrders() {
        return orders;
    }
    public void setOrders(Orders orders) {
        this.orders = orders;
    }
    @Override
    public String toString() {
        return "OrderItem [itemid=" + itemid + ", count=" + count + ", subtotal=" + subtotal + ", product=" + product
                + ", orders=" + orders + "]";
    }
    

}
