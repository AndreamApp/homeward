package com.andreamapp.homeward.ui.seller;

import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.utils.LookUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class SellerFrame extends JFrame {
    private OrderPanel orderPanel = new OrderPanel();
    private HistoryPanel historyPanel = new HistoryPanel();
    private ModifyPasswordPanel modifyPasswordPanel = new ModifyPasswordPanel();

    @SuppressWarnings("WeakerAccess")
    public SellerFrame() {
        setSize(600, 500);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.setFont(new Font("宋体", Font.PLAIN, 14));
        tabbedPane1.addTab("预订列表", orderPanel);
        tabbedPane1.addTab("预订记录", historyPanel);
        tabbedPane1.addTab("修改密码", modifyPasswordPanel);

        add(tabbedPane1);
    }

    public static void main(String[] args) throws SQLException {
        LookUtils.beautyEye();
        MySQLManager.getInstance().connect("root", "andreamApp97");
        JFrame frame = new SellerFrame();
        frame.setVisible(true);
    }
}
