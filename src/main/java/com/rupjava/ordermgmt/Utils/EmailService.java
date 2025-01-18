package com.rupjava.ordermgmt.Utils;

import com.rupjava.ordermgmt.Entity.Customer;
import com.rupjava.ordermgmt.Entity.Order;
import com.rupjava.ordermgmt.Entity.ServiceCategory;
import com.rupjava.ordermgmt.Repository.ServicesCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ServicesCategoryRepository servicesCategoryRepository;

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            // Formatting the email content
            String emailContent = String.format(
                    "Dear User,\n\n"
                            + "We received a request to verify your account. Please use the following OTP (One-Time Password) to complete the verification process:\n\n"
                            + "OTP: %s\n\n"
                            + "Note: This OTP is valid for 5 minutes. Please do not share this code with anyone.\n\n"
                            + "If you did not request this, please ignore this email.\n\n"
                            + "Best regards,\n"
                            + "NStyle Beauty Lounge",
                    otpCode
            );

            // Creating the email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP for Account Verification");
            message.setText(emailContent);
            message.setFrom("yourcompany@example.com");

            // Sending the email
            mailSender.send(message);
            System.out.println("OTP email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void sendInvoice(Customer customer, Order order) {
        try {

            List<String> serviceList = servicesCategoryRepository.findAllById(order.getServices())
                    .stream()
                    .map(ServiceCategory::getName) // Extract the names from each ServicesCategory
                    .collect(Collectors.toList()); // Collect the names into a list


            // Formatting email
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Dear ").append(customer.getName()).append(",\n\n")
                    .append("Thank you for your order. Please find the details of your invoice below:\n\n")
                    .append("Customer Name: ").append(customer.getName()).append("\n")
                    .append("Services: ").append(String.join(", ", serviceList)).append("\n")
                    .append("Total Price: $").append(order.getTotalBill()).append("\n")
                    .append("Message: ").append(order.getMessage()).append("\n\n")
                    .append("Thank You!\n\n")
                    .append("Best regards,\n")
                    .append("NStyle Beauty Lounge");

            // Sending Mail
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(customer.getEmail());
            message.setSubject("Invoice for Your Order");
            message.setText(emailContent.toString());
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + customer.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
