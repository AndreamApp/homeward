package com.andreamapp.homeward.utils;

public class Constants {
    public static class ColumnName{
        public static final String[] CUSTOMER = {
                "身份证号码", "姓名", "性别", "电话", "用户类型"
        };
        public static final String[] MANAGER = {
                "管理员ID", "售票点ID", "用户名", "密码", "姓名", "性别", "管理员类别"
        };
        public static final String[] STATION = {
                "站点ID", "站点名称"
        };
        public static final String[] TICKET_POINT = {
                "售票点ID", "售票点名称", "售票点地址", "营业时间"
        };
        public static final String[] TRAIN = {
                "车次", "列车类型", "途经站点" // , "座位"
        };
        public static final String[] SCHEDULE = {
                "列车行程ID", "出发时间", "预售时间", "配速", "车次"
        };
        public static final String[] ORDER = {
                "订单ID", "售票点ID", "用户身份证", "列车行程ID", "座位ID", "车次", "起始站", "到达站", "学生票", "金额", "订单状态" // 座位车厢 座位号 座位类别
        };
    }
}
