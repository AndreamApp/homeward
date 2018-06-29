package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Station;
import com.andreamapp.homeward.bean.TrainSchedule;

import java.util.Date;
import java.util.List;

public interface TrainScheduleDAO {
    void insertTrainSchedule(TrainSchedule trainSchedule);
    void deleteTrainSchedule(TrainSchedule trainSchedule);
    void deleteTrainSchedule(int train_schedule_id);
    void updateTrainSchedule(TrainSchedule trainSchedule);

    List<TrainSchedule> getTrainScheduleList(int limit, int skip);

    List<TrainSchedule> searchTrainSchedule(Date depart_date, Station depart_station, Station arrive_station, boolean isStudent);
}
