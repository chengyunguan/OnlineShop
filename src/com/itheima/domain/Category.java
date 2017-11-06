package com.itheima.domain;

public class Category {
    private String cid;
    private String pid;
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    @Override
    public String toString() {
        return "Category [cid=" + cid + ", pid=" + pid + "]";
    }
    

}
