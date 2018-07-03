package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.Customer;
import com.andreamapp.homeward.bean.Manager;
import com.andreamapp.homeward.bean.TrainOrder;

import java.util.List;

public interface TrainOrderDAO {
    int insertTrainOrder(TrainOrder order);
    void deleteTrainOrder(TrainOrder order);
    void deleteTrainOrder(int order_id);
    void updateTrainOrder(TrainOrder order);

    List<TrainOrder> getTrainOrderList(int limit, int skip);
    List<TrainOrder> getTrainOrderListByCustomer(Customer customer, int limit, int skip);
    List<TrainOrder> getTrainOrderListBySeller(Manager seller, int limit, int skip);
    List<TrainOrder> searchTrainOrders(String key);
}
