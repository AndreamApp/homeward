package com.andreamapp.homeward.ui.panel.base;

import com.andreamapp.homeward.ui.widget.JCenterTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "UnusedReturnValue", "WeakerAccess"})
public class BaseDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private List<Component> labelList = new ArrayList<>();
    private List<Component> componentList = new ArrayList<>();

    private int leftMargin = 12;
    private int topMargin = 12;
    private int labelWidth = 100;
    private int labelHeight = 30;
    private int fieldWidth = 300;
    private int fieldHeight = 30;
    private int padding = 10;
    private int btnWidth = 100, btnHeight = 30;
    private int tableHeight = 200;
    private int dialogWidth = 400, dialogHeight = 300;

    public BaseDialog() {
        contentPane = new JPanel();
        buttonOK = new JButton("确定");
        buttonCancel = new JButton("取消");

        contentPane.setLayout(null);
        contentPane.add(buttonOK);
        contentPane.add(buttonCancel);
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
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        initComponents();
    }

    /**
     * 子类重写该方法用于添加自定义的控件
     */
    protected void initComponents(){}

    /**
     * 由于使用绝对布局，每次添加新控件时会调用此方法
     */
    private void relayout(){
        int x = leftMargin + padding, y = topMargin, width = labelWidth, height = labelHeight;
        for(int i = 0; i < labelList.size(); i++){
            y += padding;
            if(labelList.get(i) != null)
                labelList.get(i).setBounds(x, y, width, height);
            Component c = componentList.get(i);
            int componentHeight = fieldHeight;
            if(c instanceof JScrollPane){
                componentHeight = tableHeight;
            }
            c.setBounds(x + width + padding, y, fieldWidth, componentHeight);
            y += componentHeight;
            y += padding;
        }
        y += padding;
        buttonOK.setBounds(x, y, btnWidth, btnHeight);
        buttonCancel.setBounds(x + padding + btnWidth + padding, y, btnWidth, btnHeight);
        y += btnHeight;
        y += padding;
        dialogWidth = x + width + padding + fieldWidth + padding + leftMargin + 70;
        dialogHeight = y + topMargin + 85;
    }

    protected Component componentAt(int n) {
        return componentList.get(n);
    }

    protected void addItem(Component component){
        addItem(null, component);
    }

    /**
     * 向对话框中添加一个组件
     * @param text 组件的说明文字，显示在左边
     * @param component 组件显示在右边
     */
    protected void addItem(String text, Component component){
        if(text != null){
            JLabel label = new JLabel();
            label.setText(text);
            labelList.add(label);
            contentPane.add(label);
        }
        else{
            labelList.add(null);
        }
        componentList.add(component);
        contentPane.add(component);
        relayout();
    }

    /**
     * 向对话框中添加一个{@link JLabel}
     * @param name 控件说明文字
     * @param text JLabel的初始内容
     * @return {@link JLabel}
     */
    protected JLabel addLabel(String name, String text){
        JLabel label = new JLabel(text);
        addItem(name, label);
        return label;
    }

    /**
     * 向对话框中添加一个{@link JTextField}
     * @param name 控件说明文字
     * @param text JTextField的初始内容
     * @return {@link JTextField}
     */
    protected JTextField addField(String name, String text){
        JTextField edit = new JTextField();
        edit.setColumns(30);
        edit.setText(text);
        addItem(name, edit);
        return edit;
    }

    /**
     * 向对话框中添加一个{@link JTextField}，文本框中只能输入非负整数
     *
     * @param name  控件说明文字
     * @param value 初始值
     * @return {@link JTextField}
     */
    protected JTextField addNumberField(String name, Integer value) {
        JTextField field = new JTextField();
        field.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
        if (value != null) {
            field.setText(String.valueOf(value));
        }
        addItem(name, field);
        return field;
    }

    /**
     * 向对话框中添加一个{@link JTextField}，文本框中只能输入非负小数
     *
     * @param name  控件说明文字
     * @param value 初始值
     * @return {@link JTextField}
     */
    protected JTextField addFloatField(String name, Float value) {
        JTextField field = new JTextField();
        field.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == '.') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
        if (value != null) {
            field.setText(String.valueOf(value));
        }
        addItem(name, field);
        return field;
    }

    /**
     * 向对话框中添加一个{@link JComboBox}
     * @param name 控件说明文字
     * @param options JComboBox的选项
     * @return {@link JComboBox}
     */
    protected JComboBox addComboBox(String name, String... options){
        JComboBox box = new JComboBox<>(options);
        addItem(name, box);
        return box;
    }

    /**
     * 向对话框中添加一个{@link JCheckBox}
     * @param name 控件说明文字
     * @param state JCheckBox的初始状态
     * @return {@link JCheckBox}
     */
    protected JCheckBox addCheckBox(String name, boolean state){
        JCheckBox box = new JCheckBox("", state);
        addItem(name, box);
        return box;
    }

    /**
     * 向对话框中添加一个{@link JTable}
     * @param text 控件说明文字
     * @return {@link JTable}
     */
    protected JTable addTable(String text){
        JCenterTable table = new JCenterTable();
        JScrollPane scrollPane = new JScrollPane(table);
        addItem(text, scrollPane);
        return table;
    }

    /**
     * 获取{@link JTextField}控件的值
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    protected String field(int n){
        return ((JTextField) componentList.get(n)).getText();
    }

    /**
     * 获取{@link JTextField}控件的值
     *
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    protected int fieldInt(int n) {
        return Integer.parseInt(((JTextField) componentList.get(n)).getText());
    }

    /**
     * 获取{@link JTextField}控件的值
     *
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    protected float fieldFloat(int n) {
        return Float.parseFloat(((JTextField) componentList.get(n)).getText());
    }

    /**
     * 获取{@link JComboBox}控件的值
     * @param n 控件的序号
     * @return JComboBox当前所选项的索引值（从0开始）
     */
    protected int option(int n){
        return ((JComboBox) componentList.get(n)).getSelectedIndex();
    }

    /**
     * 获取{@link JComboBox}控件的值
     *
     * @param n 控件的序号
     * @return JComboBox当前所选项的索引值（从0开始）
     */
    protected Object optionValue(int n) {
        return ((JComboBox) componentList.get(n)).getSelectedItem();
    }

    /**
     * 获取{@link JCheckBox}控件的值
     * @param n 控件的序号
     * @return JCheckBox当前状态
     */
    protected boolean checked(int n){
        return ((JCheckBox) componentList.get(n)).isSelected();
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
        setSize(dialogWidth, dialogHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
