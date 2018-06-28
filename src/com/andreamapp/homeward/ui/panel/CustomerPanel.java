package com.andreamapp.homeward.ui.panel;


import com.andreamapp.homeward.bean.Customer;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.panel.base.BaseDialog;
import com.andreamapp.homeward.ui.panel.base.ListTableModel;
import com.andreamapp.homeward.ui.panel.base.ModelPanel;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class CustomerPanel extends ModelPanel {
    List<Customer> customers;

    private void fetchAll(){
        customers = MySQLManager.getInstance().dao().getAllCustomers();
    }

    @Override
    public ListTableModel getTableModel() {
        if(customers == null) fetchAll();
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
    public void onSearch(String key) {
        if(key.equals("")){
            fetchAll();
        }
        else{
            customers = MySQLManager.getInstance().dao().searchCustomers(key);
        }
        refresh();
    }

    @Override
    public void onInsert() {
        new BaseDialog() {
            {
                String[] columns = Constants.ColumnName.CUSTOMER;
                addField(columns[0], "");
                addField(columns[1], "");
                addComboBox(columns[2], "女", "男");
                addField(columns[3], "");
                addComboBox(columns[4], "普通", "学生");
            }
            @Override
            protected void onOK() {
                Customer customer = new Customer();
                customer.setIdNum(field(0));
                customer.setName(field(1));
                customer.setSex(option(2));
                customer.setTel(field(3));
                customer.setCustomerType(option(4) + 1);
                MySQLManager.getInstance().dao().insertCustomer(customer);
                fetchAll();
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
        for(int i = selectedRows.length - 1; i >= 0; i--){
            customers.remove(i);
        }
        refresh();
    }

    @Override
    public void onUpdate(int selectedRow) {
        if(selectedRow < 0) return;
        Customer customer = customers.get(selectedRow);
        new BaseDialog() {
            {
                String[] columns = Constants.ColumnName.CUSTOMER;
                addField(columns[0], customer.getIdNum());
                addField(columns[1], customer.getName());
                addComboBox(columns[2], "女", "男").setSelectedIndex(customer.getSex());
                addField(columns[3], customer.getTel());
                addComboBox(columns[4], "普通", "学生").setSelectedIndex(customer.getCustomerType() - 1);
            }
            @Override
            protected void onOK() {
                customer.setIdNum(field(0));
                customer.setName(field(1));
                customer.setSex(option(2));
                customer.setTel(field(3));
                customer.setCustomerType(option(4) + 1);
                MySQLManager.getInstance().dao().updateCustomer(customer);
                refresh();
                super.onOK();
            }
        }.popup("修改用户");
    }
}