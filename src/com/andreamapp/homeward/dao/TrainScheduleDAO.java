package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.*;

import javax.swing.table.TableModel;
import java.util.Date;
import java.util.List;

public interface TrainScheduleDAO {
    void insertTrainSchedule(TrainSchedule trainSchedule);
    void deleteTrainSchedule(TrainSchedule trainSchedule);
    void deleteTrainSchedule(int train_schedule_id);
    void updateTrainSchedule(TrainSchedule trainSchedule);

    List<TrainSchedule> getTrainScheduleList(int limit, int skip);
    List<TrainSchedule> searchTrainSchedule(Date depart_date, Station depart_station, Station arrive_station);
    TableModel getTrainScheduleModel(List<TrainSchedule> schedules);
}
