package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Customer;
import com.andreamapp.homeward.bean.Manager;

public interface ManagerDAO {
    void insertManager(Manager manager);
    void deleteManager(Manager manager);
    void updateManager(Manager manager);
    Manager getManagerById(String manager_id);

    Manager login(String username, String password);
}
