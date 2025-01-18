package com.rupjava.ordermgmt.Service.ServiceImpl;

import com.rupjava.ordermgmt.Entity.ServiceCategory;
import com.rupjava.ordermgmt.Entity.ServiceSubCategory;
import com.rupjava.ordermgmt.Repository.ServicesCategoryRepository;
import com.rupjava.ordermgmt.Repository.ServicesSubCategoryRepository;
import com.rupjava.ordermgmt.Service.ServicesSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicesSubCategoryServiceImpl implements ServicesSubCategoryService {
    @Autowired
    private ServicesSubCategoryRepository repository;


    @Autowired
    private ServicesCategoryRepository categoryRepository;

    public List<ServiceSubCategory> getAllSubCategories() {
        return repository.findAll();
    }

    public ServiceSubCategory createSubCategory(ServiceSubCategory subCategory) {
        // Validate the category ID
        Long categoryId = subCategory.getCategory().getId();
        Optional<ServiceCategory> categoryOptional = categoryRepository.findById(categoryId);

        if (categoryOptional.isEmpty()) {
            throw new RuntimeException("Category with ID " + categoryId + " not found.");
        }

        // Set the category entity to the subcategory
        subCategory.setCategory(categoryOptional.get());

        // Save the subcategory
        return repository.save(subCategory);
    }

    public ServiceSubCategory updateSubCategory(Long id, ServiceSubCategory subCategory) {
        Optional<ServiceSubCategory> existingSubCategory = repository.findById(id);
        if (existingSubCategory.isPresent()) {
            ServiceSubCategory updatedSubCategory = existingSubCategory.get();
            updatedSubCategory.setName(subCategory.getName());
            updatedSubCategory.setCategory(subCategory.getCategory());
            updatedSubCategory.setStatus(subCategory.getStatus());
            return repository.save(updatedSubCategory);
        } else {
            throw new RuntimeException("Sub-Category not found with ID: " + id);
        }
    }

    public void deleteSubCategory(Long id) {
        repository.deleteById(id);
    }
}
