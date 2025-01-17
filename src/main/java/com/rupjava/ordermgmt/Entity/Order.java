package com.rupjava.ordermgmt.Entity;

import com.rupjava.ordermgmt.Service.OrderService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private Double totalBill;

    private String message;

    @ElementCollection
    @CollectionTable(name = "order_services", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "service_id")
    private List<Long> services;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
