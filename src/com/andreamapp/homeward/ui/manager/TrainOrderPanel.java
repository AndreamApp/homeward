package com.andreamapp.homeward.ui.manager;

import com.andreamapp.homeward.bean.*;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.base.ListTableModel;
import com.andreamapp.homeward.ui.base.ModelPanel;
import com.andreamapp.homeward.ui.widget.XDialog;
import com.andreamapp.homeward.utils.Constants;
import com.andreamapp.homeward.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class TrainOrderPanel extends ModelPanel {

    protected List<TrainOrder> orders;

    private void fetchAll() {
        orders = MySQLManager.getInstance().dao().getTrainOrderList(100, 0);
    }

    @Override
    public ListTableModel getTableModel() {
        if (orders == null) fetchAll();
        // "订单ID", "用户姓名", "车次", "起始站", "到达站", "金额", "订单状态"
        return new ListTableModel<TrainOrder>(Constants.ColumnName.ORDER, orders){
            @Override
            public Object getValueAt(int row, int column) {
                TrainOrder order = orders.get(row);
                switch (column){
                    case 0:
                        return order.getOrderId();
                    case 1:
                        return order.getBuyer().getName();
                    case 2:
                        return order.getTrain().getTrainId();
                    case 3:
                        return order.getDepartStation().getStationName();
                    case 4:
                        return order.getArriveStation().getStationName();
                    case 5:
                        return order.getMoney();
                    case 6:
                        return order.getOrderStateString();
                }
                return null;
            }
        };
    }

    @Override
    public void onSearch(String key) {
        EventQueue.invokeLater(() -> {
            if (key.equals("")) {
                fetchAll();
            } else {
                orders = MySQLManager.getInstance().dao().searchTrainOrders(key);
            }
            refresh();
        });
    }


    private JComboBox carriageBox, seatNumBox, seatTypeBox;
    private JComboBox departBox, arriveBox, stuBox;
    private JLabel moneyField;
    private Train train = new Train();

    @SuppressWarnings("unchecked")
    private boolean checkTrainId(String train_id) {
        if (StringUtils.empty(train_id)) {
            return false;
        }
        // 检查时会清空选择框的状态，所以不要重复检查
        if (train != null && train_id.equals(train.getTrainId())) {
            return true;
        }
        train = MySQLManager.getInstance().dao().getTrainById(train_id);
        carriageBox.removeAllItems();
        departBox.removeAllItems();
        arriveBox.removeAllItems();
        if (train == null) {
            JOptionPane.showMessageDialog(null, train_id + "次列车不存在！");
            carriageBox.setEnabled(false);
            seatNumBox.setEnabled(false);
            departBox.setEnabled(false);
            arriveBox.setEnabled(false);
            return false;
        }
        carriageBox.setEnabled(true);
        seatNumBox.setEnabled(true);
        departBox.setEnabled(true);
        arriveBox.setEnabled(true);
        // load carriage box
        for (SeatGroup sg : train.getSeats()) {
            carriageBox.addItem(sg.getCarriageNum());
        }
        // set to default carriage and trigger listener to fill seatComboBox
        selectCarriage(1, 1);
        // load station
        for (Passby p : train.getTrainPassby()) {
            departBox.addItem(p.getDepartStation().getStationName());
            arriveBox.addItem(p.getArriveStation().getStationName());
        }
        return true;
    }

    private void reCalcMoney() {
        moneyField.setText(String.valueOf(
                train.calcMoneyBetween(
                        departBox.getSelectedIndex() + 1,
                        arriveBox.getSelectedIndex() + 2,
                        (String) seatTypeBox.getSelectedItem(),
                        stuBox.getSelectedIndex() == 1)
        ));
    }

    private void addListener() {
        carriageBox.addItemListener(e -> {
            if (ItemEvent.SELECTED == e.getStateChange()) {
                seatNumBox.removeAllItems();
                int carriageNum = (int) e.getItem();
                selectCarriage(carriageNum, 1);
            }
        });
        ItemListener reCalcListener = e -> {
            if (ItemEvent.SELECTED == e.getStateChange()) {
                reCalcMoney();
            }
        };
        departBox.addItemListener(reCalcListener);
        arriveBox.addItemListener(reCalcListener);
        stuBox.addItemListener(reCalcListener);
    }

    @SuppressWarnings("unchecked")
    private void selectCarriage(int carriageNum, int seatNum) {
        carriageBox.setSelectedItem(carriageNum);
        // fill seat combo box
        seatNumBox.removeAllItems();
        for (SeatGroup sg : train.getSeats()) {
            if (sg.getCarriageNum() == carriageNum) {
                // set seat num in this carriage
                int seatCnt = sg.getSeatNum();
                for (int i = 1; i <= seatCnt; i++) {
                    seatNumBox.addItem(i);
                }
                // set seat type in this carriage
                seatTypeBox.setSelectedItem(sg.getSeatType());
                break;
            }
        }
        seatNumBox.setSelectedItem(seatNum);
        reCalcMoney();
    }

    @Override
    public void onInsert() {
        new XDialog() {

            @Override
            protected void initComponents() {
                // "售票点ID", "用户身份证", "列车行程ID", "车次", "车厢", "座位号", "座位类型", "起始站", "到达站", "学生票", "金额", "订单状态"
                String[] columns = Constants.ColumnName.ORDER_INSERT;
                addNumberField(columns[0], null); // 售票点ID
                addField(columns[1], ""); // 用户身份证
                addNumberField(columns[2], null); // 列车行程ID
                addField(columns[3], "").addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        checkTrainId(field(3));
                    }
                }); // 车次
                (carriageBox = addComboBox(columns[4])).setEnabled(false); // 车厢
                (seatNumBox = addComboBox(columns[5])).setEnabled(false); // 座位号
                (seatTypeBox = addComboBox(columns[6], "硬座", "硬卧", "软卧", "无座")).setEnabled(false); // 座位类型
                (departBox = addComboBox(columns[7])).setEnabled(false); // 起始站
                (arriveBox = addComboBox(columns[8])).setEnabled(false); // 到达站
                stuBox = addComboBox(columns[9], "普通票", "学生票"); // 学生票
                moneyField = addLabel(columns[10], String.valueOf(0.0f)); // 金额
                addLabel(columns[11], "预订"); // 订单状态
                addListener();
                train = null; // reset train avoid use old dialog data
            }

            @Override
            protected void onOK() {
                EventQueue.invokeLater(() -> {
                    TrainOrder order = new TrainOrder();
                    TicketPoint point = MySQLManager.getInstance().dao().getTicketPointById(fieldInt(0));
                    if (point == null) {
                        JOptionPane.showMessageDialog(null, "该售票点不存在！");
                        return;
                    }
                    order.setTicketPoint(point);
                    Customer buyer = MySQLManager.getInstance().dao().getCustomerByIdNum(field(1));
                    if (buyer == null) {
                        JOptionPane.showMessageDialog(null, "该用户不存在！");
                        return;
                    }
                    order.setBuyer(buyer);
                    checkTrainId(field(3));
                    order.setTrain(train);
                    TrainSchedule schedule = MySQLManager.getInstance().dao().getTrainScheduleById(fieldInt(2));
                    if (schedule == null) {
                        JOptionPane.showMessageDialog(null, "该次列车行程不存在！");
                        return;
                    }
                    order.setTrainSchedule(schedule);
                    Seat seat = MySQLManager.getInstance().dao().getSeat(train.getTrainId(), (int) optionValue(4), (int) optionValue(5));
                    seat.setTrain(train);
                    order.setSeat(seat);
                    if (fieldFloat(10) < 0) {
                        JOptionPane.showMessageDialog(null, "！");
                        return;
                    }
                    order.setDepartStation(MySQLManager.getInstance().dao().getStationByName((String) optionValue(7)));
                    order.setArriveStation(MySQLManager.getInstance().dao().getStationByName((String) optionValue(8)));
                    order.setDepartStationRrder(option(7) + 1);
                    order.setArriveStationOrder(option(8) + 2);
                    order.setStudentTicket(option(9) == 1);
                    order.setMoney(Float.parseFloat(field(10)));
                    order.setOrderState(TrainOrder.STATE_RESERVED);
                    MySQLManager.getInstance().dao().insertTrainOrder(order);
                    fetchAll();
                    refresh();
                    super.onOK();
                });
            }
        }.popup("添加订单");
    }

    @Override
    public void onDelete(int[] selectedRows) {
        if (selectedRows.length <= 0) return;
        EventQueue.invokeLater(() -> {
            for (int row : selectedRows) {
                TrainOrder order = orders.get(row);
                MySQLManager.getInstance().dao().deleteTrainOrder(order);
            }
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                orders.remove(i);
            }
            refresh();
        });
    }

    @Override
    public void onUpdate(int selectedRow) {
        if (selectedRow < 0) return;
        TrainOrder order = orders.get(selectedRow);
        new XDialog() {
            @Override
            protected void initComponents() {
                // "售票点ID", "用户身份证", "列车行程ID", "车次", "车厢", "座位号", "座位类型", "起始站", "到达站", "学生票", "金额", "订单状态"
                String[] columns = Constants.ColumnName.ORDER_INSERT;
//                addLabel("订单ID", String.valueOf(order.getOrderId())); // 售票点ID

                addNumberField(columns[0], order.getTicketPoint().getPointId()); // 售票点ID
                addField(columns[1], order.getBuyer().getIdNum()); // 用户身份证
                addNumberField(columns[2], order.getTrainSchedule().getScheId()); // 列车行程ID
                addField(columns[3], order.getTrain().getTrainId()).addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        checkTrainId(field(3));
                    }
                }); // 车次

                (carriageBox = addComboBox(columns[4])).setEnabled(false); // 车厢
                (seatNumBox = addComboBox(columns[5])).setEnabled(false); // 座位号
                (seatTypeBox = addComboBox(columns[6], "硬座", "硬卧", "软卧", "无座")).setEnabled(false); // 座位类型
                (departBox = addComboBox(columns[7])).setEnabled(false); // 起始站
                (arriveBox = addComboBox(columns[8])).setEnabled(false); // 到达站
                stuBox = addComboBox(columns[9], "普通票", "学生票"); // 学生票
                moneyField = addLabel(columns[10], String.valueOf(order.getMoney())); // 金额
                addLabel(columns[11], order.getOrderStateString()); // 订单状态

                addListener();
                train = null; // reset train avoid use old dialog data
                checkTrainId(order.getTrain().getTrainId());
                departBox.setSelectedItem(order.getDepartStation().getStationName());
                arriveBox.setSelectedItem(order.getArriveStation().getStationName());
                stuBox.setSelectedIndex(order.isStudentTicket() ? 1 : 0);
                order.setSeat(MySQLManager.getInstance().dao().getSeatById(order.getSeat().getSeatId()));
                selectCarriage(order.getSeat().getCarriageNum(), order.getSeat().getSeatNum());
            }

            @Override
            protected void onOK() {
                EventQueue.invokeLater(() -> {
                    TicketPoint point = MySQLManager.getInstance().dao().getTicketPointById(fieldInt(0));
                    if (point == null) {
                        JOptionPane.showMessageDialog(null, "该售票点不存在！");
                        return;
                    }
                    order.setTicketPoint(point);
                    Customer buyer = MySQLManager.getInstance().dao().getCustomerByIdNum(field(1));
                    if (buyer == null) {
                        JOptionPane.showMessageDialog(null, "该用户不存在！");
                        return;
                    }
                    order.setBuyer(buyer);
                    checkTrainId(field(3));
                    order.setTrain(train);
                    TrainSchedule schedule = MySQLManager.getInstance().dao().getTrainScheduleById(fieldInt(2));
                    if (schedule == null) {
                        JOptionPane.showMessageDialog(null, "该次列车行程不存在！");
                        return;
                    }
                    order.setTrainSchedule(schedule);
                    Seat seat = MySQLManager.getInstance().dao().getSeat(train.getTrainId(), (int) optionValue(4), (int) optionValue(5));
                    seat.setTrain(train);
                    order.setSeat(seat);
                    if (fieldFloat(10) < 0) {
                        JOptionPane.showMessageDialog(null, "！");
                        return;
                    }
                    order.setDepartStation(MySQLManager.getInstance().dao().getStationByName((String) optionValue(7)));
                    order.setArriveStation(MySQLManager.getInstance().dao().getStationByName((String) optionValue(8)));
                    order.setDepartStationRrder(option(7) + 1);
                    order.setArriveStationOrder(option(8) + 2);
                    order.setStudentTicket(option(9) == 1);
                    order.setMoney(Float.parseFloat(field(10)));
                    order.setOrderState(TrainOrder.STATE_RESERVED);
                    MySQLManager.getInstance().dao().updateTrainOrder(order);
                    refresh();
                    super.onOK();
                });
            }
        }.popup("修改订单");
    }
}