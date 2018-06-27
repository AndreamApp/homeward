package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Station;

import javax.swing.table.TableModel;
import java.util.List;

public interface StationDAO {
    void insertStation(Station station);
    void deleteStation(Station station);
    void deleteStation(int station_id);
    void updateStation(Station station);

    List<Station> getAllStations();
    TableModel getStationModel(List<Station> stations);
}
