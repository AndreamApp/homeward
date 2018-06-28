package com.andreamapp.homeward.ui.panel;


import com.andreamapp.homeward.bean.Customer;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class CustomerPanel extends ModelPanel {
    List<Customer> customers;

    @Override
    public ListTableModel getTableModel() {
        customers = MySQLManager.getInstance().dao().getAllCustomers();
        return new ListTableModel<Customer>(Constants.ColumnName.CUSTOMER, customers) {
            @Override
            public Object getValueAt(int row, int column) {
                Customer customer = customers.get(row);
                switch (column) {
                    case 0:
                        return customer.getIdNum();
                    case 1:
                        return customer.getName();
                    case 2:
                        return customer.getSexString();
                    case 3:
                        return customer.getTel();
                    case 4:
                        return customer.getCustomerTypeString();
                }
                return null;
            }
        };
    }

    @Override
    public float[] getColumnWeight(int columnCount) {
        return new float[]{2, 2, 1, 1, 1};
    }

    @Override
    public void onInsert() {
        new BaseDialog() {
            {
                for (String label : Constants.ColumnName.CUSTOMER) {
                    addFieldItem(label);
                }
            }
            @Override
            protected void onOK() {
                Customer customer = new Customer();
                customer.setIdNum(field(0));
                customer.setName(field(1));
                customer.setSex(Integer.parseInt(field(2)));
                customer.setTel(field(3));
                customer.setCustomerType(Integer.parseInt(field(4)));
                MySQLManager.getInstance().dao().insertCustomer(customer);
                refresh();
                super.onOK();
            }
        }.popup("添加用户");
    }

    @Override
    public void onDelete(int[] selectedRows) {
        if(selectedRows.length <= 0) return;
        for(int row : selectedRows){
            Customer customer = customers.get(row);
            MySQLManager.getInstance().dao().deleteCustomer(customer);
        }
        refresh();
    }

    @Override
    public void onUpdate(int selectedRow) {
        if(selectedRow < 0) return;
        Customer customer = customers.get(selectedRow);
        new BaseDialog() {
            {
                String[] labels = Constants.ColumnName.CUSTOMER;
                addFieldItem(labels[0]).setText(customer.getIdNum());
                addFieldItem(labels[1]).setText(customer.getName());
                addFieldItem(labels[2]).setText(String.valueOf(customer.getSex()));
                addFieldItem(labels[3]).setText(customer.getTel());
                addFieldItem(labels[4]).setText(String.valueOf(customer.getCustomerType()));
            }
            @Override
            protected void onOK() {
                customer.setIdNum(field(0));
                customer.setName(field(1));
                customer.setSex(Integer.parseInt(field(2)));
                customer.setTel(field(3));
                customer.setCustomerType(Integer.parseInt(field(4)));
                MySQLManager.getInstance().dao().updateCustomer(customer);
                refresh();
                super.onOK();
            }
        }.popup("修改用户");
    }

    @Override
    public void onSelect() {
    }

}