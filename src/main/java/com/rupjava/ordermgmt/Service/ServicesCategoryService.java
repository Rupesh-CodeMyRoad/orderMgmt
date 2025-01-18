package com.rupjava.ordermgmt.Service;

import com.rupjava.ordermgmt.Entity.ServiceCategory;

import java.util.List;

public interface ServicesCategoryService {

    List<ServiceCategory> getAllCategories();

    ServiceCategory createCategory(ServiceCategory category);

    void deleteCategory(Long id);

    ServiceCategory updateCategory(Long id, ServiceCategory category);


}
