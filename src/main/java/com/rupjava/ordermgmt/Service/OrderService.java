package com.rupjava.ordermgmt.Service;

import com.rupjava.ordermgmt.Entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order createOrder(Order order, List<Long> serviceIds);

    void deleteOrder(Long id);

    Order getOrderById(Long id);
    Order updateOrder(Long id, Order orderDetails);


}
