package com.andreamapp.homeward.ui.base;

import com.andreamapp.homeward.ui.widget.XTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;

@SuppressWarnings("FieldCanBeLocal")
public abstract class ModelPanel extends JPanel {
    private JTextField editSearch;
    private JButton btnSearch, btnInsert, btnUpdate, btnDelete;
    protected JTable table;
    private DefaultTableModel model;

    public ModelPanel(){
        // 自定义样式的表格
        table = new XTable();
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
        btnPanel.add(editSearch = new JTextField());
        btnPanel.add(btnSearch = new JButton("搜索"));
        btnPanel.add(btnInsert = new JButton("添加"));
        btnPanel.add(btnUpdate = new JButton("修改"));
        btnPanel.add(btnDelete = new JButton("删除"));
        editSearch.setColumns(50);
        editSearch.setFont(new Font("宋体", Font.PLAIN, 14));
        btnSearch.addActionListener(e -> {
            String key = editSearch.getText();
            onSearch(key.trim());
        });
        btnInsert.addActionListener(e -> onInsert());
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0){ // selected something
                onUpdate(modelIndex(row));
            }
        });
        btnDelete.addActionListener(e -> {
            int[] rows = table.getSelectedRows();
            if(rows.length > 0){
                onDelete(modelIndexes(rows));
            }
        });
        return btnPanel;
    }

    /**
     * 根据视图上的下标值获取对应的Model里的下标值
     * 这两个值并不总是相等的，因为可能有{@link RowSorter}的存在
     * @param index JTable视图下标
     * @return 在Model中的下标
     */
    private int modelIndex(int index){
        RowSorter sorter = table.getRowSorter();
        if(sorter != null){
            return sorter.convertRowIndexToModel(index);
        }
        return index;
    }

    private int[] modelIndexes(int[] index){
        int[] res = new int[index.length];
        for(int i = 0; i < index.length; i++){
            res[i] = modelIndex(index[i]);
        }
        return res;
    }

    private void setModel(TableModel model){
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

    /**
     * 获取表格中各个列的宽度，子类可重写该函数
     * @param columnCount 列数
     * @return 各列宽度的权重，默认权重相同
     */
    public float[] getColumnWeight(int columnCount){
        float[] defaultWeight = new float[columnCount];
        Arrays.fill(defaultWeight, 1.0f);
        return defaultWeight;
    }

    /**
     * 更新Table中的数据，会重新调用{@link #getTableModel()}
     */
    public void refresh(){
        TableModel tableModel = getTableModel();
        setModel(tableModel);
        table.validate();
        table.updateUI();
    }

    public abstract DefaultTableModel getTableModel();
    public abstract void onSearch(String key);
    public abstract void onInsert();
    public abstract void onDelete(int[] selectedRows);
    public abstract void onUpdate(int selectedRow);
}