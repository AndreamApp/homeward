package com.andreamapp.homeward.ui.panel;

import com.andreamapp.homeward.bean.TrainOrder;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class TrainOrderPanel extends ModelPanel {

    List<TrainOrder> orders;

    @Override
    public ListTableModel getTableModel() {
        orders = MySQLManager.getInstance().dao().getTrainOrderList(100, 0);
        return new ListTableModel<TrainOrder>(Constants.ColumnName.ORDER, orders){
            @Override
            public Object getValueAt(int row, int column) {
                TrainOrder order = orders.get(row);
                switch (column){
                    case 0:
                        return order.getOrderId();
                    case 1:
                        return order.getTicketPoint().getPointId();
                    case 2:
                        return order.getBuyer().getIdNum();
                    case 3:
                        return order.getTrainSchedule().getScheId();
                    case 4:
                        return order.getSeat().getSeatId();
                    case 5:
                        return order.getTrain().getTrainId();
                    case 6:
                        return order.getDepartStation().getStationName();
                    case 7:
                        return order.getArriveStation().getStationName();
                    case 8:
                        return order.isStudentTicket();
                    case 9:
                        return order.getMoney();
                    case 10:
                        return order.getOrderState();
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