package com.andreamapp.homeward.dao;

import com.andreamapp.homeward.bean.TicketPoint;

import java.util.List;

public interface TicketPointDAO {
    void insertTicketPoint(TicketPoint ticketPoint);
    void deleteTicketPoint(TicketPoint ticketPoint);
    void deleteTicketPoint(int point_id);
    void updateTicketPoint(TicketPoint ticketPoint);

    List<TicketPoint> getAllTicketPoints();
    TicketPoint getTicketPointById(int point_id);
}
