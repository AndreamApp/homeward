package com.andreamapp.homeward.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

class MySQLManagerTest {

    void connect() {
        try {
            MySQLManager.getInstance().connect("root", "andreamApp97");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void connectSilently() {
        MySQLManager.getInstance().connectSilently("root", "andreamApp97");
    }

    void execute() throws SQLException {
        ResultSet res = MySQLManager.getInstance().execute("select * from manager").query();
        while(res.next()){
            System.out.println(res.getString("username")
                    + res.getString("password")
                    + res.getString("username"));
        }
        res.close();
    }

    void batchExecute() throws SQLException {
       int[] res = MySQLManager.getInstance()
                .execute("update manager set username = 'Andrea' where username = 'Andream'")
                .execute("update manager set username = 'Superscalar' where username = 'Andrea'")
                .update();
    }

    void prepare() throws SQLException {
        ResultSet res = MySQLManager.getInstance()
                .prepare("select * from manager where username = ?", "Andream")
                .query();
        while(res.next()){
            System.out.println(res.getString("username")
                    + res.getString("password")
                    + res.getString("username"));
        }
        res.close();
    }

    public static void main(String[] args) throws SQLException {
        MySQLManagerTest test = new MySQLManagerTest();
        test.connect();
        test.batchExecute();
        test.execute();
    }
}