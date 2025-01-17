package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Entity.ServiceSubCategory;
import com.rupjava.ordermgmt.Repository.ServicesSubCategoryRepository;
import com.rupjava.ordermgmt.Service.ServicesSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesSubCategoryServiceImpl implements ServicesSubCategoryService {
    @Autowired
    private ServicesSubCategoryRepository repository;

    public List<ServiceSubCategory> getAllSubCategories() {
        return repository.findAll();
    }

    public ServiceSubCategory createSubCategory(ServiceSubCategory subCategory) {
        return repository.save(subCategory);
    }

    public void deleteSubCategory(Long id) {
        repository.deleteById(id);
    }
}
