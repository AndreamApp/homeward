package com.andreamapp.homeward.bean;

public class Manager {
    private String manager_id;
    private String point_id;
    private String username;
    private String password;
    private String name;
    private Sex sex;
    private Type manager_type;

    public enum Type{
        SuperManager,
        Seller,
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManagerId(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getPointId() {
        return point_id;
    }

    public void setPointId(String point_id) {
        this.point_id = point_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Type getManagerType() {
        return manager_type;
    }

    public void setManagerType(Type manager_type) {
        this.manager_type = manager_type;
    }
}
