package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Entity.ServiceCategory;
import com.rupjava.ordermgmt.Repository.ServicesCategoryRepository;
import com.rupjava.ordermgmt.Service.ServicesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public ServiceCategory updateCategory(Long id, ServiceCategory category) {
        Optional<ServiceCategory> existingCategory = repository.findById(id);
        if (existingCategory.isPresent()) {
            ServiceCategory updatedCategory = existingCategory.get();
            updatedCategory.setName(category.getName());
            updatedCategory.setCategoryImage(category.getCategoryImage());
            updatedCategory.setStatus(category.getStatus());
            return repository.save(updatedCategory);
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
