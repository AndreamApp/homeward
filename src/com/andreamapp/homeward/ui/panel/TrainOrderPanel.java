package com.andreamapp.homeward.ui.panel;

import com.andreamapp.homeward.bean.Customer;
import com.andreamapp.homeward.bean.TicketPoint;
import com.andreamapp.homeward.bean.TrainOrder;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.panel.base.BaseDialog;
import com.andreamapp.homeward.ui.panel.base.ListTableModel;
import com.andreamapp.homeward.ui.panel.base.ModelPanel;
import com.andreamapp.homeward.utils.Constants;

import java.util.List;

public class TrainOrderPanel extends ModelPanel {

    private List<TrainOrder> orders;

    private void fetchAll() {
        orders = MySQLManager.getInstance().dao().getTrainOrderList(100, 0);
    }

    @Override
    public ListTableModel getTableModel() {
        if (orders == null) fetchAll();
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
    public void onSearch(String key) {
        if (key.equals("")) {
            fetchAll();
        } else {
            orders = MySQLManager.getInstance().dao().searchTrainOrders(key);
        }
        refresh();
    }

    @Override
    public void onInsert() {
        new BaseDialog() {
            @Override
            protected void initComponents() {
                // "售票点ID", "用户身份证", "列车行程ID", "车次", "车厢", "座位号", "座位类型", "起始站", "到达站", "学生票", "金额", "订单状态"
                String[] columns = Constants.ColumnName.ORDER_INSERT;
                addNumberField(columns[0], null); // 售票点ID
                addField(columns[1], ""); // 用户身份证
                addNumberField(columns[2], null); // 列车行程ID
                addField(columns[3], ""); // 车次
                addNumberField(columns[4], null); // 车厢
                addNumberField(columns[5], null); // 座位号
                addComboBox(columns[6], "硬座", "硬卧", "软卧", "无座"); // 座位类型
                addField(columns[7], ""); // 起始站
                addField(columns[8], ""); // 到达站
                addComboBox(columns[9], "普通票", "学生票"); // 学生票
                addFloatField(columns[10], 0.0f); // 金额
                addLabel(columns[11], "预订"); // 订单状态
            }

            @Override
            protected void onOK() {
                TrainOrder order = new TrainOrder();
                fetchAll();
                refresh();
                super.onOK();
            }
        }.popup("添加售票点");
    }

    @Override
    public void onDelete(int[] selectedRows) {
        if (selectedRows.length <= 0) return;
        for (int row : selectedRows) {
            TrainOrder order = orders.get(row);
            MySQLManager.getInstance().dao().deleteTrainOrder(order);
        }
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            orders.remove(i);
        }
        refresh();
    }

    @Override
    public void onUpdate(int selectedRow) {
        if (selectedRow < 0) return;
        TrainOrder order = orders.get(selectedRow);
        new BaseDialog() {
            @Override
            protected void initComponents() {
                // "售票点ID", "用户身份证", "列车行程ID", "车次", "车厢", "座位号", "座位类型", "起始站", "到达站", "学生票", "金额", "订单状态"
                String[] columns = Constants.ColumnName.ORDER_INSERT;
                addLabel("订单ID", String.valueOf(order.getOrderId())); // 售票点ID
                addNumberField(columns[0], order.getTicketPoint().getPointId()); // 售票点ID
                addField(columns[1], order.getBuyer().getIdNum()); // 用户身份证
                addNumberField(columns[2], order.getTrainSchedule().getScheId()); // 列车行程ID
                addField(columns[3], order.getTrain().getTrainId()); // 车次
                addNumberField(columns[4], order.getSeat().getCarriageNum()); // 车厢
                addNumberField(columns[5], order.getSeat().getSeatNum()); // 座位号
                addComboBox(columns[6], "硬座", "硬卧", "软卧", "无座").setSelectedItem(order.getSeat().getSeatType()); // 座位类型
                addField(columns[7], order.getDepartStation().getStationName()); // 起始站
                addField(columns[8], order.getArriveStation().getStationName()); // 到达站
                addComboBox(columns[9], "普通票", "学生票").setSelectedIndex(order.isStudentTicket() ? 1 : 0); // 学生票
                addFloatField(columns[10], order.getMoney()); // 金额
                addLabel(columns[11], order.getOrderStateString()); // 订单状态
            }

            @Override
            protected void onOK() {
                // point
                TicketPoint point = new TicketPoint();
                point.setPointId(fieldInt(1));
                order.setTicketPoint(point);
                // buyer
                Customer customer = new Customer();
                customer.setIdNum(field(2));
                order.setBuyer(customer);
                MySQLManager.getInstance().dao().updateTrainOrder(order);
                refresh();
                super.onOK();
            }
        }.popup("修改售票点");
    }
}