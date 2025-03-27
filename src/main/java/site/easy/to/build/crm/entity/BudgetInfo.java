package site.easy.to.build.crm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "v_detail_budget")
public class BudgetInfo {

    @Id
    private Long budgetId;  // Utilisé comme clé primaire
    private Long customerId;
    private String name;
    private BigDecimal totalAmount;
    private LocalDate dateAjout;
    private String customerEmail;

    public BudgetInfo() {
        // Constructeur par défaut
    }

    // Constructeur avec paramètres
    public BudgetInfo(Long budgetId, Long customerId, String name, BigDecimal totalAmount, LocalDate dateAjout, String customerEmail) {
        this.budgetId = budgetId;
        this.customerId = customerId;
        this.name = name;
        this.totalAmount = totalAmount;
        this.dateAjout = dateAjout;
        this.customerEmail = customerEmail;
    }

    // Getters et Setters
    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
