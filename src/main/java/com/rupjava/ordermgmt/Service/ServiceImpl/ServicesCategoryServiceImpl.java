package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Entity.ServiceCategory;
import com.rupjava.ordermgmt.Repository.ServicesCategoryRepository;
import com.rupjava.ordermgmt.Service.ServicesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesCategoryServiceImpl implements ServicesCategoryService {
    @Autowired
    private ServicesCategoryRepository repository;

    public List<ServiceCategory> getAllCategories() {
        return repository.findAll();
    }

    public ServiceCategory createCategory(ServiceCategory category) {
        return repository.save(category);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
