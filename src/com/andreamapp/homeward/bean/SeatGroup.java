package com.andreamapp.homeward.bean;

public class SeatGroup {
    private Train train;
    private int carriage_num;
    private int seat_num;
    private String seat_type;

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getCarriageNum() {
        return carriage_num;
    }

    public void setCarriageNum(int carriage_num) {
        this.carriage_num = carriage_num;
    }

    public int getSeatNum() {
        return seat_num;
    }

    public void setSeatNum(int seat_num) {
        this.seat_num = seat_num;
    }

    public String getSeatType() {
        return seat_type;
    }

    public void setSeatType(String seat_type) {
        this.seat_type = seat_type;
    }
}
