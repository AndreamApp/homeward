package com.andreamapp.homeward.ui.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings({"FieldCanBeLocal", "UnusedReturnValue", "WeakerAccess"})
public class XDialog extends JDialog {
    private XPanel panel;
    private JButton buttonOK, buttonCancel;

    public XDialog() {
        panel = new XPanel();

        buttonOK = panel.addBtn("确定");
        buttonCancel = panel.addBtn("取消");

        setContentPane(panel);
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
        panel.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        initComponents();
    }

    /**
     * 子类重写该方法用于添加自定义的控件
     */
    protected void initComponents(){}

    public Component componentAt(int n) {
        return panel.componentAt(n);
    }

    public void addItem(Component component) {
        panel.addItem(component);
    }

    /**
     * 向对话框中添加一个组件
     * @param text 组件的说明文字，显示在左边
     * @param component 组件显示在右边
     */
    public void addItem(String text, Component component) {
        panel.addItem(text, component);
    }

    /**
     * 向对话框中添加一个{@link JLabel}
     * @param name 控件说明文字
     * @param text JLabel的初始内容
     * @return {@link JLabel}
     */
    public JLabel addLabel(String name, String text) {
        return panel.addLabel(name, text);
    }

    /**
     * 向对话框中添加一个{@link JTextField}
     * @param name 控件说明文字
     * @param text JTextField的初始内容
     * @return {@link JTextField}
     */
    public JTextField addField(String name, String text) {
        return panel.addField(name, text);
    }

    /**
     * 向对话框中添加一个{@link JTextField}，文本框中只能输入非负整数
     *
     * @param name  控件说明文字
     * @param value 初始值
     * @return {@link JTextField}
     */
    public JTextField addNumberField(String name, Integer value) {
        return panel.addNumberField(name, value);
    }

    /**
     * 向对话框中添加一个{@link JTextField}，文本框中只能输入非负小数
     *
     * @param name  控件说明文字
     * @param value 初始值
     * @return {@link JTextField}
     */
    public JTextField addFloatField(String name, Float value) {
        return panel.addFloatField(name, value);
    }

    /**
     * 向对话框中添加一个{@link JComboBox}
     * @param name 控件说明文字
     * @param options JComboBox的选项
     * @return {@link JComboBox}
     */
    public JComboBox addComboBox(String name, String... options) {
        return panel.addComboBox(name, options);
    }

    /**
     * 向对话框中添加一个{@link JCheckBox}
     * @param name 控件说明文字
     * @param state JCheckBox的初始状态
     * @return {@link JCheckBox}
     */
    public JCheckBox addCheckBox(String name, boolean state) {
        return panel.addCheckBox(name, state);
    }

    /**
     * 向对话框中添加一个{@link JTable}
     * @param text 控件说明文字
     * @return {@link JTable}
     */
    public JTable addTable(String text) {
        return panel.addTable(text);
    }

    /**
     * 获取{@link JTextField}控件的值
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    public String field(int n) {
        return panel.field(n);
    }

    /**
     * 获取{@link JTextField}控件的值
     *
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    public int fieldInt(int n) {
        return panel.fieldInt(n);
    }

    /**
     * 获取{@link JTextField}控件的值
     *
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    public float fieldFloat(int n) {
        return panel.fieldFloat(n);
    }

    /**
     * 获取{@link JComboBox}控件的值
     * @param n 控件的序号
     * @return JComboBox当前所选项的索引值（从0开始）
     */
    public int option(int n) {
        return panel.option(n);
    }

    /**
     * 获取{@link JComboBox}控件的值
     *
     * @param n 控件的序号
     * @return JComboBox当前所选项的索引值（从0开始）
     */
    public Object optionValue(int n) {
        return panel.optionValue(n);
    }

    /**
     * 获取{@link JCheckBox}控件的值
     * @param n 控件的序号
     * @return JCheckBox当前状态
     */
    public boolean checked(int n) {
        return panel.checked(n);
    }

    /**
     * 确定按钮的回调函数
     */
    protected void onOK() {
        // add your code here
        dispose();
    }

    /**
     * 取消按钮的回调函数
     */
    protected void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * 启动一个无标题的对话框
     * convenient for {@link #popup(String)}
     */
    public void popup(){
        popup("");
    }

    /**
     * 根据布局设置合适的大小，并启动该对话框
     * @param title 对话框标题
     */
    public void popup(String title){
        setTitle(title);
        setSize(panel.panelWidth, panel.panelHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
