package com.andreamapp.homeward.bean;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private String train_id;
    private String train_type;
    private List<Passby> train_passby;
    private List<SeatGroup> seats;

    public String getTrainId() {
        return train_id;
    }

    public void setTrainId(String train_id) {
        this.train_id = train_id;
    }

    public String getTrainType() {
        return train_type;
    }

    public void setTrainType(String train_type) {
        this.train_type = train_type;
    }

    public List<Passby> getTrainPassby() {
        return train_passby;
    }

    public void setTrainPassby(List<Passby> train_passby) {
        this.train_passby = train_passby;
    }

    public String getTrainPassbyString() {
        StringBuilder passby = new StringBuilder('|');
        if(train_passby != null){
            for(Passby p : train_passby){
                passby.append(p.getDepartStation().getStationName()).append('|');
            }
        }
        return passby.toString();
    }

    public List<SeatGroup> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatGroup> seats) {
        this.seats = seats;
    }
}
