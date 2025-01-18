package com.rupjava.ordermgmt.Controller;

import com.rupjava.ordermgmt.Dto.OrderRequest;
import com.rupjava.ordermgmt.Entity.*;
import com.rupjava.ordermgmt.Service.*;
import com.rupjava.ordermgmt.Utils.PDFService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class ApiController {
    @Autowired
    private ServicesCategoryService serviceCategoryService;
    private final UserService userService;
    @Autowired
    private ServicesSubCategoryService serviceSubCategoryService;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private OrderService orderService;


    @Autowired
    private CustomerService customerService;

    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getDetail")
    public ResponseEntity<Map<String, Object>> getPublicInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("name", user.getName());
            userDetails.put("profilePicUrl", user.getProfilePicUrl());
            return ResponseEntity.ok(userDetails);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsersExceptAdmins() {
        List<User> users = userService.getAllUsers().stream()
                .filter(user -> !"ADMIN".equals(user.getRole()))
                .filter(user -> Boolean.TRUE.equals(user.isVerified()))
                .toList();
        return ResponseEntity.ok(users);
    }

    //Get All customers
    @GetMapping("/getAllList")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        System.out.println(customers.toString());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/download-non-admin-pdf")
    public ResponseEntity<byte[]> downloadNonAdminUserPDF() {
        byte[] pdfContent = pdfService.generateNonAdminUserPDF();

        // Set headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "NonAdminUsers.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }

    @GetMapping("/download-order-customer-pdf")
    public ResponseEntity<byte[]> downloadCustomerPDF() {
        byte[] pdfContent = pdfService.generateCustomerPDF();

        // Set headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Order-Customer.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }

    // Get a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // Create a new customer
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok(newCustomer);
    }

    // Update a customer
    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

    // Delete a customer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/sub")
    public ResponseEntity<List<ServiceSubCategory>> getAllSubCategories() {
        return ResponseEntity.ok(serviceSubCategoryService.getAllSubCategories());
    }

    @PostMapping("/category/sub")
    public ResponseEntity<Void> createSubCategory(@RequestBody ServiceSubCategory subCategory) {
        serviceSubCategoryService.createSubCategory(subCategory);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/category/sub/{id}")
    public ResponseEntity<ServiceSubCategory> updateSubCategory(
            @PathVariable Long id,
            @RequestBody ServiceSubCategory subCategory) {
        return ResponseEntity.ok(serviceSubCategoryService.updateSubCategory(id, subCategory));
    }

    @DeleteMapping("/category/sub/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        serviceSubCategoryService.deleteSubCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category")
    public ResponseEntity<List<ServiceCategory>> getAllCategories() {
        return ResponseEntity.ok(serviceCategoryService.getAllCategories());
    }

    @PostMapping("/category")
    public ResponseEntity<ServiceCategory> createCategory(@RequestBody ServiceCategory category) {
        return ResponseEntity.ok(serviceCategoryService.createCategory(category));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<ServiceCategory> updateCategory(
            @PathVariable Long id,
            @RequestBody ServiceCategory category) {
        return ResponseEntity.ok(serviceCategoryService.updateCategory(id, category));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        serviceCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Get an order by ID
    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Create a new order
    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderRequestMapping(orderRequest);
        List<Long> serviceIds = order.getServices(); // Extract the services from the request
        Order createdOrder = orderService.createOrder(order, serviceIds);
        return ResponseEntity.ok(createdOrder);
    }

    // Update an order
    @PutMapping("/order/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderDetails) {
        Order order = orderRequestMapping(orderDetails);
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    // Delete an order
    @DeleteMapping("/order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private Order orderRequestMapping(OrderRequest orderRequest) {
        Order order = new Order();
        order.setCustomer(customerService.getCustomerById(orderRequest.getCustomerId()));
        order.setServices(orderRequest.getServices());
        order.setCreatedAt(orderRequest.getCreatedAt());
        order.setTotalBill(orderRequest.getTotalBill());
        order.setMessage(orderRequest.getMessage());
        return order;
    }

}
