package com.andreamapp.homeward.bean;

public class SeatAggregate {
    private String seat_type;
    private int seat_num;
    private float money;

    public String getSeatType() {
        return seat_type;
    }

    public void setSeatType(String seat_type) {
        this.seat_type = seat_type;
    }

    public int getSeatNum() {
        return seat_num;
    }

    public void setSeatNum(int seat_num) {
        this.seat_num = seat_num;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
