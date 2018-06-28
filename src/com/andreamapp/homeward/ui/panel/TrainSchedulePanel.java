package com.andreamapp.homeward.ui.panel;

import com.andreamapp.homeward.bean.Train;
import com.andreamapp.homeward.bean.TrainSchedule;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.panel.base.BaseDialog;
import com.andreamapp.homeward.ui.panel.base.ListTableModel;
import com.andreamapp.homeward.ui.panel.base.ModelPanel;
import com.andreamapp.homeward.utils.Constants;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TrainSchedulePanel extends ModelPanel {

    private List<TrainSchedule> schedules;

    private void fetchAll() {
        schedules = MySQLManager.getInstance().dao().getTrainScheduleList(100, 0);
    }

    @Override
    public ListTableModel getTableModel() {
        if (schedules == null) fetchAll();
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
    public void onSearch(String key) {
        if (key.equals("")) {
            fetchAll();
        } else {
            schedules = MySQLManager.getInstance().dao().searchTrainSchedule(key);
        }
        refresh();
    }

    @Override
    public void onInsert() {
        new BaseDialog() {
            @Override
            protected void initComponents() {
                // "列车行程ID", "出发时间", "预售时间", "配速", "车次"
                String[] columns = Constants.ColumnName.SCHEDULE;
                addField(columns[1], "");
                addField(columns[2], "");
                addField(columns[3], "");
                addField(columns[4], "");
            }

            @Override
            protected void onOK() {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    TrainSchedule schedule = new TrainSchedule();
                    schedule.setDepartTime(format.parse(field(0)));
                    schedule.setPresellTime(format.parse(field(1)));
                    schedule.setSpeed(Float.parseFloat((field(2))));
                    Train train = new Train();
                    train.setTrainId(field(3));
                    schedule.setTrain(train);
                    MySQLManager.getInstance().dao().insertTrainSchedule(schedule);
                    fetchAll();
                    refresh();
                    super.onOK();
                } catch (ParseException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "日期格式错误！请按照 年-月-日 时:分:秒 的格式输入！");
                }
            }
        }.popup("添加售票点");
    }

    @Override
    public void onDelete(int[] selectedRows) {
        if (selectedRows.length <= 0) return;
        for (int row : selectedRows) {
            TrainSchedule schedule = schedules.get(row);
            MySQLManager.getInstance().dao().deleteTrainSchedule(schedule);
        }
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            schedules.remove(i);
        }
        refresh();
    }

    @Override
    public void onUpdate(int selectedRow) {
        if (selectedRow < 0) return;
        TrainSchedule schedule = schedules.get(selectedRow);
        new BaseDialog() {
            @Override
            protected void initComponents() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // "列车行程ID", "出发时间", "预售时间", "配速", "车次"
                String[] columns = Constants.ColumnName.SCHEDULE;
                addLabel(columns[0], String.valueOf(schedule.getScheId()));
                addField(columns[1], format.format(schedule.getDepartTime()));
                addField(columns[2], format.format(schedule.getPresellTime()));
                addField(columns[3], String.valueOf(schedule.getSpeed()));
                addField(columns[4], schedule.getTrain().getTrainId());
            }

            @Override
            protected void onOK() {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    schedule.setDepartTime(format.parse(field(0)));
                    schedule.setPresellTime(format.parse(field(1)));
                    schedule.setSpeed(Float.parseFloat((field(2))));
                    Train train = new Train();
                    train.setTrainId(field(3));
                    schedule.setTrain(train);
                    MySQLManager.getInstance().dao().updateTrainSchedule(schedule);
                    refresh();
                    super.onOK();
                } catch (ParseException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "日期格式错误！请按照 年-月-日 时:分:秒 的格式输入！");
                }
            }
        }.popup("修改售票点");
    }
}