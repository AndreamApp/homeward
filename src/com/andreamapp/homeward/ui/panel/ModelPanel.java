package com.andreamapp.homeward.ui.panel;

import com.andreamapp.homeward.ui.widget.JCenterTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class ModelPanel extends JPanel {
    private JButton btnInsert, btnDelete, btnUpdate, btnSelect;
    protected JTable table;
    protected TableModel model;

    public ModelPanel(){
        // 自定义样式的表格
        table = new JCenterTable();
        model = getTableModel();
        setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        setLayout(new BorderLayout());
        add(btnPanel(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel btnPanel(){
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnInsert = new JButton("添加"));
        btnPanel.add(btnDelete = new JButton("删除"));
        btnPanel.add(btnUpdate = new JButton("修改"));
        btnPanel.add(btnSelect = new JButton("筛选"));
        btnInsert.addActionListener(e -> onInsert());
        btnDelete.addActionListener(e -> onDelete(table.getSelectedRows()));
        btnUpdate.addActionListener(e -> onUpdate(table.getSelectedRow()));
        btnSelect.addActionListener(e -> onSelect());
        return btnPanel;
    }

    private void popupDialog(Class<? extends BaseDialog> cls){
        if(cls == null) return;
        try {
            Constructor<? extends BaseDialog> constructor = cls.getConstructor();
            BaseDialog dialog = constructor.newInstance();
            Method popup = cls.getMethod("popup");
            popup.invoke(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModel(TableModel model){
        // 列宽
        setColumnWeight();
        // 排序
        RowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        table.setModel(model);
    }

    private void setColumnWeight(){
        addComponentListener(new ComponentListener() {
            boolean loaded = false;
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                if(loaded) return;
                float[] weight = getColumnWeight(table.getColumnCount());
                float weightSum = 0;
                for(float w : weight)
                    weightSum += w;
                for(int i = 0; i < table.getColumnCount(); i++){
                    TableColumn column = table.getColumnModel().getColumn(i);
                    int width = (int) (getWidth() * (weight[i] / weightSum));
                    column.setPreferredWidth(width);
                }
                loaded = true;
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    public float[] getColumnWeight(int columnCount){
        float[] defaultWeight = new float[columnCount];
        Arrays.fill(defaultWeight, 1.0f);
        return defaultWeight;
    }

    public void refresh(){
        TableModel tableModel = getTableModel();
        setModel(tableModel);
        table.validate();
        table.updateUI();
        //((DefaultTableModel)table.getModel()).fireTableDataChanged();
    }

    public abstract TableModel getTableModel();
    public abstract void onInsert();
    public abstract void onDelete(int[] selectedRows);
    public abstract void onUpdate(int selectedRow);
    public abstract void onSelect();
}