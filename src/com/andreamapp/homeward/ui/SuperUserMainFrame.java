package com.andreamapp.homeward.ui;

import com.andreamapp.homeward.bean.*;
import com.andreamapp.homeward.dao.MySQLDAO;
import com.andreamapp.homeward.dao.MySQLManager;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SuperUserMainFrame extends JFrame{

    private CustomerPanel customerPanel = new CustomerPanel();
    private ManagerPanel managerPanel = new ManagerPanel();
    private SellPointPanel sellPointPanel = new SellPointPanel();
    private TrainPanel trainPanel = new TrainPanel();
    private TrainSchedulePanel schedulePanel = new TrainSchedulePanel();
    private TrainOrderPanel orderPanel = new TrainOrderPanel();

    public SuperUserMainFrame(){
        setSize(1024, 768);
        initComponents();
    }

    private void initComponents(){
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setFont(new Font("宋体", 0, 14));
        tabbedPane1.addTab("用户管理", customerPanel);
        tabbedPane1.addTab("售票员管理", managerPanel);
        tabbedPane1.addTab("售票点管理", sellPointPanel);
        tabbedPane1.addTab("列车管理", trainPanel);
        tabbedPane1.addTab("列车行程管理", schedulePanel);
        tabbedPane1.addTab("订单管理", orderPanel);

        add(tabbedPane1);
    }

    private JTabbedPane tabbedPane1;

    private abstract class ModelPanel extends JPanel {
        private JButton btnInsert = new JButton("添加");
        private JButton btnDelete = new JButton("删除");
        private JButton btnUpdate = new JButton("修改");
        private JButton btnSelect = new JButton("筛选");
        private JTable table;

        ModelPanel(){
            // 自定义样式的表格
            table = new JCenterTable();
            setModel(getTableModel());

            JScrollPane scrollPane = new JScrollPane(table);
            table.setFillsViewportHeight(true);

            setLayout(new BorderLayout());
            add(btnPanel(), BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);

            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }

        private JPanel btnPanel(){
            btnInsert.addActionListener(e -> onInsert());
            btnDelete.addActionListener(e -> onDelete(table.getSelectedRows()));
            btnUpdate.addActionListener(e -> onInsert());
            btnSelect.addActionListener(e -> onInsert());

            JPanel btnPanel = new JPanel(new FlowLayout());
            btnPanel.add(btnInsert);
            btnPanel.add(btnDelete);
            btnPanel.add(btnUpdate);
            btnPanel.add(btnSelect);
            return btnPanel;
        }

        public void setModel(TableModel model){
            table.setModel(model);
            // 列宽
            setColumnWeight();
            // 排序
            RowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);
        }

        private void setColumnWeight(){
            addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                    float[] weight = getColumnWeight(table.getModel().getColumnCount());
                    float weightSum = 0;
                    for(float w : weight)
                        weightSum += w;
                    for(int i = 0; i < table.getColumnCount(); i++){
                        TableColumn column = table.getColumnModel().getColumn(i);
                        int width = (int) (getWidth() * (weight[i] / weightSum));
                        column.setPreferredWidth(width);
                    }
                }
                @Override
                public void windowClosing(WindowEvent e) {

                }
                @Override
                public void windowClosed(WindowEvent e) {

                }
                @Override
                public void windowIconified(WindowEvent e) {

                }
                @Override
                public void windowDeiconified(WindowEvent e) {

                }
                @Override
                public void windowActivated(WindowEvent e) {

                }
                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
        }

        public float[] getColumnWeight(int columnCount){
            float[] defaultWeight = new float[columnCount];
            Arrays.fill(defaultWeight, 1.0f);
            return defaultWeight;
        }

        public abstract TableModel getTableModel();
        public abstract void onInsert();
        public abstract void onDelete(int[] selectedRows);
        public abstract void onUpdate(int[] selectedRows);
        public abstract void onSelect();
    }

    private class CustomerPanel extends ModelPanel{
        List<Customer> data;

        @Override
        public TableModel getTableModel() {
            if(data == null){
                data = MySQLManager.getInstance().dao().getAllCustomers();
            }
            return MySQLManager.getInstance().dao().getCustomerModel(data);
        }

        @Override
        public float[] getColumnWeight(int columnCount) {
            return new float[]{
                2, 2, 1, 1, 1
            };
        }

        @Override
        public void onInsert() {
            // InsertDialog

        }

        @Override
        public void onDelete(int[] selectedRows) {
        }

        @Override
        public void onUpdate(int[] selectedRows) {
        }

        @Override
        public void onSelect() {
            // SelectDialog
        }
    }

    private class ManagerPanel extends ModelPanel{
        List<Manager> data;

        @Override
        public TableModel getTableModel() {
            if(data == null){
                data = MySQLManager.getInstance().dao().getAllManagers();
            }
            return MySQLManager.getInstance().dao().getManagerModel(data);
        }

        @Override
        public float[] getColumnWeight(int columnCount) {
            return new float[]{
                    1, 1, 2, 2, 2, 1, 1
            };
        }

        @Override
        public void onInsert() {
            // InsertDialog
        }

        @Override
        public void onDelete(int[] selectedRows) {
        }

        @Override
        public void onUpdate(int[] selectedRows) {
        }

        @Override
        public void onSelect() {
            // SelectDialog
        }
    }

    private class SellPointPanel extends ModelPanel{
        List<TicketPoint> data;

        @Override
        public TableModel getTableModel() {
            if(data == null){
                data = MySQLManager.getInstance().dao().getAllTicketPoints();
            }
            return MySQLManager.getInstance().dao().getTicketPointModel(data);
        }

        @Override
        public void onInsert() {
            // InsertDialog
        }

        @Override
        public void onDelete(int[] selectedRows) {
        }

        @Override
        public void onUpdate(int[] selectedRows) {
        }

        @Override
        public void onSelect() {
            // SelectDialog
        }
    }

    private class TrainPanel extends ModelPanel{

        List<Train> data;

        @Override
        public TableModel getTableModel() {
            if(data == null){
                data = MySQLManager.getInstance().dao().getAllTrains();
            }
            return MySQLManager.getInstance().dao().getTrainModel(data);
        }

        @Override
        public void onInsert() {
            // InsertDialog
        }

        @Override
        public void onDelete(int[] selectedRows) {
        }

        @Override
        public void onUpdate(int[] selectedRows) {
        }

        @Override
        public void onSelect() {
            // SelectDialog
        }
    }

    private class TrainSchedulePanel extends ModelPanel{

        List<TrainSchedule> data;

        @Override
        public TableModel getTableModel() {
            if(data == null){
                data = MySQLManager.getInstance().dao().getTrainScheduleList(100, 0);
            }
            return MySQLManager.getInstance().dao().getTrainScheduleModel(data);
        }

        @Override
        public void onInsert() {
            // InsertDialog
        }

        @Override
        public void onDelete(int[] selectedRows) {
        }

        @Override
        public void onUpdate(int[] selectedRows) {
        }

        @Override
        public void onSelect() {
            // SelectDialog
        }
    }

    private class TrainOrderPanel extends ModelPanel{

        List<TrainOrder> data;

        @Override
        public TableModel getTableModel() {
            if(data == null){
                data = MySQLManager.getInstance().dao().getTrainOrderList(100, 0);
            }
            return MySQLManager.getInstance().dao().getTrainOrderModel(data);
        }

        @Override
        public void onInsert() {
            // InsertDialog
        }

        @Override
        public void onDelete(int[] selectedRows) {
        }

        @Override
        public void onUpdate(int[] selectedRows) {
        }

        @Override
        public void onSelect() {
            // SelectDialog
        }
    }

    public static void main(String[] args) throws SQLException {
        MySQLManager.getInstance().connect("root", "andreamApp97");
        JFrame frame = new SuperUserMainFrame();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
