package site.easy.to.build.crm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.customer.CustomerService;

@RestController
public class MyCustomerController {
    
    private final CustomerService customerService;

    @Autowired
    public MyCustomerController(CustomerService c){
        this.customerService = c;
    }

    @GetMapping("/api/all-customers")
    public List<Customer> getAllCustomers(){
        List<Customer> customers = new ArrayList<>();
        try {
            customers = customerService.findAll();
        } catch (Exception e){
            return null;
        }
        return customers;
    }


}
