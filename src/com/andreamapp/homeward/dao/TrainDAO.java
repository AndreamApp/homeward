package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Train;

import java.util.List;

public interface TrainDAO {
    void insertTrain(Train train);
    void deleteTrain(Train train);
    void deleteTrain(String train_id);
    void updateTrain(Train train);

    List<Train> getAllTrains();
    Train getTrainById(String train_id);
}
