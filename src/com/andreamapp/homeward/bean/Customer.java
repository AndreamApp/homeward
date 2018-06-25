package com.andreamapp.homeward.bean;

public class Customer {
    private String name;
    private Sex sex;
    private String id_num;
    private String tel;
    private Type customer_type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getIdNum() {
        return id_num;
    }

    public void setIdNum(String id_num) {
        this.id_num = id_num;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Type getCustomerType() {
        return customer_type;
    }

    public void setCustomerType(Type customer_type) {
        this.customer_type = customer_type;
    }

    public enum Type{
        Normal,
        Student,
    }
}
