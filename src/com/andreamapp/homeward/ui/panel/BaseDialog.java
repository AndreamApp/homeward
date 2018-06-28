package com.andreamapp.homeward.ui.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private List<Component> boxList = new ArrayList<>();
    private List<Component> componentList = new ArrayList<>();
    private GroupLayout layout;

    public BaseDialog() {
        contentPane = new JPanel();
        buttonOK = new JButton("确定");
        buttonCancel = new JButton("取消");

        layout = new GroupLayout(contentPane);
        layout.setAutoCreateGaps(true);
        contentPane.setLayout(layout);
        relayout();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    protected void relayout(){
        GroupLayout.SequentialGroup verticle = layout.createSequentialGroup();
        GroupLayout.ParallelGroup horizontal = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for(Component c : boxList){
            verticle.addComponent(c);
            horizontal.addComponent(c);
        }
        verticle.addComponent(buttonOK);
        verticle.addComponent(buttonCancel);
        horizontal.addComponent(buttonOK);
        horizontal.addComponent(buttonCancel);
        layout.setVerticalGroup(verticle);
        layout.setHorizontalGroup(horizontal);
    }

    protected void addItem(Component component){
        addItem(null, component);
    }

    protected void addItem(String text, Component component){
        if(text != null){
            JLabel label = new JLabel();
            label.setText(text);
            Box box = Box.createHorizontalBox();
            box.add(label);
            box.add(component);
            boxList.add(box);
        }
        else{
            boxList.add(component);
        }
        componentList.add(component);
        relayout();
    }

    protected JTextField addFieldItem(String text){
        JTextField edit = new JTextField();
        edit.setColumns(30);
        addItem(text, edit);
        return edit;
    }

    protected String field(int n){
        return ((JTextField) componentList.get(n)).getText();
    }

    protected void onOK() {
        // add your code here
        dispose();
    }

    protected void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void popup(){
        setSize(600, 400);
        setVisible(true);
    }
}
