package com.andreamapp.homeward.ui.login;

import com.andreamapp.homeward.bean.Manager;
import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.manager.ManagerFrame;
import com.andreamapp.homeward.ui.seller.SellerFrame;
import com.andreamapp.homeward.ui.widget.Measurable;
import com.andreamapp.homeward.ui.widget.XPanel;
import com.andreamapp.homeward.utils.LookUtils;
import com.andreamapp.homeward.utils.StringUtils;
import com.andreamapp.homeward.utils.WidgetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame implements Measurable {
    private JTextField usernameField, passwordField;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        XPanel contentPane = new XPanel();

        JLabel head = new JLabel("火车票售票系统");
        WidgetUtils.format(head, new Font("黑体", Font.PLAIN, 32), SwingConstants.CENTER, SwingConstants.CENTER);
        contentPane.addItem(head, 300, 80);
        usernameField = contentPane.addHint("用户名");
        passwordField = contentPane.addPasswordHint("密码");
        JButton btnLogin = new JButton("登录");
        btnLogin.addActionListener(this::onLoginClicked);
        contentPane.addItem(btnLogin, 300, 40);

        add(contentPane);
    }

    private void clearPassword() {
        passwordField.setText("");
    }

    private void onLoginClicked(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String error = "未知错误";
        if (StringUtils.empty(username)) {
            error = "请输入用户名！";
        } else if (StringUtils.empty("password")) {
            error = "请输入密码！";
        } else {
            Manager manager = MySQLManager.getInstance().dao().login(username, password);
            if (manager == null) {
                error = "密码错误！";
            } else if (Manager.TYPE_SUPERUSER == manager.getManagerType()) {
                JOptionPane.showMessageDialog(null, "以管理员身份登陆成功！");
                WidgetUtils.popup(ManagerFrame.class);
                dispose();
                return;
            } else if (Manager.TYPE_SELLER == manager.getManagerType()) {
                JOptionPane.showMessageDialog(null, "以售票员身份登陆成功！");
                WidgetUtils.popup(SellerFrame.class);
                dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(null, error);
    }

    @Override
    public int width() {
        return 420;
    }

    @Override
    public int height() {
        return 380;
    }

    public static void main(String[] args) {
        LookUtils.beautyEye();
        WidgetUtils.popup(LoginFrame.class, EXIT_ON_CLOSE);
    }
}
