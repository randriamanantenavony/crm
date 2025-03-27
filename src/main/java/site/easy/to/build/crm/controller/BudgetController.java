package site.easy.to.build.crm.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.checkerframework.checker.units.qual.C;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.BudgetInfo;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.budget.BudgetService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/admin/budget")
public class BudgetController {
    
    private final BudgetService budgetService;
    private final CustomerRepository customerRepository;

    public BudgetController(BudgetService budgetService,CustomerRepository customerRepository) {
        this.budgetService = budgetService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/list")
    public String showBudgets(Model model) {
        // List<Budget> budgets = budgetService.findAll();
        List<BudgetInfo> budgets = budgetService.getBudgetInfo(); 
        model.addAttribute("budgets", budgets);
        return "budget/liste"; // Si ton fichier est dans templates/admin/budget-list.html
    }

   @GetMapping("/new")
    public String showBudgetForm(Model model) {
        model.addAttribute("budget", new Budget());
        model.addAttribute("customers", customerRepository.findAll());
        return "budget/save"; // le nom de ton fichier HTML/Thymeleaf
    }

    @PostMapping("/save")
    public String saveBudget(@RequestParam("customerId") Integer customerId,
                             @RequestParam("name") String name,
                             @RequestParam("totalAmount") BigDecimal totalAmount,
                             @RequestParam("dateAjout") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date dateAjout) {
    
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
    
        Budget budget = new Budget();
        budget.setCustomer(customer);
        budget.setName(name);
        budget.setTotalAmount(totalAmount);
        budget.setDateAjout(new java.sql.Date(dateAjout.getTime())); // Important si `dateAjout` est `java.sql.Date`
    
        budgetService.save(budget);
    
        return "redirect:/admin/budget/list";
    }
    
    

    
}
