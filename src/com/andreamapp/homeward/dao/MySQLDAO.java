package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDAO implements
        ManagerDAO
{
    private ResultSet query(String sql){
        return MySQLManager.getInstance().execute(sql).query();
    }

    private int[] update(String sql){
        return MySQLManager.getInstance().execute(sql).update();
    }

    private ResultSet query(String sql, Object ... params){
        return MySQLManager.getInstance().prepare(sql, params).query();
    }

    private int[] update(String sql, Object ... params){
        return MySQLManager.getInstance().prepare(sql, params).update();
    }

    @Override
    public void insertManager(Manager manager) {
        update("insert into manager " +
                        "(point_id, username, password, name, sex, manager_type) values " +
                        "(?, ?, ?, ?, ?, ?)",
                manager.getPointId(),
                manager.getUsername(),
                manager.getPassword(),
                manager.getName(),
                manager.getSex(),
                manager.getManagerType()
        );
    }

    @Override
    public void deleteManager(Manager manager) {
        update("delete from manager where manager_id = ?", manager.getManagerId());
    }

    @Override
    public void updateManager(Manager manager) {
        update("update manager set point_id = ?, username = ?, name = ?, sex = ?, manager_type = ?" +
                " where manager_id = ?",
                manager.getPointId(), manager.getUsername(), manager.getName(), manager.getSex(), manager.getManagerType(),
                manager.getManagerId());
    }

    private Manager getManagerFromResult(ResultSet res) throws SQLException {
        Manager manager = new Manager();
        manager.setManagerId(res.getInt("manager_id"));
        manager.setManagerType(res.getInt("manager_type"));
        manager.setName(res.getString("name"));
        manager.setPassword(res.getString("password"));
        manager.setPointId(res.getInt("point_id"));
        manager.setSex(res.getInt("sex"));
        manager.setUsername(res.getString("username"));
        return manager;
    }

    @Override
    public Manager getManagerById(String manager_id) {
        Manager manager = null;
        try {
            try (ResultSet res = query("select * from manager where manager_id = ?", manager_id)) {
                if (res.next()) {
                    manager = getManagerFromResult(res);
                }
            }
            return manager;
        } catch (SQLException e) {
            e.printStackTrace();
            return manager;
        }
    }

    public Manager getManagerByUsername(String username) {
        Manager manager = null;
        try {
            try (ResultSet res = query("select * from manager where username = ?", username)) {
                if (res.next()) {
                    manager = getManagerFromResult(res);
                }
            }
            return manager;
        } catch (SQLException e) {
            e.printStackTrace();
            return manager;
        }
    }

    @Override
    public Manager login(String username, String password) {
        Manager manager = getManagerByUsername(username);
        if(manager != null && manager.getPassword().equals(password)){
            return manager;
        }
        return null;
    }
}
