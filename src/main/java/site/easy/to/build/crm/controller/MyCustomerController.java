package site.easy.to.build.crm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;

@RestController
public class MyCustomerController {
    
    private final CustomerService customerService;
    private final TicketServiceImpl ticketServiceImpl;
    private final LeadServiceImpl leadServiceImpl;

    @Autowired
    public MyCustomerController(CustomerService c, TicketServiceImpl ticketServiceImpl,LeadServiceImpl leadServiceImpl){
        this.customerService = c;
        this.ticketServiceImpl = ticketServiceImpl;
        this.leadServiceImpl = leadServiceImpl;
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

    @GetMapping("/api/all-tickets")
    public List<Ticket> getAllTickets(){
        List<Ticket> tickets = new ArrayList<>();
        try {
            tickets = ticketServiceImpl.findAll();
        } catch (Exception e){
            return null;
        }
        return tickets;
    }

    @GetMapping("/api/all-leads")
    public List<Lead> getAllLeads(){
        List<Lead> Leads = new ArrayList<>();
        try {
            Leads = leadServiceImpl.findAll();
        } catch (Exception e){
            return null;
        }
        return Leads;
    }

}
