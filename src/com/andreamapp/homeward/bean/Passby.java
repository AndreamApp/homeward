package com.andreamapp.homeward.bean;

public class Passby {
    private Train train;
    private int station_order;
    private Station depart_station;
    private Station arrive_station;
    private float distance;
    private int stay_time; // minutes

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getStationOrder() {
        return station_order;
    }

    public void setStationOrder(int station_order) {
        this.station_order = station_order;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getStayTime() {
        return stay_time;
    }

    public void setStayTime(int stay_time) {
        this.stay_time = stay_time;
    }
}
