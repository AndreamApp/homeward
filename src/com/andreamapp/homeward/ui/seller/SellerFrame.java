package com.andreamapp.homeward.ui.seller;

import com.andreamapp.homeward.bean.Manager;
import com.andreamapp.homeward.ui.widget.Measurable;
import com.andreamapp.homeward.utils.Constants;
import com.andreamapp.homeward.utils.LookUtils;
import com.andreamapp.homeward.utils.WidgetUtils;

import javax.swing.*;
import java.awt.*;

public class SellerFrame extends JFrame implements Measurable {
    private OrderPanel orderPanel = new OrderPanel();
    private HistoryPanel historyPanel = new HistoryPanel();
    private ModifyPasswordPanel modifyPasswordPanel = new ModifyPasswordPanel();

    @SuppressWarnings("WeakerAccess")
    public SellerFrame() {
        Constants.checkManagerType(Manager.TYPE_SELLER);
        setTitle("Homeward售票系统");
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

    @Override
    public int width() {
        return 1024;
    }

    @Override
    public int height() {
        return 768;
    }

    public static void main(String[] args) {
        LookUtils.beautyEye();
        WidgetUtils.popup(SellerFrame.class);
    }
}
