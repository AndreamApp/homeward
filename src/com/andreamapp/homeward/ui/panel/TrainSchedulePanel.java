package com.andreamapp.homeward.ui.panel;

import com.andreamapp.homeward.bean.TrainSchedule;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.panel.base.ListTableModel;
import com.andreamapp.homeward.ui.panel.base.ModelPanel;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class TrainSchedulePanel extends ModelPanel {

    List<TrainSchedule> schedules;

    @Override
    public ListTableModel getTableModel() {
        schedules = MySQLManager.getInstance().dao().getTrainScheduleList(100, 0);
        return new ListTableModel<TrainSchedule>(Constants.ColumnName.SCHEDULE, schedules){
            @Override
            public Object getValueAt(int row, int column) {
                TrainSchedule schedule = schedules.get(row);
                switch (column){
                    case 0:
                        return schedule.getScheId();
                    case 1:
                        return schedule.getDepartTime();
                    case 2:
                        return schedule.getPresellTime();
                    case 3:
                        return schedule.getSpeed();
                    case 4:
                        return schedule.getTrain().getTrainId();
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