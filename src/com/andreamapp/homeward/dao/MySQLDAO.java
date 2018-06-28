package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MySQLDAO implements
        CustomerDAO, ManagerDAO, StationDAO, TicketPointDAO, TrainDAO, TrainOrderDAO, TrainScheduleDAO
{
    @SuppressWarnings("WeakerAccess")
    public ResultSet query(String sql){
        return MySQLManager.getInstance().execute(sql).query();
    }

    @SuppressWarnings("WeakerAccess")
    public int[] update(String sql){
        return MySQLManager.getInstance().execute(sql).update();
    }

    @SuppressWarnings("WeakerAccess")
    public ResultSet query(String sql, Object ... params){
        if(params == null || params.length == 0) return query(sql);
        return MySQLManager.getInstance().prepare(sql, params).query();
    }

    @SuppressWarnings("WeakerAccess")
    public int[] update(String sql, Object ... params){
        if(params == null || params.length == 0) return update(sql);
        return MySQLManager.getInstance().prepare(sql, params).update();
    }

    private interface Parser<T> {
        T parse(ResultSet res) throws SQLException;
    }

    private class Loader<T> {
        private Parser<T> parser;

        Loader(Parser<T> parser) {
            this.parser = parser;
        }

        List<T> load(String sql, Object... params) {
            List<T> rows = new ArrayList<>();
            try {
                try (ResultSet res = query(sql, params)) {
                    while (res.next()) {
                        rows.add(parser.parse(res));
                    }
                }
                return rows;
            } catch (SQLException e) {
                e.printStackTrace();
                return rows;
            }
        }

        T loadOne(String sql, Object... params) {
            T row = null;
            try {
                try (ResultSet res = query(sql, params)) {
                    if (res.next()) {
                        row = parser.parse(res);
                    }
                }
                return row;
            } catch (SQLException e) {
                e.printStackTrace();
                return row;
            }
        }
    }

    @Override
    public void insertManager(Manager manager) {
        update("insert into manager " +
                        "(point_id, username, password, name, sex, manager_type) values " +
                        "(?, ?, ?, ?, ?, ?)",
                manager.getPointId(),
                manager.getUsername(),
                manager.getPassword(),
                manager.getName(),
                manager.getSex(),
                manager.getManagerType()
        );
    }

    @Override
    public void deleteManager(Manager manager) {
        update("delete from manager where manager_id = ?", manager.getManagerId());
    }

    @Override
    public void updateManager(Manager manager) {
        update("update manager set point_id = ?, username = ?, name = ?, sex = ?, manager_type = ?" +
                " where manager_id = ?",
                manager.getPointId(), manager.getUsername(), manager.getName(), manager.getSex(), manager.getManagerType(),
                manager.getManagerId());
    }

    private Manager getManagerFromResult(ResultSet res) throws SQLException {
        Manager manager = new Manager();
        manager.setManagerId(res.getInt("manager_id"));
        manager.setManagerType(res.getInt("manager_type"));
        manager.setName(res.getString("name"));
        manager.setPassword(res.getString("password"));
        manager.setPointId(res.getInt("point_id"));
        manager.setSex(res.getInt("sex"));
        manager.setUsername(res.getString("username"));
        return manager;
    }

    @Override
    public Manager getManagerById(String manager_id) {
        return new Loader<>(this::getManagerFromResult)
                .loadOne("select * from manager where manager_id = ?", manager_id);
    }

    @Override
    public List<Manager> getAllManagers() {
        return new Loader<>(this::getManagerFromResult)
                .load("select * from manager");
    }

    @Override
    public List<Manager> searchManagers(String key) {
        key = '%' + key + '%';
        return new Loader<>(this::getManagerFromResult)
                .load("select * from manager where manager_id = ? or name like ? or username like ?" +
                                " or point_id = ?",
                        key, key, key, key);
    }

    @SuppressWarnings("WeakerAccess")
    public Manager getManagerByUsername(String username) {
        return new Loader<>(this::getManagerFromResult)
                .loadOne("select * from manager where username = ?", username);
    }

    @Override
    public Manager login(String username, String password) {
        Manager manager = getManagerByUsername(username);
        if(manager != null && manager.getPassword().equals(password)){
            return manager;
        }
        return null;
    }

    @Override
    public void insertCustomer(Customer customer) {
        update("insert into customer " +
                        "(name, sex, id_num, tel, customer_type) values " +
                        "(?, ?, ?, ?, ?)",
                customer.getName(),
                customer.getSex(),
                customer.getIdNum(),
                customer.getTel(),
                customer.getCustomerType()
        );
    }

    @Override
    public void deleteCustomer(Customer customer) {
        update("delete from customer where id_num = ?", customer.getIdNum());
    }

    @Override
    public void updateCustomer(Customer customer) {
        update("update customer set name = ?, sex = ?, tel = ?, customer_type = ?" +
                        " where id_num = ?",
                customer.getName(), customer.getSex(), customer.getTel(), customer.getCustomerType(),
                customer.getIdNum());
    }

    @Override
    public void upsertCustomer(Customer customer) {

    }

    private Customer getCustomerFromResult(ResultSet res) throws SQLException {
        Customer customer = new Customer();
        customer.setIdNum(res.getString("id_num"));
        customer.setName(res.getString("name"));
        customer.setTel(res.getString("tel"));
        customer.setSex(res.getInt("sex"));
        customer.setCustomerType(res.getInt("customer_type"));
        return customer;
    }

    @Override
    public Customer getCustomerByIdNum(String id_num) {
        return new Loader<>(this::getCustomerFromResult)
                .loadOne("select * from customer where id_num = ?", id_num);
    }

    @Override
    public List<Customer> searchCustomers(String key) {
        key = '%' + key + '%';
        return new Loader<>(this::getCustomerFromResult)
                .load("select * from customer where id_num like ? or name like ? or tel = ?",
                        key, key, key);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return new Loader<>(this::getCustomerFromResult)
                .load("select * from customer");
    }

    @Override
    public void insertStation(Station station) {
        update("insert into station " +
                        "(station_name) values " +
                        "(?)",
                station.getStationName()
        );
    }

    @Override
    public void deleteStation(Station station) {
        deleteStation(station.getStationId());
    }

    @Override
    public void deleteStation(int station_id) {
        update("delete from station where station_id = ?", station_id);
    }

    @Override
    public void updateStation(Station station) {
        update("update station set station_name = ?" +
                        " where station_id = ?",
                station.getStationName(), station.getStationId());
    }

    private Station getStationFromResult(ResultSet res) throws SQLException {
        Station station = new Station();
        station.setStationId(res.getInt("station_id"));
        station.setStationName(res.getString("station_name"));
        return station;
    }

    private Map<String, Station> stationMap;

    public Station getStationByName(String name){
        // init map
        if(stationMap == null){
            stationMap = new HashMap<>();
        }
        // find in cache first
        if(stationMap.containsKey(name)){
            return stationMap.get(name);
        }
        // or find it in database, and refresh cache
        List<Station> stations = getAllStations();
        Station res = null;
        for(Station s : stations){
            if(name.equals(s.getStationName())){
                res = s;
            }
            stationMap.put(s.getStationName(), s);
        }
        // or insert it into database
        if(res == null){
            res = new Station();
            res.setStationName(name);
            insertStation(res);
            res = getStationByName(name);
        }
        return res;
    }

    private Station getStationById(int id) {
        return new Loader<>(this::getStationFromResult)
                .loadOne("select * from station where station_id = " + id);
    }

    @Override
    public List<Station> getAllStations() {
        return new Loader<>(this::getStationFromResult)
                .load("select * from station");
    }

    @Override
    public void insertTicketPoint(TicketPoint ticketPoint) {
        update("insert into ticket_point " +
                        "(username, address, open_time) values " +
                        "(?, ?, ?)",
                ticketPoint.getUsername(),
                ticketPoint.getAddress(),
                ticketPoint.getOpenTime()
        );
    }

    @Override
    public void deleteTicketPoint(TicketPoint ticketPoint) {
        deleteTicketPoint(ticketPoint.getPointId());
    }

    @Override
    public void deleteTicketPoint(int point_id) {
        update("delete from ticket_point where point_id = ?", point_id);
    }

    @Override
    public void updateTicketPoint(TicketPoint ticketPoint) {
        update("update ticket_point set username = ?, address = ?, open_time = ?" +
                        " where point_id = ?",
                ticketPoint.getUsername(), ticketPoint.getAddress(), ticketPoint.getOpenTime(),
                ticketPoint.getPointId());
    }

    @Override
    public TicketPoint getTicketPointById(int point_id) {
        return null;
    }

    private TicketPoint getTicketPointFromResult(ResultSet res) throws SQLException {
        TicketPoint point = new TicketPoint();
        point.setPointId(res.getInt("point_id"));
        point.setUsername(res.getString("username"));
        point.setAddress(res.getString("address"));
        point.setOpenTime(res.getString("open_time"));
        return point;
    }

    @Override
    public List<TicketPoint> getAllTicketPoints() {
        return new Loader<>(this::getTicketPointFromResult)
                .load("select * from ticket_point");
    }

    @Override
    public List<TicketPoint> searchTicketPoints(String key) {
        key = '%' + key + '%';
        return new Loader<>(this::getTicketPointFromResult)
                .load("select * from ticket_point where point_id = ? or username like ? or address like ?",
                        key, key, key);
    }

    @Override
    public void insertTrain(Train train) {
        update("insert into train " +
                        "(train_id, train_type, train_passby) values " +
                        "(?, ?, ?)",
                train.getTrainId(),
                train.getTrainType(),
                train.getTrainPassbyString()
        );
    }

    @Override
    public void deleteTrain(Train train) {
        deleteTrain(train.getTrainId());
    }

    @Override
    public void deleteTrain(String train_id) {
        update("delete from train where train_id = ?", train_id);
    }

    @Override
    public void updateTrain(Train train) {
        update("update train set train_type = ?, train_passby = ?" +
                        " where train_id = ?",
                train.getTrainType(), train.getTrainPassbyString(),
                train.getTrainId());
        if(train.getTrainPassby() != null){
            for(Passby passby : train.getTrainPassby()){
                update("insert into passby (train_id, station_order, depart_station, arrive_station, distance, stay_time) " +
                        "values( ?, ?, ?, ?, ?, ? )",
                        passby.getTrain().getTrainId(), passby.getStationOrder(), passby.getDepartStation().getStationId(), passby.getArriveStation().getStationId(),
                        passby.getDistance(), passby.getStayTime());
            }
        }
        if(train.getSeats() != null){
            for(SeatGroup sg : train.getSeats()){
                for(int i = 1; i <= sg.getSeatNum(); i++){
                    update("insert into seat (train_id, carriage_num, seat_num, seat_type) " +
                                    "values(?, ?, ?, ?)",
                            sg.getTrain().getTrainId(), sg.getCarriageNum(), i, sg.getSeatType());
                }
            }
        }
    }

    @Override
    public Train getTrainById(String train_id) {
        return null;
    }

    private Train getTrainFromResult(ResultSet res) throws SQLException {
        Train train = new Train();
        train.setTrainId(res.getString("train_id"));
        train.setTrainType(res.getString("train_type"));
        // load passby
        List<Passby> passby = new ArrayList<>();
        ResultSet pres = query("select * from passby where train_id = ?", train.getTrainId());
        while(pres.next()){
            Passby p = new Passby();
            p.setTrain(train);
            p.setStationOrder(pres.getInt("station_order"));
            p.setStayTime(pres.getInt("stay_time"));
            p.setDistance(pres.getFloat("distance"));
            p.setArriveStation(getStationById(pres.getInt("arrive_station")));
            p.setDepartStation(getStationById(pres.getInt("depart_station")));
            passby.add(p);
        }
        train.setTrainPassby(passby);
        pres.close();
        // load seats
        List<SeatGroup> seatGroups = new ArrayList<>();
        pres = query("select train_id, carriage_num, count(seat_num) as cnt, seat_type from seat where train_id = ?" +
                        " group by train_id, carriage_num, seat_type",
                train.getTrainId());
        while(pres.next()){
            SeatGroup sg = new SeatGroup();
            sg.setTrain(train);
            sg.setCarriageNum(pres.getInt("carriage_num"));
            sg.setSeatNum(pres.getInt("cnt"));
            sg.setSeatType(pres.getString("seat_type"));
            seatGroups.add(sg);
        }
        train.setSeats(seatGroups);
        return train;
    }

    @Override
    public List<Train> getAllTrains() {
        return new Loader<>(this::getTrainFromResult)
                .load("select * from train");
    }

    @Override
    public List<Train> searchTrains(String key) {
        String searchKey = '%' + key + '%';
        return new Loader<>(this::getTrainFromResult)
                .load("select * from train where train_id = ? or train_type = ? or train_passby like ?",
                        key, key, searchKey);
    }

    @Override
    public void insertTrainOrder(TrainOrder order) {
        update("insert into ticket_order " +
                        "(point_id, id_num, sche_id, seat_id, train_id, depart_station_order, arrive_station_order, depart_station, arrive_station, is_stu_ticket, money, order_state) values " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                order.getTicketPoint().getPointId(),
                order.getBuyer().getIdNum(),
                order.getTrainSchedule().getScheId(),
                order.getSeat().getSeatId(),
                order.getTrain().getTrainId(),
                order.getDepart_station_order(),
                order.getArriveStationOrder(),
                order.getDepartStation().getStationName(),
                order.getArriveStation().getStationName(),
                order.isStudentTicket(),
                order.getMoney(),
                order.getOrderState()
        );
    }

    @Override
    public void deleteTrainOrder(TrainOrder order) {
        deleteTrainOrder(order.getOrderId());
    }

    @Override
    public void deleteTrainOrder(int order_id) {
        update("delete from ticket_order where order_id = ?", order_id);
    }

    @Override
    public void updateTrainOrder(TrainOrder order) {
        update("update ticket_order set point_id = ?, id_num = ?, sche_id = ?, seat_id = ?, train_id = ?," +
                        " depart_station_order = ?, arrive_station_order = ?, depart_station = ?, arrive_station = ?," +
                        " is_stu_ticket = ?, money = ?, order_state = ?" +
                        " where order_id = ?",
                order.getTicketPoint().getPointId(), order.getBuyer().getIdNum(), order.getSeat().getSeatId(), order.getTrain().getTrainId(),
                order.getDepart_station_order(), order.getArriveStationOrder(), order.getDepartStation().getStationName(), order.getArriveStation().getStationName(),
                order.isStudentTicket(), order.getMoney(), order.getOrderState(),
                order.getOrderId());
    }

    private TrainOrder getOrderFromResult(ResultSet res) throws SQLException {
        TrainOrder order = new TrainOrder();
        order.setOrderId(res.getInt("order_id"));
        TicketPoint point = new TicketPoint();
        point.setPointId(res.getInt("point_id"));
        order.setTicketPoint(point);

        Customer buyer = new Customer();
        buyer.setIdNum(res.getString("id_num"));
        order.setBuyer(buyer);

        TrainSchedule schedule = new TrainSchedule();
        schedule.setScheId(res.getInt("sche_id"));
        order.setTrainSchedule(schedule);

        Train train = new Train();
        train.setTrainId(res.getString("train_id"));
        order.setTrain(train);

        order.setDepartStationRrder(res.getInt("depart_station_order"));
        order.setArriveStationOrder(res.getInt("arrive_station_order"));
        Station depart = new Station(), arrive = new Station();
        depart.setStationName(res.getString("depart_station"));
        arrive.setStationName(res.getString("arrive_station"));
        order.setDepartStation(depart);
        order.setArriveStation(arrive);

        order.setStudentTicket(res.getBoolean("is_stu_ticket"));
        order.setMoney(res.getFloat("money"));
        order.setOrderState(res.getInt("order_state"));

        return order;
    }

    @Override
    public List<TrainOrder> getTrainOrderList(int limit, int skip) {
        return new Loader<>(this::getOrderFromResult)
                .load("select * from ticket_order");
    }

    @Override
    public List<TrainOrder> searchTrainOrders(String key) {
        String likeKey = '%' + key + '%';
        return new Loader<>(this::getOrderFromResult)
                .load("select * from ticket_order where order_id = ? or point_id = ? or id_num like ?" +
                                " or sche_id = ? or train_id = ? or depart_station = ? or arrive_station = ?",
                        key, key, likeKey, key, key, key, key);
    }

    @Override
    public List<TrainOrder> getTrainOrderListByCustomer(Customer customer, int limit, int skip) {
        return null;
    }

    @Override
    public List<TrainOrder> getTrainOrderListBySeller(Manager seller, int limit, int skip) {
        return null;
    }

    @Override
    public void insertTrainSchedule(TrainSchedule schedule) {
        update("insert into train_schedule " +
                        "(depart_time, presell_time, speed, train_id) values " +
                        "(?, ?, ?, ?)",
                schedule.getDepartTime(),
                schedule.getPresellTime(),
                schedule.getSpeed(),
                schedule.getTrain().getTrainId()
        );
    }

    @Override
    public void deleteTrainSchedule(TrainSchedule trainSchedule) {
        deleteTrainSchedule(trainSchedule.getScheId());
    }

    @Override
    public void deleteTrainSchedule(int sche_id) {
        update("delete from train_schedule where sche_id = ?", sche_id);
    }

    @Override
    public void updateTrainSchedule(TrainSchedule schedule) {
        update("update train_schedule set depart_time = ?, presell_time = ?, speed = ?, train_id = ?" +
                        " where sche_id = ?",
                schedule.getDepartTime(), schedule.getPresellTime(), schedule.getSpeed(), schedule.getTrain().getTrainId(),
                schedule.getScheId());
    }

    private TrainSchedule getScheduleFromResult(ResultSet res) throws SQLException {
        TrainSchedule schedule = new TrainSchedule();
        schedule.setScheId(res.getInt("sche_id"));
        schedule.setDepartTime(res.getDate("depart_time"));
        schedule.setPresellTime(res.getDate("presell_time"));
        schedule.setSpeed(res.getFloat("speed"));
        Train train = new Train();
        train.setTrainId(res.getString("train_id"));
        schedule.setTrain(train);
        return schedule;
    }

    @Override
    public List<TrainSchedule> getTrainScheduleList(int limit, int skip) {
        return new Loader<>(this::getScheduleFromResult)
                .load("select * from train_schedule");
    }

    public List<TrainSchedule> searchTrainSchedule(String key) {
        return new Loader<>(this::getScheduleFromResult)
                .load("select * from train_schedule where sche_id = ? or depart_time = ? or presell_time = ? or train_id = ?",
                        key, key, key, key);
    }

    @Override
    public List<TrainSchedule> searchTrainSchedule(Date depart_date, Station depart_station, Station arrive_station) {
        return null;
    }

}
