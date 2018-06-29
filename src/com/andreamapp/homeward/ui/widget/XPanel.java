package com.andreamapp.homeward.ui.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class XPanel extends JPanel {
    private List<Component> labelList = new ArrayList<>();
    private List<Component> componentList = new ArrayList<>();
    private List<JButton> btnList = new ArrayList<>();

    private int leftMargin = 12;
    private int topMargin = 12;
    private int labelWidth = 100;
    private int labelHeight = 30;
    private int fieldWidth = 300;
    private int fieldHeight = 30;
    private int padding = 10;
    private int btnWidth = 100, btnHeight = 30;
    private int tableHeight = 200;
    protected int panelWidth = 400, panelHeight = 300;

    public XPanel() {
        this.setLayout(null);
        initComponents();
        relayout();
    }

    /**
     * 子类重写该方法用于添加自定义的控件
     */
    protected void initComponents() {
    }

    /**
     * 由于使用绝对布局，每次添加新控件时会调用此方法
     */
    private void relayout() {
        int x = leftMargin + padding, y = topMargin, width = labelWidth, height = labelHeight;
        for (int i = 0; i < labelList.size(); i++) {
            y += padding;
            if (labelList.get(i) != null)
                labelList.get(i).setBounds(x, y, width, height);
            Component c = componentList.get(i);
            int componentHeight = fieldHeight;
            if (c instanceof JScrollPane) {
                componentHeight = tableHeight;
            }
            c.setBounds(x + width + padding, y, fieldWidth, componentHeight);
            y += componentHeight;
            y += padding;
        }
        y += padding;
        // add btn as last line
        int offset = x;
        for (JButton btn : btnList) {
            btn.setBounds(x, y, btnWidth, btnHeight);
            offset += padding + btnWidth + padding;
        }
        offset -= btnWidth + padding;

        y += btnHeight;
        y += padding;
        panelWidth = Math.max(x + width + padding + fieldWidth + padding + leftMargin + 70, offset);
        panelHeight = y + topMargin + 85;
    }

    public Component componentAt(int n) {
        return componentList.get(n);
    }

    public void addItem(Component component) {
        addItem(null, component);
    }

    /**
     * 向容器中添加一个组件
     *
     * @param text      组件的说明文字，显示在左边
     * @param component 组件显示在右边
     */
    public void addItem(String text, Component component) {
        if (text != null) {
            JLabel label = new JLabel();
            label.setText(text);
            labelList.add(label);
            this.add(label);
        } else {
            labelList.add(null);
        }
        componentList.add(component);
        this.add(component);
        relayout();
    }

    /**
     * 向容器中添加一个{@link JLabel}
     *
     * @param name 控件说明文字
     * @param text JLabel的初始内容
     * @return {@link JLabel}
     */
    public JLabel addLabel(String name, String text) {
        JLabel label = new JLabel(text);
        addItem(name, label);
        return label;
    }

    /**
     * 向容器中添加一个{@link JTextField}
     *
     * @param name 控件说明文字
     * @param text JTextField的初始内容
     * @return {@link JTextField}
     */
    public JTextField addField(String name, String text) {
        JTextField edit = new JTextField();
        edit.setColumns(30);
        edit.setText(text);
        addItem(name, edit);
        return edit;
    }

    /**
     * 向容器中添加一个{@link JTextField}，文本框中只能输入非负整数
     *
     * @param name  控件说明文字
     * @param value 初始值
     * @return {@link JTextField}
     */
    public JTextField addNumberField(String name, Integer value) {
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
     * 向容器中添加一个{@link JTextField}，文本框中只能输入非负小数
     *
     * @param name  控件说明文字
     * @param value 初始值
     * @return {@link JTextField}
     */
    public JTextField addFloatField(String name, Float value) {
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
     * 向容器中添加一个{@link JComboBox}
     *
     * @param name    控件说明文字
     * @param options JComboBox的选项
     * @return {@link JComboBox}
     */
    public JComboBox addComboBox(String name, String... options) {
        JComboBox box = new JComboBox<>(options);
        addItem(name, box);
        return box;
    }

    /**
     * 向容器中添加一个{@link JCheckBox}
     *
     * @param name  控件说明文字
     * @param state JCheckBox的初始状态
     * @return {@link JCheckBox}
     */
    public JCheckBox addCheckBox(String name, boolean state) {
        JCheckBox box = new JCheckBox("", state);
        addItem(name, box);
        return box;
    }

    /**
     * 向容器中添加一个{@link JTable}
     *
     * @param text 控件说明文字
     * @return {@link JTable}
     */
    public JTable addTable(String text) {
        XTable table = new XTable();
        JScrollPane scrollPane = new JScrollPane(table);
        addItem(text, scrollPane);
        return table;
    }

    /**
     * 在容器最后一行从左向右依次添加{@link JButton}
     *
     * @param text 控件说明文字
     * @return {@link JButton}
     */
    public JButton addBtn(String text) {
        JButton btn = new JButton(text);
        btnList.add(btn);
        this.add(btn);
        relayout();
        return btn;
    }

    /**
     * 获取{@link JTextField}控件的值
     *
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    public String field(int n) {
        return ((JTextField) componentList.get(n)).getText();
    }

    /**
     * 获取{@link JTextField}控件的值
     *
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    public int fieldInt(int n) {
        return Integer.parseInt(((JTextField) componentList.get(n)).getText());
    }

    /**
     * 获取{@link JTextField}控件的值
     *
     * @param n 控件的序号
     * @return JTextField的当前内容
     */
    public float fieldFloat(int n) {
        return Float.parseFloat(((JTextField) componentList.get(n)).getText());
    }

    /**
     * 获取{@link JComboBox}控件的值
     *
     * @param n 控件的序号
     * @return JComboBox当前所选项的索引值（从0开始）
     */
    public int option(int n) {
        return ((JComboBox) componentList.get(n)).getSelectedIndex();
    }

    /**
     * 获取{@link JComboBox}控件的值
     *
     * @param n 控件的序号
     * @return JComboBox当前所选项的索引值（从0开始）
     */
    public Object optionValue(int n) {
        return ((JComboBox) componentList.get(n)).getSelectedItem();
    }

    /**
     * 获取{@link JCheckBox}控件的值
     *
     * @param n 控件的序号
     * @return JCheckBox当前状态
     */
    public boolean checked(int n) {
        return ((JCheckBox) componentList.get(n)).isSelected();
    }

}
