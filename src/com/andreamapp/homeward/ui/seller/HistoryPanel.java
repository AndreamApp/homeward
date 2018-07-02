package com.andreamapp.homeward.ui.seller;

import com.andreamapp.homeward.bean.TrainOrder;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.manager.TrainOrderPanel;
import com.andreamapp.homeward.ui.widget.XDialog;

import javax.swing.*;

@SuppressWarnings("WeakerAccess")
public class HistoryPanel extends TrainOrderPanel {
    private JButton btnRefund;

    public HistoryPanel() {
        btnPanel.add(btnRefund = new JButton());
        btnRefund.setText("退款");
        btnRefund.addActionListener(e -> onRefund(table.getSelectedRow()));
    }

    protected void onRefund(int selectedRow) {
        TrainOrder order = orders.get(selectedRow);
        new XDialog() {
            @Override
            protected void initComponents() {
                addItem(new JLabel("需要退款 " + order.getMoney() + " 元。\n确定要退款吗？"));
            }

            @Override
            protected void onOK() {
                order.setOrderState(TrainOrder.STATE_REFUNDED);
                MySQLManager.getInstance().dao().updateTrainOrder(order);
                JOptionPane.showMessageDialog(null, "退款成功！");
                super.onOK();
            }
        }.popup("确认退款");
    }
}
