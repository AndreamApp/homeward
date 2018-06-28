package com.andreamapp.homeward.ui.panel;

import com.andreamapp.homeward.bean.TicketPoint;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class SellPointPanel extends ModelPanel {
    List<TicketPoint> ticketPoints;

    @Override
    public ListTableModel getTableModel() {
        ticketPoints = MySQLManager.getInstance().dao().getAllTicketPoints();
        return new ListTableModel<TicketPoint>(Constants.ColumnName.TICKET_POINT, ticketPoints){
            @Override
            public Object getValueAt(int row, int column) {
                TicketPoint point = ticketPoints.get(row);
                switch (column){
                    case 0:
                        return point.getPointId();
                    case 1:
                        return point.getUsername();
                    case 2:
                        return point.getAddress();
                    case 3:
                        return point.getOpenTime();
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
    public void onSelect() {
        // SelectDialog
    }
}
