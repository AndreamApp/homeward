package com.andreamapp.homeward.ui.manager;


import com.andreamapp.homeward.bean.Passby;
import com.andreamapp.homeward.bean.SeatGroup;
import com.andreamapp.homeward.bean.Station;
import com.andreamapp.homeward.bean.Train;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.base.ListTableModel;
import com.andreamapp.homeward.ui.base.ModelPanel;
import com.andreamapp.homeward.ui.widget.XDialog;
import com.andreamapp.homeward.utils.Constants;
import com.andreamapp.homeward.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TrainPanel extends ModelPanel {

    private List<Train> trains;

    private void fetchAll(){
        trains = MySQLManager.getInstance().dao().getAllTrains();
    }

    @Override
    public ListTableModel getTableModel() {
        if(trains == null) fetchAll();
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

    private Station getStationByName(String name){
        return MySQLManager.getInstance().dao().getStationByName(name);
    }

    private JTable passbyTable;
    private String[] passbyNames = new String[]{ "起点站", "终点站", "距离", "停留时间" };
    private JTable seatTable;
    private String[] seatNames = new String[]{ "车厢号", "座位数", "座位类型" };

    private void loadTrain(Train train, String train_id, String train_type){
        train.setTrainId(train_id);
        train.setTrainType(train_type);
        // insert train first so the foreign key in Station and Seat can be inserted
        MySQLManager.getInstance().dao().insertTrain(train);
        // load passby info
        TableModel model = passbyTable.getModel();
        List<Passby> passby = new ArrayList<>();
        for(int i = 0; i < 100 && !StringUtils.empty(model.getValueAt(i, 0)); i++){
            Passby p = new Passby();
            p.setTrain(train);
            p.setDepartStation(getStationByName((String) model.getValueAt(i, 0)));
            p.setArriveStation(getStationByName((String) model.getValueAt(i, 1)));
            p.setDistance(Float.parseFloat(String.valueOf(model.getValueAt(i, 2))));
            p.setStayTime(Integer.parseInt(String.valueOf(model.getValueAt(i, 3))));
            p.setStationOrder(i + 1);
            passby.add(p);
        }
        // load seat info
        model = seatTable.getModel();
        train.setTrainPassby(passby);
        List<SeatGroup> seats = new ArrayList<>();
        for(int i = 0; i < 100 && !StringUtils.empty(model.getValueAt(i, 0)); i++){
            SeatGroup seat = new SeatGroup();
            seat.setTrain(train);
            seat.setCarriageNum(Integer.parseInt(String.valueOf(model.getValueAt(i, 0))));
            seat.setSeatNum(Integer.parseInt(String.valueOf(model.getValueAt(i, 1))));
            seat.setSeatType(String.valueOf(model.getValueAt(i, 2)));
            seats.add(seat);
        }
        train.setSeats(seats);
    }

    @Override
    public void onSearch(String key) {
        EventQueue.invokeLater(() -> {
            trains = MySQLManager.getInstance().dao().searchTrains(key);
            refresh();
        });
    }

    @Override
    public void onInsert() {
        new XDialog() {
            @Override
            protected void initComponents(){
                // "车次", "列车类型", "途经站点"
                String[] columns = Constants.ColumnName.TRAIN;
                addField(columns[0], "");
                addField(columns[1], "");
                passbyTable = addTable(columns[2]);
                passbyTable.setModel(new DefaultTableModel(new Object[100][4], passbyNames));
                seatTable = addTable("座位信息");
                seatTable.setModel(new DefaultTableModel(new Object[100][4], seatNames));
            }
            @Override
            protected void onOK() {
                Train train = new Train();
                loadTrain(train, field(0), field(1));
                EventQueue.invokeLater(() -> {
                    // update train info
                    MySQLManager.getInstance().dao().updateTrain(train);
                    fetchAll();
                    refresh();
                    super.onOK();
                });
            }
        }.popup("添加列车");
    }

    @Override
    public void onDelete(int[] selectedRows) {
        if(selectedRows.length <= 0) return;
        EventQueue.invokeLater(() -> {
            for (int row : selectedRows) {
                Train train = trains.get(row);
                MySQLManager.getInstance().dao().deleteTrain(train);
            }
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                trains.remove(i);
            }
            refresh();
        });
    }

    @Override
    public void onUpdate(int selectedRow) {
        if(selectedRow < 0) return;
        Train train = trains.get(selectedRow);
        new XDialog() {
            @Override
            protected void initComponents(){
                // "车次", "列车类型", "途经站点"
                String[] columns = Constants.ColumnName.TRAIN;
                addField(columns[0], train.getTrainId());
                addField(columns[1], train.getTrainType());
                passbyTable = addTable(columns[2]);
                passbyTable.setModel(new DefaultTableModel(passbyNames, train.getTrainPassby().size() + 20) {
                    @Override
                    public Object getValueAt(int row, int column) {
                        if(row < 0 || row >= train.getTrainPassby().size()){
                            return null;
                        }
                        Passby p = train.getTrainPassby().get(row);
                        switch (column){
                            case 0: return p.getDepartStation().getStationName();
                            case 1: return p.getArriveStation().getStationName();
                            case 2: return p.getDistance();
                            case 3: return p.getStayTime();
                        }
                        return null;
                    }
                });
                seatTable = addTable("座位信息");
                seatTable.setModel(new DefaultTableModel(seatNames, train.getSeats().size() + 20) {
                    @Override
                    public Object getValueAt(int row, int column) {
                        if(row < 0 || row >= train.getSeats().size()){
                            return null;
                        }
                        SeatGroup sg = train.getSeats().get(row);
                        switch (column){
                            case 0: return sg.getCarriageNum();
                            case 1: return sg.getSeatNum();
                            case 2: return sg.getSeatType();
                        }
                        return null;
                    }
                });
            }
            @Override
            protected void onOK() {
                loadTrain(train, field(0), field(1));
                EventQueue.invokeLater(() -> {
                    // update train info
                    MySQLManager.getInstance().dao().updateTrain(train);
                    fetchAll();
                    refresh();
                    super.onOK();
                });
            }
        }.popup("修改列车信息");
    }
}