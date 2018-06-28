package com.andreamapp.homeward.ui.panel;

import com.andreamapp.homeward.bean.TicketPoint;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.panel.base.BaseDialog;
import com.andreamapp.homeward.ui.panel.base.ListTableModel;
import com.andreamapp.homeward.ui.panel.base.ModelPanel;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class SellPointPanel extends ModelPanel {
    List<TicketPoint> ticketPoints;

    private void fetchAll(){
        ticketPoints = MySQLManager.getInstance().dao().getAllTicketPoints();
    }

    @Override
    public ListTableModel getTableModel() {
        if(ticketPoints == null) fetchAll();
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
    public void onSearch(String key) {
        if(key.equals("")){
            fetchAll();
        }
        else{
            ticketPoints = MySQLManager.getInstance().dao().searchTicketPoints(key);
        }
        refresh();
    }

    @Override
    public void onInsert() {
        new BaseDialog() {
            {
                // "售票点ID", "售票点名称", "售票点地址", "营业时间"
                String[] columns = Constants.ColumnName.TICKET_POINT;
                addField(columns[1], "");
                addField(columns[2], "");
                addField(columns[3], "");
            }
            @Override
            protected void onOK() {
                TicketPoint point = new TicketPoint();
                point.setUsername(field(0));
                point.setAddress(field(1));
                point.setOpenTime(field(2));
                MySQLManager.getInstance().dao().insertTicketPoint(point);
                fetchAll();
                refresh();
                super.onOK();
            }
        }.popup("添加售票点");
    }

    @Override
    public void onDelete(int[] selectedRows) {
        if(selectedRows.length <= 0) return;
        for(int row : selectedRows){
            TicketPoint ticketPoint = ticketPoints.get(row);
            MySQLManager.getInstance().dao().deleteTicketPoint(ticketPoint);
        }
        for(int i = selectedRows.length - 1; i >= 0; i--){
            ticketPoints.remove(i);
        }
        refresh();
    }

    @Override
    public void onUpdate(int selectedRow) {
        if(selectedRow < 0) return;
        TicketPoint point = ticketPoints.get(selectedRow);
        new BaseDialog() {
            {
                // "售票点ID", "售票点名称", "售票点地址", "营业时间"
                String[] columns = Constants.ColumnName.TICKET_POINT;
                addLabel(columns[0], String.valueOf(point.getPointId()));
                addField(columns[1], point.getUsername());
                addField(columns[2], point.getAddress());
                addField(columns[3], point.getOpenTime());
            }
            @Override
            protected void onOK() {
                point.setUsername(field(1));
                point.setAddress(field(2));
                point.setOpenTime(field(3));
                MySQLManager.getInstance().dao().insertTicketPoint(point);
                refresh();
                super.onOK();
            }
        }.popup("修改售票点");
    }
}
