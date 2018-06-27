package com.andreamapp.homeward.bean;

public class Manager {
    private int manager_id;
    private int point_id;
    private String username;
    private String password;
    private String name;
    private int sex;
    private int manager_type;

    public static final int TYPE_SUPERUSER = 1;
    public static final int TYPE_SELLER = 2;
    public static final int SEX_FEMALE = 0;
    public static final int SEX_MALE = 1;

    public int getManagerId() {
        return manager_id;
    }

    public void setManagerId(int manager_id) {
        this.manager_id = manager_id;
    }

    public int getPointId() {
        return point_id;
    }

    public void setPointId(int point_id) {
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

    public int getSex() {
        return sex;
    }

    public String getSexString() {
        return sex == SEX_FEMALE ? "女" : "男";
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getManagerType() {
        return manager_type;
    }

    public String getManagerTypeString() {
        return manager_type == TYPE_SUPERUSER ? "管理员" : "售票员";
    }

    public void setManagerType(int manager_type) {
        this.manager_type = manager_type;
    }
}
