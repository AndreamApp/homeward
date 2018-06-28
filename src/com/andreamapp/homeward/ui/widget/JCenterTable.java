package com.andreamapp.homeward.ui.widget;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JCenterTable extends JTable{
    private DefaultTableCellRenderer cellRenderer = new MyCellRenderer();

    public JCenterTable(){
        setFont(new Font("宋体", 0, 14));
        setRowHeight(30);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return cellRenderer;
    }

    private class MyCellRenderer extends DefaultTableCellRenderer{
        MyCellRenderer(){
            setHorizontalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (row % 2 == 0)
                setBackground(new Color(255, 255, 255, 30)); // 设置奇数行底色
            else if (row % 2 == 1)
                setBackground(new Color(180, 231, 255, 80)); // 设置偶数行底色
            return super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
        }
    }
}
