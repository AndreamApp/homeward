package com.andreamapp.homeward.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLManager {

    // or "com.microsoft.sqlserver.jdbc.SQLServerDriver" for SQL Server
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static MySQLManager sInstance;

    public static synchronized MySQLManager getInstance() {
        if (sInstance == null) {
            sInstance = new MySQLManager();
        }
        return sInstance;
    }

    private Connection mConnection;

    private MySQLManager() {
    }

    /**
     * 通过JDBC驱动连接数据库
     *
     * @param user 数据库用户名
     * @param pass 数据库密码
     * @return 连接状态，如果连接失败则抛出异常
     * @throws SQLException 连接异常
     */
    public boolean connect(String user, String pass) throws SQLException {
        try {
            Class.forName(DRIVER);
            this.mConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_train?characterEncoding=utf-8&serverTimezone=UTC", user, pass);
            return true;
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过JDBC驱动连接数据库
     *
     * @param user 数据库用户名
     * @param pass 数据库密码
     * @return 连接状态
     */
    public boolean connectSilently(String user, String pass) {
        try {
            return connect(user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return 数据库连接状态
     */
    public boolean isClosed() {
        try {
            return mConnection == null || mConnection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * StatementBuilder的包装函数
     * @param sql 待执行的sql语句
     * @return {@link StatementBuilder}
     */
    public StatementBuilder execute(String sql) {
        return new StatementBuilder().execute(sql);
    }

    /**
     * StatementBuilder的包装函数
     * @param sql 待执行的sql语句
     * @return {@link StatementBuilder}
     */
    public StatementBuilder prepare(String sql, Object ... params) {
        return new StatementBuilder().prepare(sql, params);
    }

    private enum StatementState {
        Ready, Batching, Preparing, Commited
    }

    /**
     * Statement语句创建类
     * 使用方法：
     * <pre>
     *     MySQLManager mysql = MySQLManager.getInstance();
     *     mysql.connect("root", "password");
     *
     *     // Single sql update
     *     mysql.execute("sql statement").update();
     *
     *     // Single sql query
     *     ResultSet res = mysql.execute("sql statement").query();
     *     //...
     *     res.close();
     *
     *     // Batch sql update
     *     mysql.execute("insert ...")
     *          .execute("update ...")
     *          .execute("delete ...")
     *          .update();
     *
     *     // Single prepare sql update
     *     ResultSet res = mysql
     *          .prepare("delete from user where name = ?", "Andream")
     *          .update();
     *
     *     // Single prepare sql query
     *     ResultSet res = mysql
     *          .prepare("select * from user where name = ?")
     *          .put("Andream")
     *          .query();
     *     //...
     *     res.close();
     * </pre>
     */
    public class StatementBuilder {
        Statement stmt;
        StatementState state;
        List<String> sqlList;
        int prepareParamIndex;

        public StatementBuilder() {
            this.state = StatementState.Ready;
            this.sqlList = new ArrayList<>(3);
        }

        /**
         * 将一条sql语句加入执行队列中，可以是查询或增删改语句
         * @param sql 待执行的sql语句
         * @return {@link StatementBuilder}
         */
        public StatementBuilder execute(String sql) {
            if (state == StatementState.Ready) {
                state = StatementState.Batching;
                try {
                    stmt = mConnection.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            sqlList.add(sql);
            return this;
        }

        /**
         * 准备执行一条带?参数的Prepare语句，可以是查询或增删改语句
         * @param sql 待?参数的sql语句
         * @return {@link StatementBuilder}
         */
        public StatementBuilder prepare(String sql, Object... params) {
            try {
                state = StatementState.Preparing;
                stmt = mConnection.prepareStatement(sql);
                put(params);
                return this;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 按照顺序填充Prepare语句中的参数
         * @param params 参数
         * @return {@link StatementBuilder}
         */
        public StatementBuilder put(Object... params) {
            if (params != null && params.length > 0) {
                try {
                    PreparedStatement stmt = (PreparedStatement) this.stmt;
                    for (Object obj : params) {
                        prepareParamIndex++;
                        stmt.setObject(prepareParamIndex, obj);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            return this;
        }

        /**
         * 执行查询语句，完成后自动关闭{@link Statement}。如果有超过一条语句，只会执行第一条
         * @return {@link ResultSet} 返回查询结果，查询失败则返回null，并抛出运行时异常
         */
        public ResultSet query() {
            try {
                ResultSet res = null;
                if (state == StatementState.Batching && sqlList.size() >= 1) {
                    res = stmt.executeQuery(sqlList.get(0));
                }
                else if(state == StatementState.Preparing){
                    res = ((PreparedStatement) stmt).executeQuery();
                }
                //close();
                return res;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 执行更新语句
         * 若为批处理模式，执行队列中的sql更新语句。若为Prepare模式，执行单行更新语句。完成后自动关闭{@link Statement}
         * @return int[] 每一条语句更新成功的行数
         */
        public int[] update() {
            try {
                int[] res = null;
                if (state == StatementState.Batching) {
                    for (String sql : sqlList) {
                        stmt.addBatch(sql);
                    }
                    res = stmt.executeBatch();
                }
                else if(state == StatementState.Preparing){
                    boolean isQuery = ((PreparedStatement) stmt).execute();
                    if(!isQuery){
                        res = new int[] { stmt.getUpdateCount() };
                    }
                }
                close();
                return res;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 关闭{@link Statement}
         */
        public void close() {
            try {
                if (stmt != null && !stmt.isClosed()) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
