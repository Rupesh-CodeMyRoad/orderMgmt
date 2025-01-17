package com.rupjava.ordermgmt.Repository;

import com.rupjava.ordermgmt.Entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesCategoryRepository extends JpaRepository<ServiceCategory, Long> {
}
