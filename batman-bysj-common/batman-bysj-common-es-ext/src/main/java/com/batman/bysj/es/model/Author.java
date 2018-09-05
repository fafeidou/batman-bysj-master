package com.batman.bysj.es.model;

/**
 * @author victor.qin
 * @date 2018/8/27 13:37
 */
public class Author {
    private long id;
    private String name;
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
