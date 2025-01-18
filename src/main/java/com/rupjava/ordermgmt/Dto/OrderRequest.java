package com.rupjava.ordermgmt.Dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {

    private long id;

    private long customerId;

    @Column(nullable = false)
    private Double totalBill;

    private String message;

    private List<Long> services;

    private LocalDateTime createdAt = LocalDateTime.now();
}
