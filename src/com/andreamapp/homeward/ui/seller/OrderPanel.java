package com.andreamapp.homeward.ui.seller;

import com.andreamapp.homeward.bean.Train;
import com.andreamapp.homeward.bean.TrainSchedule;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.widget.XTextField;
import com.andreamapp.homeward.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private class ScheduleItem extends JPanel {
        private TrainSchedule schedule;
        private Train train;
        private TrainSchedule.Extra extra;
        private String departStationNameInQuery, arriveStationNameInQuery;

        private JLabel departTime, departStation, arriveTime, arriveStation;
        private JLabel trainId, passTime;
        private JLabel hardSeat, hardBerth, softBerth, noSeat;
        private JButton orderHardSeat, orderHardBerth, orderSoftBerth, orderNoSeat;
        int itemWidth = 900, itemHeight = 200;

        public ScheduleItem(TrainSchedule schedule, String departStation, String arriveStation, boolean isStudent) {
            this.schedule = schedule;
            this.train = schedule.getTrain();
            this.departStationNameInQuery = departStation;
            this.arriveStationNameInQuery = arriveStation;
            this.extra = schedule.getExtra(departStation, arriveStation, isStudent);
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
            add(departTime = new JLabel());
            add(arriveTime = new JLabel());
            add(departStation = new JLabel());
            add(arriveStation = new JLabel());
            add(trainId = new JLabel());
            add(passTime = new JLabel());
            add(hardSeat = new JLabel());
            add(hardBerth = new JLabel());
            add(softBerth = new JLabel());
            add(noSeat = new JLabel());
            add(orderHardSeat = new JButton("预订"));
            add(orderHardBerth = new JButton("预订"));
            add(orderSoftBerth = new JButton("预订"));
            add(orderNoSeat = new JButton("预订"));
            orderHardBerth.addActionListener(e -> {
//                TODO
//                buyer info
//                choose seat
//                MySQLManager.getInstance().dao().insertTrainOrder(order);
//                jie zhang dialog
            });
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
            departTime.setBounds(x, y, 100, 50);
            y += padding + 50 + padding;
            departStation.setBounds(x, y, 100, 50);
            x += 100 + 50;
            y = leftMargin + padding + 20;
            trainId.setBounds(x, y, 160, 30);
            y += padding + 30 + padding;
            passTime.setBounds(x, y, 160, 30);
            x += 160 + 50;
            y = leftMargin + padding;
            arriveTime.setBounds(x, y, 100, 50);
            y += padding + 50 + padding;
            arriveStation.setBounds(x, y, 100, 50);
            x += 100 + 50;
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
    }
}
