package com.andreamapp.homeward.ui.panel;


import com.andreamapp.homeward.bean.Train;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class TrainPanel extends ModelPanel {

    List<Train> trains;

    @Override
    public ListTableModel getTableModel() {
        trains = MySQLManager.getInstance().dao().getAllTrains();
        return new ListTableModel<Train>(Constants.ColumnName.TRAIN, trains){
            @Override
            public Object getValueAt(int row, int column) {
                Train train = trains.get(row);
                switch (column){
                    case 0:
                        return train.getTrainId();
                    case 1:
                        return train.getTrainType();
                    case 2:
                        return train.getTrainPassbyString();
                }
                return null;
            }
        };
    }

    @Override
    public void onInsert() {
        // InsertDialog
    }

    @Override
    public void onDelete(int[] selectedRows) {
    }

    @Override
    public void onUpdate(int selectedRow) {
    }

    @Override
    public void onSearch(String key) {
    }
}