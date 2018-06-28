package com.andreamapp.homeward.ui;

import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.panel.*;
import com.andreamapp.homeward.utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class SuperUserMainFrame extends JFrame{

    private CustomerPanel customerPanel = new CustomerPanel();
    private ManagerPanel managerPanel = new ManagerPanel();
    private SellPointPanel sellPointPanel = new SellPointPanel();
    private TrainPanel trainPanel = new TrainPanel();
    private TrainSchedulePanel schedulePanel = new TrainSchedulePanel();
    private TrainOrderPanel orderPanel = new TrainOrderPanel();

    public SuperUserMainFrame(){
        initComponents();
        setSize(1024, 768);
    }

    private void initComponents(){
        JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.setFont(new Font("宋体", Font.PLAIN, 14));
        tabbedPane1.addTab("用户管理", customerPanel);
        tabbedPane1.addTab("售票员管理", managerPanel);
        tabbedPane1.addTab("售票点管理", sellPointPanel);
        tabbedPane1.addTab("列车管理", trainPanel);
        tabbedPane1.addTab("列车行程管理", schedulePanel);
        tabbedPane1.addTab("订单管理", orderPanel);

        add(tabbedPane1);
    }


    public static void main(String[] args) throws SQLException {
        MySQLManager.getInstance().connect("root", "andreamApp97");
        JFrame frame = new SuperUserMainFrame();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
