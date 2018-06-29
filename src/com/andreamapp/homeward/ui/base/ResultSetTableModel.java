package com.andreamapp.homeward.ui.base;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetTableModel extends DefaultTableModel{
    public ResultSetTableModel(ResultSet res, String[] columns){
        super(toArray(res, columns.length), columns);
    }

    private static Object[][] toArray(ResultSet res, int columnCnt){
        try {
            res.last();
            int r = res.getRow();
            res.beforeFirst();
            if(r >0){
                Object[][] arr = new Object[r][columnCnt];
                for(int i = 0; i < arr.length && res.next(); i++){
                    for(int j = 0; j < arr[0].length; j++){
                        arr[i][j] = res.getObject(j + 1);
                    }
                }
                return arr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
