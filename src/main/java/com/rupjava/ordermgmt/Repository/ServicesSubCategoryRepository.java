package com.rupjava.ordermgmt.Repository;

import com.rupjava.ordermgmt.Entity.ServiceSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesSubCategoryRepository extends JpaRepository<ServiceSubCategory, Long> {
}
