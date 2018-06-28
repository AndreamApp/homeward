package com.andreamapp.homeward.bean;

import java.util.Date;

public class TrainSchedule {
    private int sche_id;
    private Date depart_time;
    private Date presell_time;
    private float speed;
    private Train train;
    //

    public int getScheId() {
        return sche_id;
    }

    public void setScheId(int sche_id) {
        this.sche_id = sche_id;
    }

    public Date getDepartTime() {
        return depart_time;
    }

    public void setDepartTime(Date depart_time) {
        this.depart_time = depart_time;
    }

    public Date getPresellTime() {
        return presell_time;
    }

    public void setPresellTime(Date presell_time) {
        this.presell_time = presell_time;
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
