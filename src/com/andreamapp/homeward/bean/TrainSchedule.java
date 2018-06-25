package com.andreamapp.homeward.bean;

import java.util.Date;

public class TrainSchedule {
    private String sche_id;
    private Date depart_time;
    private float speed;
    private Train train;

    public String getSche_id() {
        return sche_id;
    }

    public void setSche_id(String sche_id) {
        this.sche_id = sche_id;
    }

    public Date getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(Date depart_time) {
        this.depart_time = depart_time;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}
