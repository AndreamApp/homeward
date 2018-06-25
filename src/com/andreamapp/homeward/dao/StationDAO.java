package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Station;

import java.util.List;

public interface StationDAO {
    void insertStation(Station station);
    void deleteStation(Station station);
    void deleteStation(String station_id);
    void updateStation(Station station);

    List<Station> getAllStations();
}
