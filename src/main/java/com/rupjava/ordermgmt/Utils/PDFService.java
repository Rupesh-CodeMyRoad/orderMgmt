package com.rupjava.ordermgmt.Utils;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.rupjava.ordermgmt.Entity.Customer;
import com.rupjava.ordermgmt.Entity.Order;
import com.rupjava.ordermgmt.Entity.ServiceCategory;
import com.rupjava.ordermgmt.Entity.User;
import com.rupjava.ordermgmt.Repository.ServicesCategoryRepository;
import com.rupjava.ordermgmt.Repository.UserRepository;
import com.rupjava.ordermgmt.Service.OrderService;
import com.rupjava.ordermgmt.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PDFService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServicesCategoryRepository serviceCategoryRepository;


    public byte[] generateNonAdminUserPDF() {
        List<User> nonAdminUsers = userService.getAllUsers().stream()
                .filter(user -> !"ADMIN".equals(user.getRole()))
                .toList();

        // Create a ByteArrayOutputStream to write PDF content to
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Create PDF writer
            PdfWriter writer = new PdfWriter(outputStream);

            // Initialize PDF document
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            // Add Title
            document.add(new Paragraph("Non-Admin Users List").setBold().setFontSize(18));

            // Create a table with appropriate columns
            Table table = new Table(new float[]{2, 3, 3, 3});
            table.addHeaderCell("ID");
            table.addHeaderCell("Name");
            table.addHeaderCell("Email");
            table.addHeaderCell("Verified");

            // Add rows for each user
            for (User user : nonAdminUsers) {
                table.addCell(String.valueOf(user.getId()));
                table.addCell(user.getName());
                table.addCell(user.getEmail());
                table.addCell(String.valueOf(user.isVerified()));
            }

            // Add table to document
            document.add(table);

            // Close document
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return outputStream.toByteArray(); // Return PDF content as byte array
    }

    public byte[] generateCustomerPDF() {
        List<Order> orders = orderService.getAllOrders();

        // Fetch service names for all service IDs
        Map<Long, String> serviceNameMap = serviceCategoryRepository.findAll().stream()
                .collect(Collectors.toMap(ServiceCategory::getId, ServiceCategory::getName));

        // Group orders by customers
        Map<Customer, List<Order>> customerOrdersMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer));

        // Create a ByteArrayOutputStream to write PDF content to
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Create PDF writer
            PdfWriter writer = new PdfWriter(outputStream);

            // Initialize PDF document
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            // Add Title
            document.add(new Paragraph("Customer Orders Report").setBold().setFontSize(18));

            // Iterate over each customer and their orders
            for (Map.Entry<Customer, List<Order>> entry : customerOrdersMap.entrySet()) {
                Customer customer = entry.getKey();
                List<Order> customerOrders = entry.getValue();

                // Add customer details
                document.add(new Paragraph("Customer Name: " + customer.getName()).setBold());
                document.add(new Paragraph("Mobile: " + customer.getMobile()));
                document.add(new Paragraph("Email: " + (customer.getEmail() != null ? customer.getEmail() : "N/A")));

                // Add a table for the customer's orders
                Table table = new Table(new float[]{1, 2, 3, 4});
                table.addHeaderCell("Order ID");
                table.addHeaderCell("Total Bill");
                table.addHeaderCell("Message");
                table.addHeaderCell("Services");

                for (Order order : customerOrders) {
                    table.addCell(String.valueOf(order.getId()));
                    table.addCell("$" + order.getTotalBill());
                    table.addCell(order.getMessage() != null ? order.getMessage() : "N/A");

                    // Convert service IDs to names
                    String services = order.getServices().stream()
                            .map(serviceNameMap::get) // Map ID to name
                            .filter(name -> name != null) // Remove null values
                            .collect(Collectors.joining(", "));
                    table.addCell(services);
                }

                // Add the table to the document
                document.add(table);

                // Add a blank line between customers
                document.add(new Paragraph("\n"));
            }

            // Close document
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return outputStream.toByteArray(); // Return PDF content as byte array
    }

}
