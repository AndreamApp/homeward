package com.andreamapp.homeward.ui.panel;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class ListTableModel<T> extends DefaultTableModel{
    private List<T> data;
    private float[] weights;

    public ListTableModel(String[] columns, List<T> data, float[] weights) {
        super(columns, data.size());
        this.weights = weights;
        this.data = data;
    }

    public ListTableModel(Object[][] res, String[] columns) {
        super(res, columns);
    }

    public ListTableModel(String[] columns, List<T> data) {
        super(columns, data.size());
        this.data = data;
        this.weights = new float[columns.length];
        Arrays.fill(this.weights, 1);
    }

    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public float[] getWeights() {
        return weights;
    }

    public void setWeights(float[] weights) {
        this.weights = weights;
    }
}
