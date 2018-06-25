package com.andreamapp.homeward.bean;

public class TrainOrder {
    private int order_id;
    private TicketPoint ticket_point;
    private Customer buyer;
    private TrainSchedule train_schedule;
    private Seat seat;
    private Train train;
    private int depart_station_order;
    private int arrive_station_order;
    private Station depart_station;
    private Station arrive_station;
    private boolean is_student_ticket;
    private float money;
    private State order_state;

    public enum State{
        Reserved, // 预订
        Cancled,  // 取消
        Payed,    // 支付
        Refunded, // 退款
    }

    public int getOrderId() {
        return order_id;
    }

    public void setOrderId(int order_id) {
        this.order_id = order_id;
    }

    public TicketPoint getTicketPoint() {
        return ticket_point;
    }

    public void setTicketPoint(TicketPoint ticket_point) {
        this.ticket_point = ticket_point;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public void setBuyer(Customer buyer) {
        this.buyer = buyer;
    }

    public TrainSchedule getTrainSchedule() {
        return train_schedule;
    }

    public void setTrainSchedule(TrainSchedule train_schedule) {
        this.train_schedule = train_schedule;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getDepart_station_order() {
        return depart_station_order;
    }

    public void setDepartStationRrder(int depart_station_order) {
        this.depart_station_order = depart_station_order;
    }

    public int getArriveStationOrder() {
        return arrive_station_order;
    }

    public void setArriveStationOrder(int arrive_station_order) {
        this.arrive_station_order = arrive_station_order;
    }

    public Station getDepartStation() {
        return depart_station;
    }

    public void setDepartStation(Station depart_station) {
        this.depart_station = depart_station;
    }

    public Station getArriveStation() {
        return arrive_station;
    }

    public void setArriveStation(Station arrive_station) {
        this.arrive_station = arrive_station;
    }

    public boolean isStudentTicket() {
        return is_student_ticket;
    }

    public void setStudentTicket(boolean is_student_ticket) {
        this.is_student_ticket = is_student_ticket;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public State getOrderState() {
        return order_state;
    }

    public void setOrderState(State order_state) {
        this.order_state = order_state;
    }
}
