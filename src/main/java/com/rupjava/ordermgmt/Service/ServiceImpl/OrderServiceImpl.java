package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Entity.Customer;
import com.rupjava.ordermgmt.Entity.Order;
import com.rupjava.ordermgmt.Repository.CustomerRepository;
import com.rupjava.ordermgmt.Repository.OrderRepository;
import com.rupjava.ordermgmt.Service.OrderService;
import com.rupjava.ordermgmt.Utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order, List<Long> serviceIds) {

        Customer customer = order.getCustomer();
        order.setServices(serviceIds);
        emailService.sendInvoice(customer, order);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }

    @Override
    public Order updateOrder(Long id, Order orderDetails) {
        Order existingOrder = getOrderById(id);

        // Update fields
        existingOrder.setCustomer(orderDetails.getCustomer());
        existingOrder.setTotalBill(orderDetails.getTotalBill());
        existingOrder.setMessage(orderDetails.getMessage());
        existingOrder.setServices(orderDetails.getServices());

        // Save updated order
        return orderRepository.save(existingOrder);
    }
}
