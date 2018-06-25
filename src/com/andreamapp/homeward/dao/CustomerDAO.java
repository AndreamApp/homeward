package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Customer;

public interface CustomerDAO {
    void insertCustomer(Customer customer);
    void deleteCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void upsertCustomer(Customer customer);
    Customer getCustomerByIdNum(String id_num);
}
