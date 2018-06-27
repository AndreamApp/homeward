package com.andreamapp.homeward.ui;

import javax.swing.*;

public class Test {
    private JPanel panel1;
    private JTable table1;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new Test().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //System.out.println("create:" + table1);
    }
}
