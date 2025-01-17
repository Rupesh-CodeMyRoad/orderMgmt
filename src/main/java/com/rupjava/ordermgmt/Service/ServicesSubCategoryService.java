package com.rupjava.ordermgmt.Service;

import com.rupjava.ordermgmt.Entity.ServiceSubCategory;

import java.util.List;

public interface ServicesSubCategoryService {

    ServiceSubCategory createSubCategory(ServiceSubCategory subCategory);

    void deleteSubCategory(Long id);

    List<ServiceSubCategory> getAllSubCategories();
}
