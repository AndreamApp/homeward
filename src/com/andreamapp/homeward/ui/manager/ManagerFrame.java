package com.andreamapp.homeward.ui.manager;

import com.andreamapp.homeward.ui.widget.Measurable;
import com.andreamapp.homeward.utils.LookUtils;
import com.andreamapp.homeward.utils.WidgetUtils;

import javax.swing.*;
import java.awt.*;

public class ManagerFrame extends JFrame implements Measurable {

    private CustomerPanel customerPanel = new CustomerPanel();
    private ManagerPanel managerPanel = new ManagerPanel();
    private SellPointPanel sellPointPanel = new SellPointPanel();
    private TrainPanel trainPanel = new TrainPanel();
    private TrainSchedulePanel schedulePanel = new TrainSchedulePanel();
    private TrainOrderPanel orderPanel = new TrainOrderPanel();

    @SuppressWarnings("WeakerAccess")
    public ManagerFrame() {
        initComponents();
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


    public static void main(String[] args) {
        LookUtils.beautyEye();
        WidgetUtils.popup(ManagerFrame.class);
    }

    @Override
    public int width() {
        return 1024;
    }

    @Override
    public int height() {
        return 768;
    }
}
