package com.itheima.domain;

import java.util.List;

public class PageBean<T> {
    private int currentPage;    //  当前页号
    private int currentCount;   //  当前页显示条目数
    private int totalPage;      //  总页数
    private int totalCount;     //  总条目数
    private List<T> list;       //  封装数据
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentCount() {
        return currentCount;
    }
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
    public int getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
    @Override
    public String toString() {
        return "PageBean [currentPage=" + currentPage + ", currentCount=" + currentCount + ", totalPage=" + totalPage
                + ", totalCount=" + totalCount + ", list=" + list + "]";
    }
    

}
