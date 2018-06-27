package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Train;

import javax.swing.table.TableModel;
import java.util.List;

public interface TrainDAO {
    void insertTrain(Train train);
    void deleteTrain(Train train);
    void deleteTrain(String train_id);
    void updateTrain(Train train);

    Train getTrainById(String train_id);
    List<Train> getAllTrains();
    TableModel getTrainModel(List<Train> trains);
}
