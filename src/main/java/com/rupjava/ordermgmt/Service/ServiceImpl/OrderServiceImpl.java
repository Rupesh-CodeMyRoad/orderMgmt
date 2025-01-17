package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Entity.Order;
import com.rupjava.ordermgmt.Entity.ServiceSubCategory;
import com.rupjava.ordermgmt.Repository.OrderRepository;
import com.rupjava.ordermgmt.Repository.ServicesCategoryRepository;
import com.rupjava.ordermgmt.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServicesCategoryRepository servicesCategoryRepository;


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order, List<Long> serviceIds) {
        // Link services to the order
        order.setServices(serviceIds); // Directly set the list of service IDs

        // Save the order
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
