package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Customer;

import javax.swing.table.TableModel;
import java.util.List;

public interface CustomerDAO {
    void insertCustomer(Customer customer);
    void deleteCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void upsertCustomer(Customer customer);
    Customer getCustomerByIdNum(String id_num);
    List<Customer> getAllCustomers();
    List<Customer> searchCustomers(String key);
}
