package com.rupjava.ordermgmt.Entity;

import com.rupjava.ordermgmt.Utils.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services_category")
public class ServiceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String categoryImage;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
}
