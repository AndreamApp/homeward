package com.andreamapp.homeward.ui.panel.base;

import com.andreamapp.homeward.ui.widget.JCenterTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

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
    int dialogWidth = 400, dialogHeight = 300;

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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    protected void relayout(){
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
        dialogWidth = x + width + padding + fieldWidth + padding + leftMargin + 20;
        dialogHeight = y + topMargin + 40;
    }

    protected void addItem(Component component){
        addItem(null, component);
    }

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

    protected JLabel addLabel(String name, String text){
        JLabel label = new JLabel(text);
        addItem(name, label);
        return label;
    }

    protected JTextField addField(String name, String text){
        JTextField edit = new JTextField();
        edit.setColumns(30);
        edit.setText(text);
        addItem(name, edit);
        return edit;
    }

    protected JComboBox addComboBox(String name, String... options){
        JComboBox box = new JComboBox<>(options);
        addItem(name, box);
        return box;
    }

    protected JCheckBox addCheckBox(String text, boolean state){
        JCheckBox box = new JCheckBox("", state);
        addItem(text, box);
        return box;
    }

    protected JTable addTable(String text){
        JCenterTable table = new JCenterTable();
        JScrollPane scrollPane = new JScrollPane(table);
        addItem(text, scrollPane);
        return table;
    }

    protected String field(int n){
        return ((JTextField) componentList.get(n)).getText();
    }

    protected int option(int n){
        return ((JComboBox) componentList.get(n)).getSelectedIndex();
    }

    protected boolean checked(int n){
        return ((JCheckBox) componentList.get(n)).isSelected();
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
        popup("");
    }

    public void popup(String title){
        setTitle(title);
        setSize(dialogWidth, dialogHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
