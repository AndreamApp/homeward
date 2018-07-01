package com.andreamapp.homeward.ui.seller;

import com.andreamapp.homeward.bean.*;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.widget.MeasurablePanel;
import com.andreamapp.homeward.ui.widget.XDialog;
import com.andreamapp.homeward.ui.widget.XTextField;
import com.andreamapp.homeward.utils.Constants;
import com.andreamapp.homeward.utils.StringUtils;
import org.jdesktop.swingx.JXImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class OrderPanel extends JPanel {
    private JTextField departStationText;
    private JTextField arriveStationText;
    private JTextField departDateText;
    private JCheckBox stuBox;
    private JButton queryBtn;
    private List<ScheduleItem> scheduleItems = new ArrayList<>(); // view

    private List<TrainSchedule> schedules = new ArrayList<>(); // model

    private int leftMargin = 12;
    private int topMargin = 12;
    private int labelWidth = 100;
    private int labelHeight = 30;
    private int fieldWidth = 200;
    private int fieldHeight = 30;
    private int padding = 10;
    private int btnWidth = 100, btnHeight = 30;

    private int panelBaseHeight = 200;

    public OrderPanel() {
        initComponents();
        departStationText.setText("北京");
        arriveStationText.setText("上海");
        departDateText.setText("2018-07-20");
    }

    private void initComponents() {
        setLayout(null);
        add(departStationText = new XTextField("起点站"));
        add(arriveStationText = new XTextField("终点站"));
        add(departDateText = new XTextField("出发日期 yyyy-mm-dd"));
        add(stuBox = new JCheckBox("学生票"));
        add(queryBtn = new JButton("查询"));
        queryBtn.addActionListener(e -> onQueryClicked());

        initLayout();
    }

    private void initLayout() {
        int x = leftMargin, y = topMargin;
        x += padding;
        y += padding;
        departStationText.setBounds(x, y, fieldWidth, fieldHeight);
        x += padding + fieldWidth + padding;
        arriveStationText.setBounds(x, y, fieldWidth, fieldHeight);
        x += padding + fieldWidth + padding;
        departDateText.setBounds(x, y, fieldWidth, fieldHeight);
        x += padding + fieldWidth + padding;
        stuBox.setBounds(x, y, btnWidth, btnHeight);
        x += padding + btnWidth + padding;
        queryBtn.setBounds(x, y, btnWidth, btnHeight);
        y += fieldHeight + padding;
        panelBaseHeight = y;
    }

    private void relayout() {
        int x = leftMargin + padding, y = panelBaseHeight + padding;
        for (ScheduleItem item : scheduleItems) {
            add(item);
            item.setBounds(x, y, item.itemWidth, item.itemHeight);
            y += padding + padding;
        }
    }

    public void onQueryClicked() {
        String departStation = departStationText.getText().trim();
        String arriveStation = arriveStationText.getText().trim();
        Date departDate;
        boolean isStudent = stuBox.isSelected();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            departDate = format.parse(departDateText.getText().trim());
        } catch (ParseException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "日期格式不对！请按照 年-月-日 的格式输入！");
            return;
        }
        if (StringUtils.empty(departStation)) {
            JOptionPane.showMessageDialog(null, "请输入起点站！");
        } else if (StringUtils.empty(arriveStation)) {
            JOptionPane.showMessageDialog(null, "请输入终点站！");
        } else {
            onQuery(departStation, arriveStation, departDate, isStudent);
        }
    }

    private void onQuery(String departStation, String arriveStation, Date departDate, boolean isStudent) {
        schedules = MySQLManager.getInstance().dao()
                .searchTrainSchedule(departStation, arriveStation, departDate, isStudent);
        refresh(departStation, arriveStation, isStudent);
    }

    private void refresh(String departStation, String arriveStation, boolean isStudent) {
        for (ScheduleItem item : scheduleItems) {
            remove(item);
        }
        scheduleItems.clear();
        for (TrainSchedule schedule : schedules) {
            scheduleItems.add(new ScheduleItem(schedule, departStation, arriveStation, isStudent));
        }
        relayout();
    }

    /**
     * 显示列车起始时间、起始站点、经过时间、车次名称
     */
    private class TrainInfoItem extends JPanel implements MeasurablePanel {
        private TrainSchedule schedule;
        private Train train;
        private TrainSchedule.Extra extra;

        private JLabel departTime, departStation, arriveTime, arriveStation;
        private JLabel trainId, passTime;
        int itemWidth = 900, itemHeight = 200;

        public TrainInfoItem(TrainSchedule schedule, TrainSchedule.Extra extra) {
            this.schedule = schedule;
            this.train = schedule.getTrain();
            this.extra = extra;
            initComponents();
        }

        private void initValues() {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
            departTime.setText(format.format(extra.departTime));
            arriveTime.setText(format.format(extra.arriveTime));
            departStation.setText(extra.departStationName);
            arriveStation.setText(extra.arriveStationName);
            trainId.setText(extra.trainId);
            passTime.setText(extra.passTime);
        }

        private void initComponents() {
            initialize();
            initAlignment();
            initLayout();
            initValues();
        }

        private void initialize() {
            add(departTime = new JLabel());
            add(arriveTime = new JLabel());
            add(departStation = new JLabel());
            add(arriveStation = new JLabel());
            add(trainId = new JLabel());
            add(passTime = new JLabel());
        }

        private void initBtn(JLabel btn, Font font, int horizontalAlign, int verticalAlign) {
            btn.setFont(font);
            btn.setHorizontalAlignment(horizontalAlign);
            btn.setVerticalAlignment(verticalAlign);
        }

        private void initBtn(JButton btn, Font font, int horizontalAlign, int verticalAlign) {
            btn.setFont(font);
            btn.setHorizontalAlignment(horizontalAlign);
            btn.setVerticalAlignment(verticalAlign);
        }

        private void initAlignment() {
            Font boldFont = new Font("黑体", Font.BOLD, 24);
            Font bigFont = new Font("宋体", Font.PLAIN, 24);
            Font midFont = new Font("宋体", Font.PLAIN, 16);
            Font smallFont = new Font("宋体", Font.PLAIN, 14);

            initBtn(departTime, boldFont, SwingConstants.CENTER, SwingConstants.BOTTOM);
            initBtn(departStation, bigFont, SwingConstants.CENTER, SwingConstants.TOP);
            initBtn(arriveTime, boldFont, SwingConstants.CENTER, SwingConstants.BOTTOM);
            initBtn(arriveStation, bigFont, SwingConstants.CENTER, SwingConstants.TOP);

            initBtn(trainId, smallFont, SwingConstants.CENTER, SwingConstants.BOTTOM);
            initBtn(passTime, smallFont, SwingConstants.CENTER, SwingConstants.TOP);
        }

        private void initLayout() {
            setLayout(null);
            int x = 0, y = 0;
            x += padding;
            y += padding;
            departTime.setBounds(x, y, 100, 50);
            y += padding + 50 + padding;
            departStation.setBounds(x, y, 100, 50);
            x += 100 + 50;
            y = padding + 20;
            trainId.setBounds(x, y, 160, 30);
            y += padding + 30 + padding;
            passTime.setBounds(x, y, 160, 30);
            x += 160 + 50;
            y = padding;
            arriveTime.setBounds(x, y, 100, 50);
            y += padding + 50 + padding;
            arriveStation.setBounds(x, y, 100, 50);
            x += 100 + 50;
            y += padding + 40;
            itemWidth = x;
            itemHeight = y;
        }

        @Override
        public int width() {
            return itemWidth;
        }

        @Override
        public int height() {
            return itemHeight;
        }
    }

    /**
     * 显示列车信息和预订信息
     */
    private class ScheduleItem extends JPanel implements MeasurablePanel {
        private TrainSchedule schedule;
        private Train train;
        private TrainSchedule.Extra extra;
        private String departStationNameInQuery, arriveStationNameInQuery;
        private boolean isStudent;

        private TrainInfoItem trainInfoItem;
        private JLabel hardSeat, hardBerth, softBerth, noSeat;
        private JButton orderHardSeat, orderHardBerth, orderSoftBerth, orderNoSeat;
        int itemWidth = 900, itemHeight = 200;

        public ScheduleItem(TrainSchedule schedule, String departStation, String arriveStation, boolean isStudent) {
            this.schedule = schedule;
            this.train = schedule.getTrain();
            this.departStationNameInQuery = departStation;
            this.arriveStationNameInQuery = arriveStation;
            this.isStudent = isStudent;
            this.extra = schedule.getExtra(departStation, arriveStation, isStudent);
            this.trainInfoItem = new TrainInfoItem(schedule, extra);
            initComponents();
        }

        private void initValues() {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
            hardSeat.setText("硬座  " + extra.hardSeatNum + "张  " + extra.hardSeatMoney + "元");
            hardBerth.setText("硬卧  " + extra.hardBerthNum + "张  " + extra.hardBerthMoney + "元");
            softBerth.setText("软卧  " + extra.softBerthNum + "张  " + extra.softBerthMoney + "元");
            noSeat.setText("无座  " + extra.noSeatNum + "张  " + extra.noSeatMoney + "元");
            orderHardSeat.setEnabled(extra.hardSeatNum > 0);
            orderHardBerth.setEnabled(extra.hardBerthNum > 0);
            orderSoftBerth.setEnabled(extra.softBerthNum > 0);
            orderNoSeat.setEnabled(extra.noSeatNum > 0);
        }

        private void initComponents() {
            initialize();
            initAlignment();
            initLayout();
            initValues();
        }

        private void initialize() {
            add(trainInfoItem);
            add(hardSeat = new JLabel());
            add(hardBerth = new JLabel());
            add(softBerth = new JLabel());
            add(noSeat = new JLabel());
            add(orderHardSeat = new JButton("预订"));
            add(orderHardBerth = new JButton("预订"));
            add(orderSoftBerth = new JButton("预订"));
            add(orderNoSeat = new JButton("预订"));
            orderHardSeat.addActionListener(e -> new OrderDialog("硬座").popup("预订硬座票"));
            orderHardBerth.addActionListener(e -> new OrderDialog("硬卧").popup("预订硬卧票"));
            orderSoftBerth.addActionListener(e -> new OrderDialog("软卧").popup("预订软卧票"));
            orderNoSeat.addActionListener(e -> new OrderDialog("无座").popup("预订无座票"));
        }

        private void initBtn(JLabel btn, Font font, int horizontalAlign, int verticalAlign) {
            btn.setFont(font);
            btn.setHorizontalAlignment(horizontalAlign);
            btn.setVerticalAlignment(verticalAlign);
        }

        private void initBtn(JButton btn, Font font, int horizontalAlign, int verticalAlign) {
            btn.setFont(font);
            btn.setHorizontalAlignment(horizontalAlign);
            btn.setVerticalAlignment(verticalAlign);
        }

        private void initAlignment() {
            Font boldFont = new Font("黑体", Font.BOLD, 24);
            Font bigFont = new Font("宋体", Font.PLAIN, 24);
            Font midFont = new Font("宋体", Font.PLAIN, 16);
            Font smallFont = new Font("宋体", Font.PLAIN, 14);

            initBtn(hardSeat, midFont, SwingConstants.CENTER, SwingConstants.CENTER);
            initBtn(hardBerth, midFont, SwingConstants.CENTER, SwingConstants.CENTER);
            initBtn(softBerth, midFont, SwingConstants.CENTER, SwingConstants.CENTER);
            initBtn(noSeat, midFont, SwingConstants.CENTER, SwingConstants.CENTER);

            initBtn(orderHardSeat, midFont, SwingConstants.CENTER, SwingConstants.CENTER);
            initBtn(orderHardBerth, midFont, SwingConstants.CENTER, SwingConstants.CENTER);
            initBtn(orderSoftBerth, midFont, SwingConstants.CENTER, SwingConstants.CENTER);
            initBtn(orderNoSeat, midFont, SwingConstants.CENTER, SwingConstants.CENTER);
        }

        private void initLayout() {
            setLayout(null);
            int x = leftMargin, y = topMargin;
            x += padding;
            y += padding;
            trainInfoItem.setBounds(x, y, trainInfoItem.itemWidth, trainInfoItem.itemHeight);
            x += trainInfoItem.itemWidth + 50;
            y = leftMargin + padding;
            hardSeat.setBounds(x, y, 200, 30);
            y += 30 + padding;
            hardBerth.setBounds(x, y, 200, 30);
            y += 30 + padding;
            softBerth.setBounds(x, y, 200, 30);
            y += 30 + padding;
            noSeat.setBounds(x, y, 200, 30);
            x += padding + 200 + padding;
            y = leftMargin + padding;
            orderHardSeat.setBounds(x, y, 80, 30);
            y += 30 + 10;
            orderHardBerth.setBounds(x, y, 80, 30);
            y += 30 + 10;
            orderSoftBerth.setBounds(x, y, 80, 30);
            y += 30 + 10;
            orderNoSeat.setBounds(x, y, 80, 30);
            x += 80 + padding + leftMargin;
            y += 30 + 10 + topMargin;
            itemWidth = x;
            itemHeight = y;
        }

        @Override
        public int width() {
            return itemWidth;
        }

        @Override
        public int height() {
            return itemHeight;
        }

        @SuppressWarnings("unchecked")
        private class OrderDialog extends XDialog {
            private JLabel dateInfo, seatInfo;
            private TrainInfoItem trainInfoItem;
            private JComboBox carriageBox, seatNumBox;
            private String seatType;
            private float seatMoney;
            private List<Seat> freeSeats;

            public OrderDialog(String seatType) {
                this.seatType = seatType;
                if ("硬座".equals(seatType)) {
                    seatMoney = extra.hardSeatMoney;
                } else if ("硬卧".equals(seatType)) {
                    seatMoney = extra.hardBerthMoney;
                } else if ("软卧".equals(seatType)) {
                    seatMoney = extra.softBerthMoney;
                } else if ("无座".equals(seatType)) {
                    seatMoney = extra.noSeatMoney;
                }
                init();
            }

            @Override
            protected void onOK() {
                String name = field(2);
                String idNum = field(3);
                String tel = field(4);
                Customer buyer = new Customer();
                buyer.setName(name);
                buyer.setIdNum(idNum);
                buyer.setCustomerType(isStudent ? 1 : 0);
                buyer.setSex(MySQLManager.getInstance().dao().getSexFromIdNum(idNum));
                buyer.setTel(tel);
                MySQLManager.getInstance().dao().upsertCustomer(buyer);

                int carriage = Integer.parseInt((String) Objects.requireNonNull(carriageBox.getSelectedItem()));
                int seatNum = (int) seatNumBox.getSelectedItem();
                Seat seat = MySQLManager.getInstance().dao().getSeat(train.getTrainId(), carriage, seatNum);

                TrainOrder order = new TrainOrder();
                TicketPoint point = new TicketPoint();
                point.setPointId(Constants.currentManager.getPointId());
                order.setTicketPoint(point);
                order.setBuyer(buyer);
                order.setTrainSchedule(schedule);
                order.setSeat(seat);
                order.setTrain(train);
                order.setDepartStation(MySQLManager.getInstance().dao().getStationByName(extra.departStationName));
                order.setArriveStation(MySQLManager.getInstance().dao().getStationByName(extra.arriveStationName));
                order.setDepartStationRrder(extra.departStationOrder);
                order.setArriveStationOrder(extra.arriveStationOrder);
                order.setStudentTicket(isStudent);
                order.setMoney(seatMoney);
                order.setOrderState(TrainOrder.STATE_RESERVED);
                MySQLManager.getInstance().dao().insertTrainOrder(order);
                PayDialog pay = new PayDialog();
                pay.popup("等待用户支付...");
                if (pay.result) {
                    order.setOrderState(TrainOrder.STATE_PAYED);
                    MySQLManager.getInstance().dao().updateTrainOrder(order);
                    JOptionPane.showMessageDialog(null, "支付成功！");
                    dispose();
                }
            }

            private String[] getCarriages() {
                List<String> carriageList = new ArrayList<>();
                for (Seat s : freeSeats) {
                    String num = String.valueOf(s.getCarriageNum());
                    if (s.getSeatType().equals(seatType) && !carriageList.contains(num)) {
                        carriageList.add(num);
                    }
                }
                String[] carriages = new String[carriageList.size()];
                carriageList.toArray(carriages);
                return carriages;
            }

            public List<Integer> getFreeSeatIn(int carriage) {
                List<Integer> seats = new ArrayList<>();
                for (Seat s : freeSeats) {
                    if (s.getCarriageNum() == carriage) {
                        seats.add(s.getSeatNum());
                    }
                }
                return seats;
            }

            private void init() {
                SimpleDateFormat format = new SimpleDateFormat("MM月dd日  hh时mm分开");
                String departDate = format.format(extra.departTime);
                freeSeats = MySQLManager.getInstance().dao().getFreeSeats(schedule);

                addItem(dateInfo = new JLabel(train.getTrainId() + "次列车  " + departDate + "    " + seatType + "  " + seatMoney + "元"), 500, 40);
                addItem(trainInfoItem = new TrainInfoItem(schedule, extra));
                addField("乘客姓名", "");
                addField("身份证号码", "");
                addField("手机号码", "");
                carriageBox = addComboBox("车厢", getCarriages());
                (seatNumBox = addComboBox("座位号")).setEnabled(false);

                initBtn(dateInfo, new Font("宋体", Font.PLAIN, 16), SwingConstants.CENTER, SwingConstants.CENTER);
                carriageBox.addItemListener(e -> {
                    if (ItemEvent.SELECTED == e.getStateChange()) {
                        int carriage = Integer.parseInt((String) e.getItem());
                        selectCarriage(carriage);
                    }
                });
                buttonOK.setText("去支付");
                // init carriage and seat
                selectCarriage(Integer.parseInt((String) carriageBox.getItemAt(0)));
                relayout();
            }

            private void selectCarriage(int carriage) {
                seatNumBox.setEnabled(true);
                seatNumBox.removeAllItems();
                for (Integer seat : getFreeSeatIn(carriage)) {
                    seatNumBox.addItem(seat);
                }
            }
        }
    }

    private class PayDialog extends XDialog {
        JXImagePanel imagePanel;
        boolean result = false;

        @Override
        protected void initComponents() {
            try {
                imagePanel = new JXImagePanel(new URL("file:///D:/Andream/CQU/Homework/数据库实验/课程设计/homeward/res/qrcode.png"));
                imagePanel.setPreferredSize(new Dimension(500, 500));
                imagePanel.setStyle(JXImagePanel.Style.SCALED);
                addItem(imagePanel, 500, 500);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onOK() {
            result = true;
            super.onOK();
        }
    }
}
