package com.andreamapp.homeward.ui.seller;

import com.andreamapp.homeward.bean.TrainOrder;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.manager.TrainOrderPanel;

import javax.swing.*;

@SuppressWarnings("WeakerAccess")
public class HistoryPanel extends TrainOrderPanel {
    private JButton btnRefund;

    // TODO: 搜索 修改 *退订
    public HistoryPanel() {
        btnPanel.add(btnRefund = new JButton());
        btnRefund.setText("退款");
        btnRefund.addActionListener(e -> onRefund(table.getSelectedRow()));
    }

    protected void onRefund(int selectedRow) {
        TrainOrder order = orders.get(selectedRow);
        order.setOrderState(TrainOrder.STATE_REFUNDED);
        MySQLManager.getInstance().dao().updateTrainOrder(order);
        JOptionPane.showMessageDialog(null, "退款成功！");
    }
}
