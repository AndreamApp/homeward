package com.andreamapp.homeward.ui.panel;


import com.andreamapp.homeward.bean.Manager;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class ManagerPanel extends ModelPanel {
    List<Manager> managers;

    @Override
    public ListTableModel getTableModel() {
        managers = MySQLManager.getInstance().dao().getAllManagers();
        return new ListTableModel<Manager>(Constants.ColumnName.MANAGER, managers) {
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Manager manager = managers.get(rowIndex);
                switch (columnIndex){
                    case 0:
                        return manager.getManagerId();
                    case 1:
                        return manager.getPointId();
                    case 2:
                        return manager.getUsername();
                    case 3:
                        return manager.getPassword();
                    case 4:
                        return manager.getName();
                    case 5:
                        return manager.getSexString();
                    case 6:
                        return manager.getManagerTypeString();
                }
                return null;
            }
        };
    }

    @Override
    public float[] getColumnWeight(int columnCount) {
        return new float[]{ 1, 1, 2, 2, 2, 1, 1 };
    }

    @Override
    public void onInsert() {
        // InsertDialog
    }

    @Override
    public void onDelete(int[] selectedRows) {
    }

    @Override
    public void onUpdate(int selectedRows) {
    }

    @Override
    public void onSelect() {
        // SelectDialog
    }
}