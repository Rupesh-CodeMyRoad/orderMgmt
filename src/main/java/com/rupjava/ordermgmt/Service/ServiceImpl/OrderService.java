package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Entity.Customer;
import com.rupjava.ordermgmt.Entity.Order;
import com.rupjava.ordermgmt.Repository.CustomerRepository;
import com.rupjava.ordermgmt.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }

    public Order createOrder(Order order) {
        // Validate customer
        Optional<Customer> customerOptional = customerRepository.findById(order.getCustomer().getId());
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + order.getCustomer().getId());
        }

        // Set the customer entity
        order.setCustomer(customerOptional.get());

        // Save order
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id);

        // Update fields
        order.setCustomer(orderDetails.getCustomer());
        order.setTotalBill(orderDetails.getTotalBill());
        order.setMessage(orderDetails.getMessage());
        order.setServices(orderDetails.getServices());

        // Save updated order
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
