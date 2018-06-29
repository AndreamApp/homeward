package com.andreamapp.homeward.ui.seller;

import com.andreamapp.homeward.dao.MySQLManager;
import com.andreamapp.homeward.ui.widget.XPanel;
import com.andreamapp.homeward.utils.Constants;

import javax.swing.*;

public class ModifyPasswordPanel extends XPanel {

    private JPasswordField addPasswordField(String name) {
        JPasswordField field = new JPasswordField();
        addItem(name, field);
        return field;
    }

    @Override
    protected void initComponents() {
        final JPasswordField oldPass = addPasswordField("原密码"),
                newPass = addPasswordField("新密码"),
                newPassConfirm = addPasswordField("确认新密码");
        addBtn("修改").addActionListener(e -> {
            String pass = String.valueOf(oldPass.getPassword());
            String now = String.valueOf(newPass.getPassword());
            String confirm = String.valueOf(newPassConfirm.getPassword());
            if (pass.equals(Constants.currentManager.getPassword())) {
                if (now.equals(confirm)) {
                    MySQLManager.getInstance().dao().modifyPassword(Constants.currentManager, now);
                    JOptionPane.showMessageDialog(null, "修改成功！请牢记您的新密码~");
                } else {
                    JOptionPane.showMessageDialog(null, "两次密码不相同！请重新输入");
                }
            } else {
                JOptionPane.showMessageDialog(null, "原密码错误！请重新输入");
            }
        });
    }
}
